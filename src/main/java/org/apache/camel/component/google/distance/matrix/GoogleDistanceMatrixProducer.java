package org.apache.camel.component.google.distance.matrix;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * The GoogleDistanceMatrix producer.
 */
public class GoogleDistanceMatrixProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleDistanceMatrixProducer.class);
    private GoogleDistanceMatrixEndpoint endpoint;

    private  Map<Map<Double, Double>, Map<Double, Double>> coordinatesToGoogleDistanceMatrix;

    public GoogleDistanceMatrixProducer(GoogleDistanceMatrixEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {

        if(endpoint.getHaversine()){
            filterGeolozalizationsWithHaversine(exchange.getProperty("LatLongOrigin", Map.class),
                                                exchange.getProperty("LatLongDestination", Map.class),
                                                endpoint.getRadius());
        }

        System.out.println(exchange.getIn().getBody());
    }

    private void filterGeolozalizationsWithHaversine(Map<Double, Double> origin, Map<Double, Double> destination, Double radius){
        this.coordinatesToGoogleDistanceMatrix = new HaversineService().getLatLongToSendForGoogleDistanceMatrix(origin, destination, radius);
    }

}
