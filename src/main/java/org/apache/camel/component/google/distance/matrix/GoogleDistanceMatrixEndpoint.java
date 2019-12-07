package org.apache.camel.component.google.distance.matrix;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a GoogleDistanceMatrix endpoint.
 */
@UriEndpoint(firstVersion = "1.0-SNAPSHOT", scheme = "distance-matrix", title = "GoogleDistanceMatrix", syntax = "distance-matrix:name",
        consumerClass = GoogleDistanceMatrixConsumer.class, label = "custom")
public class GoogleDistanceMatrixEndpoint extends DefaultEndpoint {

    @UriPath
    @Metadata(required = "true")
    private String name;

    @UriParam(defaultValue = "10")
    private int option = 10;

    @UriParam(defaultValue = "true")
    private Boolean haversine = true;

    @UriParam(defaultValue = "1")
    private Double radius = 1d;

    @UriParam(defaultValue = "5000")
    private Long timeout = 5000L;

    public GoogleDistanceMatrixEndpoint() {
    }

    public GoogleDistanceMatrixEndpoint(String uri, GoogleDistanceMatrixComponent component) {
        super(uri, component);
    }

    public GoogleDistanceMatrixEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new GoogleDistanceMatrixProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new GoogleDistanceMatrixConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }

    public Boolean getHaversine() {
        return haversine;
    }

    public void setHaversine(Boolean haversine) {
        this.haversine = haversine;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
