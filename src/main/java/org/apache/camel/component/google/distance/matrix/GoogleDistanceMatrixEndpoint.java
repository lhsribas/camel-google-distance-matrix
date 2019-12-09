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
    private Integer connectionTimeout = 5000;

    @UriParam(defaultValue = "5000")
    private Integer socketTimeout = 5000;

    @UriParam(defaultValue = "json")
    private String type = "json";

    @UriParam(defaultValue = "driving")
    private String mode = "driving";

    @UriParam(defaultValue = "pt-BR")
    private String language = "pt-BR";

    @UriParam(defaultValue = "false")
    private Boolean sensor = false;

    @UriParam(defaultValue = "metric")
    private String unit = "metric";

    @UriParam
    private String key;


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

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getSensor() {
        return sensor;
    }

    public void setSensor(Boolean sensor) {
        this.sensor = sensor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
