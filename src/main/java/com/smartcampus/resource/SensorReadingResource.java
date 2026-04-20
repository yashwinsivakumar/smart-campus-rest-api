package com.smartcampus.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.resource.exception.SensorUnavailableException;
import com.smartcampus.store.InMemoryStore;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getAllReadings() {
        Map<String, SensorReading> readingsById = InMemoryStore.sensorReadings()
                .getOrDefault(sensorId, new ConcurrentHashMap<>());

        List<SensorReading> readings = new ArrayList<>(readingsById.values());
        readings.sort(Comparator.comparingLong(SensorReading::getTimestamp));
        return readings;
    }

    @POST
    public Response createReading(SensorReading reading, @Context UriInfo uriInfo) {
        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error("Invalid reading payload."))
                    .build();
        }

        Sensor sensor = InMemoryStore.sensors().get(sensorId);
        if (sensor != null && "MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId, sensor.getStatus());
        }

        String readingId = isBlank(reading.getId())
                ? "R-" + UUID.randomUUID().toString().substring(0, 8)
                : reading.getId();

        Map<String, SensorReading> readingsById = InMemoryStore.sensorReadings()
                .computeIfAbsent(sensorId, key -> new ConcurrentHashMap<>());

        if (readingsById.containsKey(readingId)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(error("Reading with id '" + readingId + "' already exists for sensor '" + sensorId + "'."))
                    .build();
        }

        SensorReading toStore = new SensorReading();
        toStore.setId(readingId);
        toStore.setTimestamp(reading.getTimestamp() <= 0 ? System.currentTimeMillis() : reading.getTimestamp());
        toStore.setValue(reading.getValue());

        readingsById.put(readingId, toStore);

        if (sensor != null) {
            sensor.setCurrentValue(toStore.getValue());
        }

        URI location = uriInfo.getAbsolutePathBuilder().path(readingId).build();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("message", "Reading recorded successfully.");
        payload.put("reading", toStore);

        return Response.created(location).entity(payload).build();
    }

    @GET
    @Path("/{readingId}")
    public Response getReadingById(@PathParam("readingId") String readingId) {
        SensorReading reading = InMemoryStore.sensorReadings()
                .getOrDefault(sensorId, new ConcurrentHashMap<>())
                .get(readingId);

        if (reading == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error("Reading with id '" + readingId + "' was not found for sensor '" + sensorId + "'."))
                    .build();
        }

        return Response.ok(reading).build();
    }

    private static Map<String, String> error(String message) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("error", message);
        return response;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
