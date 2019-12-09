package org.apache.camel.component.google.distance.matrix;

import org.apache.camel.Exchange;
import org.apache.camel.component.google.distance.matrix.dto.FilterMatrix;
import org.apache.camel.component.google.distance.matrix.exception.NotFoundLatitudeLongitudeException;
import org.apache.camel.component.google.distance.matrix.service.impl.DistanceMatrix;
import org.apache.camel.component.google.distance.matrix.service.impl.Haversine;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The GoogleDistanceMatrix producer.
 */
public class GoogleDistanceMatrixProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleDistanceMatrixProducer.class);
    private GoogleDistanceMatrixEndpoint endpoint;

    private Map<Map<Double, Double>, Map<Double, Double>> coordinatesToGoogleDistanceMatrix;

    private String contentType;
    private String mode;
    private String language;
    private Boolean sensor = false;
    private String units;

    public GoogleDistanceMatrixProducer(GoogleDistanceMatrixEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {

        final Map<Double, Double> origin = exchange.getProperty("LatLongOrigin", Map.class);
        final Map<Double, Double> destination = exchange.getProperty("LatLongDestination", Map.class);

        /*
         * Verify if is not null or empty
         */
        launcherLatLongExecetion(origin, destination);

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
     * Note: if the Latitude or Longitude of origin or destination is null or empty this is throw
     * {@Link NotFoundLatitudeLongitudeException}
     *
     * @param origin
     * @param destination
     */
    private void launcherLatLongExecetion(final Map origin, final Map destination) throws NotFoundLatitudeLongitudeException {
        if ((origin == null || origin.isEmpty()) || (destination == null || destination.isEmpty())) {
            throw new NotFoundLatitudeLongitudeException("Error, Latitude or Longitude Not Found!");
        }
    }

    /**
     * @param origin
     * @param destination
     */
    private void harvesineIsEnabled(final Map origin, final Map destination) {
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
    private void filterGeolozalizationsWithHaversine(final Map<Double, Double> origin, final Map<Double, Double> destination, final Double radius) {
        this.coordinatesToGoogleDistanceMatrix = new Haversine().getLatLongToSendForGoogleDistanceMatrix(origin, destination, radius);
    }

    private void googleDistanceMatrix(Exchange exchange) throws Exception {

        AtomicInteger countPipe = new AtomicInteger();
        StringBuffer bufferOrigin = new StringBuffer();
        StringBuffer bufferDestination = new StringBuffer();

        this.coordinatesToGoogleDistanceMatrix.forEach((_origin, _destination) -> {
            _origin.forEach((_lat, _long) -> {
                bufferOrigin.append(_lat).append(",").append(_long);
            });

            countPipe.set(_destination.size());

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

        String _data = new DistanceMatrix().distanceInfo(filterMatrix);

        exchange.getOut().setBody(_data);
    }
}
