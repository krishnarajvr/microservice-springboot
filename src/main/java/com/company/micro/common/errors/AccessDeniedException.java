package com.company.micro.common.errors;

/**
 * Thrown when there's no privilege for accessing an API.
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(final String message) {
        super(message);
    }

}
