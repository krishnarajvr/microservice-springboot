package com.company.micro.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.company.micro.common.errors.AccessDeniedException;
import com.company.micro.common.errors.ResourceNotFoundException;

class GlobalExceptionHandlerTest {

    @Mock
    private RequestContext requestContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    void testHandleAccessDeniedException() {
        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.FORBIDDEN.name())
                .message("access denied")
                .build();

        final APIResponse<Object> responseData = new APIResponse.Builder<Object>()
                .status(HttpStatus.FORBIDDEN.value())
                .error(errorData)
                .build();

        final ResponseEntity<APIResponse<Object>> expected = ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(responseData);

        Mockito.when(requestContext.getRequestId())
                .thenReturn("testtrace");

        final ResponseEntity<APIResponse<Object>> actual = handler
                .handleAccessDeniedException(new AccessDeniedException("error message"));

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertNull(actual.getBody().getData());
        assertEquals(expected.getBody().getError().getCode(), actual.getBody().getError().getCode());
    }

    @Test
    void testHandleNoHandlerFoundException() {
        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message("invalid route")
                .build();

        final APIResponse<Object> responseData = new APIResponse.Builder<Object>()
                .status(HttpStatus.NOT_FOUND.value())
                .error(errorData)
                .build();

        final ResponseEntity<APIResponse<Object>> expected = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseData);

        final ResponseEntity<APIResponse<Object>> actual = handler
                .handleNoHandlerFoundException(new NoHandlerFoundException("GET", "/", new HttpHeaders()));

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertNull(actual.getBody().getData());
        assertEquals(expected.getBody().getError().getCode(), actual.getBody().getError().getCode());
    }

    @Test
    void testHandleResourceNotFoundException() {
        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.NOT_FOUND.name())
                .message("resource not found")
                .build();

        final APIResponse<Object> responseData = new APIResponse.Builder<Object>()
                .status(HttpStatus.NOT_FOUND.value())
                .error(errorData)
                .build();

        final ResponseEntity<APIResponse<Object>> expected = ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseData);

        final ResponseEntity<APIResponse<Object>> actual = handler
                .handleResourceNotFoundException(new ResourceNotFoundException("resource not found", "test"));

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertNull(actual.getBody().getData());
        assertEquals(expected.getBody().getError().getCode(), actual.getBody().getError().getCode());
    }

    @Test
    void testHandleGeneralException() {
        final ErrorData errorData = new ErrorData.Builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message("internal server error")
                .build();

        final APIResponse<Object> responseData = new APIResponse.Builder<Object>()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(errorData)
                .build();

        final ResponseEntity<APIResponse<Object>> expected = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseData);

        final ResponseEntity<APIResponse<Object>> actual = handler
                .handleGeneralException(new RuntimeException("error"));

        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertNull(actual.getBody().getData());
        assertEquals(expected.getBody().getError().getCode(), actual.getBody().getError().getCode());
    }
}