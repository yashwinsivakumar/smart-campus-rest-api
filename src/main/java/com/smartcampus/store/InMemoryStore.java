package com.smartcampus.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

public final class InMemoryStore {
    private static final Map<String, Room> ROOMS = new ConcurrentHashMap<>();
    private static final Map<String, Sensor> SENSORS = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, SensorReading>> SENSOR_READINGS = new ConcurrentHashMap<>();

    private InMemoryStore() {
    }

    public static Map<String, Room> rooms() {
        return ROOMS;
    }

    public static Map<String, Sensor> sensors() {
        return SENSORS;
    }

    public static Map<String, Map<String, SensorReading>> sensorReadings() {
        return SENSOR_READINGS;
    }
}