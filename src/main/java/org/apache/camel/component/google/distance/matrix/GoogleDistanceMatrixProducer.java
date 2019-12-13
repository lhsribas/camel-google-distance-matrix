package org.apache.camel.component.google.distance.matrix;

import org.apache.camel.Exchange;
import org.apache.camel.component.google.distance.matrix.dto.FilterMatrix;
import org.apache.camel.component.google.distance.matrix.service.impl.DistanceMatrixServiceImpl;
import org.apache.camel.component.google.distance.matrix.service.impl.HaversineServiceImpl;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The GoogleDistanceMatrix producer.
 */
public class GoogleDistanceMatrixProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleDistanceMatrixProducer.class);
    private GoogleDistanceMatrixEndpoint endpoint;

    private Map<Map<Double, Double>, Map<Double, Double>> coordinatesToGoogleDistanceMatrix;

    public GoogleDistanceMatrixProducer(GoogleDistanceMatrixEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {

        final Map<Double, Double> origin = exchange.getProperty("LatLongOrigin", Map.class);
        final Map<Double, Double> destination = exchange.getProperty("LatLongDestination", Map.class);

        /*
         * If is enabled call the Haversine algorithm;
         */
        harvesineIsEnabled(origin, destination);

        /*
         * Using Google
         */
        googleDistanceMatrix(exchange);
    }

    /**
     * @param origin
     * @param destination
     */
    private void harvesineIsEnabled(final Map origin, final Map destination) throws Exception {
        Objects.nonNull(origin);
        Objects.nonNull(destination);

        if (endpoint.getHaversine()) {
            filterGeolozalizationsWithHaversine(origin,
                    destination,
                    endpoint.getRadius());
        } else {
            this.coordinatesToGoogleDistanceMatrix.put(origin, destination);
        }
    }

    /**
     * @param origin
     * @param destination
     * @param radius
     */
    private void filterGeolozalizationsWithHaversine(final Map<Double, Double> origin, final Map<Double, Double> destination, final Double radius) throws Exception {
        this.coordinatesToGoogleDistanceMatrix = new HaversineServiceImpl().getLatLongToSendForGoogleDistanceMatrix(origin, destination, radius);
    }

    private void googleDistanceMatrix(Exchange exchange) throws Exception {

        AtomicInteger countPipe = new AtomicInteger();
        StringBuffer bufferOrigin = new StringBuffer();
        StringBuffer bufferDestination = new StringBuffer();

        this.coordinatesToGoogleDistanceMatrix.forEach((_origin, _destination) -> {

            /*counter*/
            countPipe.set(_destination.size());

            _origin.forEach((_lat, _long) -> {
                bufferOrigin.append(_lat).append(",").append(_long);
            });

            _destination.forEach((_lat, _long) -> {
                bufferDestination.append(_lat).append(",").append(_long);

                if (countPipe.get() > 1) {
                    bufferDestination.append("|");
                    countPipe.addAndGet(-1);
                }
            });
        });

        FilterMatrix filterMatrix = new FilterMatrix();
        filterMatrix.setOrigins(bufferOrigin.toString());
        filterMatrix.setDestinations(bufferDestination.toString());
        filterMatrix.setMode(endpoint.getMode());
        filterMatrix.setLanguage(endpoint.getLanguage());
        filterMatrix.setSensor(endpoint.getSensor());
        filterMatrix.setUnits(endpoint.getUnit());
        filterMatrix.setType(endpoint.getType());
        filterMatrix.setConnectionTimeout(endpoint.getConnectionTimeout());
        filterMatrix.setSocketTimeout(endpoint.getSocketTimeout());
        filterMatrix.setKey(endpoint.getKey());

        final String distance = new DistanceMatrixServiceImpl().distanceInfo(filterMatrix);

        exchange.getOut().setBody(distance);
        exchange.getIn().setBody(distance);
    }
}
