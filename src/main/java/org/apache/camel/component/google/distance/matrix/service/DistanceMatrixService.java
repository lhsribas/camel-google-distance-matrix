package org.apache.camel.component.google.distance.matrix.service;

import org.apache.camel.component.google.distance.matrix.dto.FilterMatrix;

public interface DistanceMatrixService {

    String distanceInfo(final FilterMatrix filterMatrix) throws Exception;
}
