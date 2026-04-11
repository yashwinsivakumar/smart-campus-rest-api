package com.smartcampus;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.smartcampus.resource.BootstrapResource;
import com.smartcampus.resource.SensorRoomResource;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(BootstrapResource.class);
        classes.add(SensorRoomResource.class);
        classes.add(JacksonFeature.class);
        return classes;
    }
}