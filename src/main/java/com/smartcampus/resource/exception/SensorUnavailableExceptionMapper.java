package com.smartcampus.resource.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("error", "Sensor is unavailable because it is currently in maintenance mode.");
        payload.put("sensorId", exception.getSensorId());
        payload.put("status", exception.getStatus());
        payload.put("reason", "sensor_unavailable");

        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(payload)
                .build();
    }
}