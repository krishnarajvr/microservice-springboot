

package com.company.micro.common.errors;

import com.company.micro.common.ErrorData;

import java.util.List;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -4367191649866316234L;

    private List<ErrorData> errorDetails;

    private String target;

    private String path;

    public BadRequestException(String message) {
        super(message);
        target = "";
        path = "";
    }

    public BadRequestException(String message, List<ErrorData> errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }

    public BadRequestException(String message, List<ErrorData> errorDetails, String target) {
        super(message);
        this.errorDetails = errorDetails;
        this.target = target;
    }

    public List<ErrorData> getErrorDetails() {
        return errorDetails;
    }

    public String getTarget() {
        return target;
    }

    public String getPath() {
        return path;
    }
}
