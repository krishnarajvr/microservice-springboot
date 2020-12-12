package com.company.micro.common;

import com.company.micro.common.errors.AccessDeniedException;
import com.company.micro.common.errors.BadRequestException;
import com.company.micro.common.errors.ConflictException;
import com.company.micro.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

/**
 * Common controller for handling API exceptions.
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    /**
     * Logger.
     */
    private static final Logger errorLog = LoggerFactory.getLogger("error-log");

    @Autowired
    private RequestContext requestContext;

    /**
     * Returns HTTP 403 response in case there's no privilege to access an API.
     *
     * @param ex {@link AccessDeniedException}
     * @return 403 response
     */
    @ExceptionHandler
    public ResponseEntity<APIResponse<Object>> handleAccessDeniedException(final AccessDeniedException ex) {
        errorLog.warn("access denied", ex);

        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.FORBIDDEN.name())
                .message("access denied")
                .build();

        final APIResponse<Object> apiResponse = new APIResponse.Builder<Object>()
                .status(HttpStatus.FORBIDDEN.value())
                .error(errorData)
                .traceId(requestContext.getRequestId())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    /**
     * Returns HTTP 404 response in case of invalid request route.
     *
     * @param ex {@link NoHandlerFoundException}
     * @return 404 response
     */
    @ExceptionHandler
    public ResponseEntity<APIResponse<Object>> handleNoHandlerFoundException(
            final NoHandlerFoundException ex
    ) {
        errorLog.warn("handler not found", ex);

        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message("invalid route")
                .build();

        final APIResponse<Object> apiResponse = new APIResponse.Builder<Object>()
                .status(HttpStatus.NOT_FOUND.value())
                .error(errorData)
                .traceId(requestContext.getRequestId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    /**
     * Returns HTTP 404 response in case resource to be fetched is not found.
     *
     * @param ex {@link ResourceNotFoundException}
     * @return 404 response
     */
    @ExceptionHandler
    public ResponseEntity<APIResponse<Object>> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        errorLog.warn("resource not found", ex);

        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.NOT_FOUND.name())
                .target(ex.getResourceName())
                .message(ex.getMessage())
                .build();

        final APIResponse<Object> apiResponse = new APIResponse.Builder<Object>()
                .status(HttpStatus.NOT_FOUND.value())
                .error(errorData)
                .traceId(requestContext.getRequestId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    /**
     * Catch-all mechanism for unexpected errors.
     * Returns HTTP 500 response.
     *
     * @param ex any unhandled {@link Exception}
     * @return 500 response
     */
    @ExceptionHandler
    public ResponseEntity<APIResponse<Object>> handleGeneralException(final Exception ex) {
        errorLog.error("internal server error", ex);

        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message("Something went worong from our side")
                .target("#")
                .build();

        final APIResponse<Object> apiResponse = new APIResponse.Builder<Object>()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(errorData)
                .traceId(requestContext.getRequestId())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    /**
     * Returns HTTP 400 response in case request is not proper.
     *
     * @param badRequestException {@link BadRequestException}
     * @return 400 response
     */
    @ExceptionHandler
    public ResponseEntity<APIResponse> handleBadRequestException(
            final BadRequestException badRequestException) {
        errorLog.warn("{}", keyValue("message",
                "Caught BadRequestException in GlobalExceptionHandler"), badRequestException);

        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.BAD_REQUEST.name())
                .message(badRequestException.getMessage())
                .target(badRequestException.getTarget())
                .path(badRequestException.getPath())
                .details(badRequestException.getErrorDetails())
                .build();

        final APIResponse<Object> apiResponse = new APIResponse.Builder<Object>()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(errorData)
                .traceId(requestContext.getRequestId())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * Returns HTTP 409 response in case resource already exists.
     *
     * @param exception {@link ConflictException}
     * @return 400 response
     */
    @ExceptionHandler
    public ResponseEntity<APIResponse> handleConflictException(ConflictException exception) {
        errorLog.warn("{}", keyValue("message",
                "Caught BadRequestException in GlobalExceptionHandler"), exception);

        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.CONFLICT.name())
                .message(exception.getMessage())
                .build();

        final APIResponse<Object> apiResponse = new APIResponse.Builder<Object>()
                .status(HttpStatus.CONFLICT.value())
                .error(errorData)
                .traceId(requestContext.getRequestId())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }
}
