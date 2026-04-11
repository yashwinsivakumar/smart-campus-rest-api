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
    public Map<String, Object> bootstrapInfo() {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, String> admin = new LinkedHashMap<>();
        Map<String, String> resources = new LinkedHashMap<>();

        response.put("service", "Smart Campus API");
        response.put("status", "UP");
        response.put("apiVersion", "v1");
        response.put("basePath", "/api/v1");

        admin.put("name", "Campus Facilities API Team");
        admin.put("email", "facilities-api@university.example");
        response.put("adminContact", admin);

        resources.put("self", "/api/v1");
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        response.put("resources", resources);

        return response;
    }
}