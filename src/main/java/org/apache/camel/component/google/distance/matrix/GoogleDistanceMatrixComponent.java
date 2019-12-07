package org.apache.camel.component.google.distance.matrix;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link GoogleDistanceMatrixEndpoint}.
 */
public class GoogleDistanceMatrixComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new GoogleDistanceMatrixEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
