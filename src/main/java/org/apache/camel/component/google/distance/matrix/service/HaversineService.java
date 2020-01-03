package org.apache.camel.component.google.distance.matrix.service;

import java.util.Map;

public interface HaversineService {

    Map<Map<Double, Double>, Map<Double, Double>> getLatLongToSendForGoogleDistanceMatrix(final Map<Double, Double> origin,
                                                                                          final Map<Double, Double> destination,
                                                                                          final Double radius,
                                                                                          final String unit) throws Exception;
}
