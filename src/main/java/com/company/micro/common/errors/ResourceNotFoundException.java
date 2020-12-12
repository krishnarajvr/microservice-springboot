package com.company.micro.common.errors;

import lombok.Getter;

/**
 * Thrown when resource is not found.
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Name of the resource which is not found.
     */
    private String resourceName;

    public ResourceNotFoundException(final String message, final String resourceName) {
        super(message);
        this.resourceName = resourceName;
    }

}
