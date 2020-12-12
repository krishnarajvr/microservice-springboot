package com.company.micro.common.errors;

import java.util.List;

public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = -3415290940263416214L;

    private List<Error> details;

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, List<Error> details) {
        super(message);
        this.details = details;
    }

    public List<Error> getDetails() {
        return details;
    }

}
