package com.company.micro.v1.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.company.micro.common.APIResponse;
import com.company.micro.common.RequestContext;
import com.company.micro.common.errors.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for Common User related APIs (v1).
 */
@RestController("FormControllerV1")
@RequestMapping("/v1")
public class FormController {

    /**
     * Request scoped data (use only at the controller level).
     */
    private RequestContext requestContext;

    /**
     * Service methods for Common User.
     */
    private FormService service;
    /**
     * Repository for Common User Operations.
     */
    private FormRepo repository;

    @Autowired
    public FormController(
            final RequestContext requestContext,
            final FormService service,
            final FormRepo repository) {
        this.service = service;
        this.repository = repository;
        this.requestContext = requestContext;
    }


    /**
     * Method to Get common users of an institution.
     * @param name institutionId
     * @return API Response
     * @throws IOException IO exception
     */
    @GetMapping("/forms/{name}")
    public ResponseEntity<APIResponse> getFormByName(
            @PathVariable(value = "name") final String name
    ) throws IOException {

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }

        final JsonNode responseData = service.getForms(requestContext.getTenantId(), requestContext.getDomain(), name);

        final APIResponse<Map<String, JsonNode>> apiResponse = new APIResponse.Builder<Map<String, JsonNode>>()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonMap("forms", responseData))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Method to Get common users of an institution.
     * @param name form name
     * @return API Response
     * @throws IOException IO exception
     */
    @GetMapping("/forms/{name}/validate")
    public ResponseEntity<APIResponse> validateForm(
            @PathVariable(value = "name") final String name
    ) throws IOException {

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }
        final JsonNode responseData = service.validateForm(requestContext.getTenantId(), requestContext.getDomain(), name);

        final APIResponse<Map<String, JsonNode>> apiResponse = new APIResponse.Builder<Map<String, JsonNode>>()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonMap("forms", responseData))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Method to Get fields of a form.
     * @param name form name
     * @return API Response
     * @throws IOException IO exception
     */
    @GetMapping("/forms/{name}/fields")
    public ResponseEntity<APIResponse> getFormFields(
            @PathVariable(value = "name") final String name
    ) throws IOException {

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }
        final HashMap<String, JsonNode> responseData = service.getFields(requestContext.getTenantId(), requestContext.getDomain(), name);

        final APIResponse<Map<String, HashMap<String, JsonNode>>> apiResponse = new APIResponse.Builder<Map<String, HashMap<String, JsonNode>>>()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonMap("fields", responseData))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
