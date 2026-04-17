package com.smartcampus.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.resource.exception.LinkedResourceNotFoundException;
import com.smartcampus.store.InMemoryStore;

@Path("/sensors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(InMemoryStore.sensors().values());
        if (!isBlank(type)) {
            sensors.removeIf(sensor -> sensor.getType() == null || !sensor.getType().equalsIgnoreCase(type));
        }
        sensors.sort(Comparator.comparing(Sensor::getId));
        return sensors;
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null || isBlank(sensor.getId()) || isBlank(sensor.getType())
                || isBlank(sensor.getStatus()) || isBlank(sensor.getRoomId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error("Invalid sensor payload. id, type, status and roomId are required."))
                    .build();
        }

        if (InMemoryStore.sensors().containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(error("Sensor with id '" + sensor.getId() + "' already exists."))
                    .build();
        }

        Room room = InMemoryStore.rooms().get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("room", sensor.getRoomId());
        }

        InMemoryStore.sensors().put(sensor.getId(), sensor);
        if (!room.getSensorIds().contains(sensor.getId())) {
            room.getSensorIds().add(sensor.getId());
        }

        URI location = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("message", "Sensor registered successfully.");
        payload.put("sensor", sensor);

        return Response.created(location).entity(payload).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = InMemoryStore.sensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error("Sensor with id '" + sensorId + "' was not found."))
                    .build();
        }
        return Response.ok(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingsResource(@PathParam("sensorId") String sensorId) {
        if (!InMemoryStore.sensors().containsKey(sensorId)) {
            throw new NotFoundException("Sensor with id '" + sensorId + "' was not found.");
        }
        return new SensorReadingResource(sensorId);
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