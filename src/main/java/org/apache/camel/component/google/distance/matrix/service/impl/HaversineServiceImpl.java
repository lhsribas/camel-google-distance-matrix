package org.apache.camel.component.google.distance.matrix.service.impl;

import org.apache.camel.component.google.distance.matrix.service.HaversineService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class HaversineServiceImpl implements HaversineService {

    private static final Double RAIO_TERRA = 6372.8;

    @Override
    public Map<Map<Double, Double>, Map<Double, Double>> getLatLongToSendForGoogleDistanceMatrix(final Map<Double, Double> origin,
                                                                                                 final Map<Double, Double> destination,
                                                                                                 final Double radius) throws Exception {

        Map<Map<Double, Double>, Map<Double, Double>> coordinatesForGoogleDistanceMatrix = new HashMap<>();
        Map<Double, Double> coordinatesInsideRadius = new HashMap<>();

        AtomicReference<Double> latDifference = new AtomicReference<>();
        AtomicReference<Double> longDifference = new AtomicReference<>();

        AtomicReference<Double> latOrigin = new AtomicReference<>();
        AtomicReference<Double> latDestination = new AtomicReference<>();
        AtomicReference<Double> eq = new AtomicReference<>();
        AtomicReference<Double> v = new AtomicReference<>();

        origin.forEach((oLat, oLong) -> {
            latOrigin.set(Math.toRadians(oLat));

            destination.forEach((dLat, dLong) -> {
                latDestination.set(Math.toRadians(dLat));

                latDifference.set(Math.toRadians(oLat - dLat));
                longDifference.set(Math.toRadians(oLong - dLong));

                eq.set(Math.pow(Math.sin(latDifference.get() / 2), 2) + Math.pow(Math.sin(longDifference.get() / 2), 2) * Math.cos(latOrigin.get()) * Math.cos(latDestination.get()));
                eq.set(2 * Math.asin(Math.sqrt(eq.get())));

                v.set(RAIO_TERRA * eq.get());

                if (v.get() <= radius) {
                    coordinatesInsideRadius.put(dLat, dLong);
                }
            });
        });
        coordinatesForGoogleDistanceMatrix.put(origin, coordinatesInsideRadius);

        return coordinatesForGoogleDistanceMatrix;
    }
}
