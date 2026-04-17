package com.smartcampus.resource.exception;

public class LinkedResourceNotFoundException extends RuntimeException {
    private final String resourceName;
    private final String resourceId;

    public LinkedResourceNotFoundException(String resourceName, String resourceId) {
        super("Referenced " + resourceName + " '" + resourceId + "' does not exist.");
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceId() {
        return resourceId;
    }
}