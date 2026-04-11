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
        payload.put("error", "Room cannot be deleted because it still has assigned sensors.");
        payload.put("roomId", exception.getRoomId());
        payload.put("sensorCount", exception.getSensorCount());

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(payload)
                .build();
    }
}