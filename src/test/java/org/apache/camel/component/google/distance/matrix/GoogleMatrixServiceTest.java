package org.apache.camel.component.google.distance.matrix;

import org.apache.camel.component.google.distance.matrix.dto.FilterMatrix;
import org.apache.camel.component.google.distance.matrix.service.impl.DistanceMatrixServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GoogleMatrixServiceTest {

    private static Map<Double, Double> origin = new HashMap<>();
    private static Map<Double, Double> destination = new HashMap<>();

    @BeforeClass
    public static void setup() {
        origin.put(-23.493925, -46.631343);

        destination.put(-23.501824, -46.623647);
        destination.put(-23.515714, -46.626776);
        destination.put(-23.501989, -46.848045);
        destination.put(-23.499921, -46.623318);
        destination.put(-23.499367, -46.848481);
    }

    @Test
    public void checkOriginAndDestination() {
        Assert.assertEquals(origin.size(), 1);
        Assert.assertEquals(destination.size(), 5);
    }

    @Test
    public void routerizeLocalizationsWithGoogleDistanceMAtrix() throws Exception {

        Map<Map<Double, Double>, Map<Double, Double>> coordinatesToGoogleDistanceMatrix = new HashMap();
        coordinatesToGoogleDistanceMatrix.put(origin, destination);

        AtomicInteger countPipe = new AtomicInteger();
        StringBuffer bufferOrigin = new StringBuffer();
        StringBuffer bufferDestination = new StringBuffer();

        coordinatesToGoogleDistanceMatrix.forEach((_origin, _destination) -> {

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
        filterMatrix.setMode("drive");
        filterMatrix.setLanguage("pt-BR");
        filterMatrix.setSensor(false);
        filterMatrix.setUnits("metric");
        filterMatrix.setType("json");
        filterMatrix.setConnectionTimeout(500);
        filterMatrix.setSocketTimeout(500);
        filterMatrix.setKey("AIzaSyCT2C5tv8svunS8vr6E_VKDz2wI9nuOhn0");

        final String distance = new DistanceMatrixServiceImpl().distanceInfo(filterMatrix);

        Assert.assertNotNull(distance);
    }
}
