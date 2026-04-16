package com.smartcampus.model;

public class SensorReading {
    private String id;
    private double value;
    private String recordedAt;
    private String sensorId;

    public SensorReading() {
    }

    public SensorReading(String id, double value, String recordedAt, String sensorId) {
        this.id = id;
        this.value = value;
        this.recordedAt = recordedAt;
        this.sensorId = sensorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
}
