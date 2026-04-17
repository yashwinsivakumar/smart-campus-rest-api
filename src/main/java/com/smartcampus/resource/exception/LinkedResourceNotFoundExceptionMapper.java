package com.smartcampus.resource.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("error", "Cannot create sensor because the referenced room does not exist.");
        payload.put("resourceName", exception.getResourceName());
        payload.put("resourceId", exception.getResourceId());
        payload.put("reason", "missing_linked_resource");

        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(payload)
                .build();
    }
}