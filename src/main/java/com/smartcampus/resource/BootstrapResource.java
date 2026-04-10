package com.smartcampus.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class BootstrapResource {
    @GET
    public Map<String, String> bootstrapInfo() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("service", "Smart Campus API");
        response.put("status", "UP");
        response.put("basePath", "/api/v1");
        return response;
    }
}