package com.smartcampus.resource.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("error", "Room cannot be deleted because it is occupied by active hardware.");
        payload.put("roomId", exception.getRoomId());
        payload.put("sensorCount", exception.getSensorCount());
    payload.put("reason", "occupied_by_active_hardware");

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(payload)
                .build();
    }
}