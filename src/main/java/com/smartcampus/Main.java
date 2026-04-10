package com.smartcampus;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
    private static final URI BASE_URI = URI.create("http://0.0.0.0:8080/api/v1/");

    private Main() {
    }

    public static HttpServer startServer() {
        ResourceConfig resourceConfig = ResourceConfig.forApplication(new SmartCampusApplication());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer server = startServer();
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        System.out.println("Smart Campus API started at http://localhost:8080/api/v1");
        Thread.currentThread().join();
    }
}