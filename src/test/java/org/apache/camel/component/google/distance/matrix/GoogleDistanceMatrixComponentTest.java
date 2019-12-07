package org.apache.camel.component.google.distance.matrix;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GoogleDistanceMatrixComponentTest extends CamelTestSupport {

    private Map<Double, Double> origin  = new HashMap<>();
    private Map<Double, Double> destination = new HashMap<>();

   public GoogleDistanceMatrixComponentTest(){
        this.origin.put(-23.493925, -46.631343);

        this.destination.put(-23.501824, -46.623647);
        this.destination.put(-23.515714, -46.626776);
        this.destination.put(-23.501989, -46.848045);
        this.destination.put(-23.499921, -46.623318);
        this.destination.put(-23.499367, -46.848481);
    }

    @Test
    public void testGoogleDistanceMatrix() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("distance-matrix://foo")
                        .process(exchange -> {
                            exchange.setProperty("LatLongOrigin", origin);
                            exchange.setProperty("LatLongDestination", destination);
                        })
                        .to("distance-matrix://bar?option=5&haversine=true&radius=2")
                        .to("mock:result");
            }
        };
    }
}
