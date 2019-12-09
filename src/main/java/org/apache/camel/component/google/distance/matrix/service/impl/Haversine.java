package org.apache.camel.component.google.distance.matrix.service.impl;

import org.apache.camel.component.google.distance.matrix.service.HaversineService;

import java.util.HashMap;
import java.util.Map;

public class Haversine implements HaversineService {

    private static final Double RAIO_TERRA = 6372.8;

    @Override
    public Map<Map<Double, Double>, Map<Double, Double>> getLatLongToSendForGoogleDistanceMatrix(final Map<Double, Double> origin,
                                                                                                 final Map<Double, Double> destination,
                                                                                                 final Double radius) {

        Map<Map<Double, Double>, Map<Double, Double>> coordinatesForGoogleDistanceMatrix = new HashMap<>();
        Map<Double, Double> coordinatesInsideRadius = new HashMap<>();

        Double latDifference;
        Double longDifference;

        Double latOrigin;
        Double latDestination;
        Double eq;
        Double v;

        for (Map.Entry<Double, Double> entryOrigin : origin.entrySet()) {

            latOrigin = Math.toRadians(entryOrigin.getKey());

            for (Map.Entry<Double, Double> entryDestination : destination.entrySet()) {

                latDestination = Math.toRadians(entryDestination.getKey());

                latDifference = Math.toRadians(entryOrigin.getKey() - entryDestination.getKey());
                longDifference = Math.toRadians(entryOrigin.getValue() - entryDestination.getValue());

                eq = Math.pow(Math.sin(latDifference / 2), 2) + Math.pow(Math.sin(longDifference / 2), 2) * Math.cos(latOrigin) * Math.cos(latDestination);
                eq = 2 * Math.asin(Math.sqrt(eq));

                v = RAIO_TERRA * eq;

                if (v <= radius) {
                    coordinatesInsideRadius.put(entryDestination.getKey(), entryDestination.getValue());
                }
            }
        }

        coordinatesForGoogleDistanceMatrix.put(origin, coordinatesInsideRadius);

        return coordinatesForGoogleDistanceMatrix;
    }
}
