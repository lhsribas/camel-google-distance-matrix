package org.apache.camel.component.google.distance.matrix;

import org.apache.camel.component.google.distance.matrix.dto.FilterMatrix;
import org.apache.camel.component.google.distance.matrix.service.HaversineService;
import org.apache.camel.component.google.distance.matrix.service.impl.DistanceMatrixServiceImpl;
import org.apache.camel.component.google.distance.matrix.service.impl.HaversineServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HaversineAndGoogleDistanceMatrixTest {

    private static Map<Double, Double> origin = new HashMap<>();
    private static Map<Double, Double> destination = new HashMap<>();
    private AtomicReference<Map<Map<Double, Double>, Map<Double, Double>>> coordinatesToGoogleDistanceMatrix = new AtomicReference<>();

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
    public void checkOriginAndDestination(){
        Assert.assertEquals(origin.size(), 1);
        Assert.assertEquals(destination.size(), 5);
    }

    @Test
    public void filterLataLongWithHaverineRadius2Km() throws Exception {

    }

}
