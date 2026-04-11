package com.smartcampus.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.smartcampus.model.Room;
import com.smartcampus.store.InMemoryStore;

@Path("/rooms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SensorRoomResource {

    @GET
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>(InMemoryStore.rooms().values());
        rooms.sort(Comparator.comparing(Room::getId));
        return rooms;
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null || isBlank(room.getId()) || isBlank(room.getName()) || room.getCapacity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error("Invalid room payload. id, name and positive capacity are required."))
                    .build();
        }

        if (InMemoryStore.rooms().containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(error("Room with id '" + room.getId() + "' already exists."))
                    .build();
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        InMemoryStore.rooms().put(room.getId(), room);

        URI location = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("message", "Room created successfully.");
        payload.put("room", room);

        return Response.created(location).entity(payload).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = InMemoryStore.rooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error("Room with id '" + roomId + "' was not found."))
                    .build();
        }

        return Response.ok(room).build();
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