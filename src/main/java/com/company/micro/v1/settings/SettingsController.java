package com.company.micro.v1.settings;

import com.company.micro.common.APIResponse;
import com.company.micro.common.MessageByLocale;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.Map;

/**
 * Controller for Common User related APIs (v1).
 */
@RestController("SettingsControllerV1")
@RequestMapping("/v1")
public class SettingsController {

    /**
     * Service methods for Common User.
     */
    private SettingsService service;

    /**
     * Request scoped variables.
     */
    private RequestContext requestContext;

    /**
     * Localized messages.
     */
    private MessageByLocale messageByLocale;

    /**
     * Repository for Common User Operations.
     */
    private SettingsRepo repository;

    @Autowired
    public SettingsController(
            final RequestContext requestContext,
            final SettingsService service,
            final MessageByLocale messageByLocale,
            final SettingsRepo repository) {
        this.messageByLocale = messageByLocale;
        this.requestContext = requestContext;
        this.service = service;
        this.repository = repository;
    }


    /**
     * Method to Get common users of an institution.
     *
     * @param tenantId institutionId
     * @return API Response
     * @throws  IOException exception
     */
    @GetMapping("/settings/{tenantId}")
    public ResponseEntity<APIResponse> getCommonUsers(
            @PathVariable(value = "tenantId") final long tenantId
    ) throws IOException {

        if (!requestContext.hasPublicToken()) {
            throw new AccessDeniedException("access denied");
        }

        final JsonNode settingsData = service.getSettings(tenantId);

        final APIResponse<Map<String, JsonNode>> apiResponse = new APIResponse.Builder<Map<String, JsonNode>>()
                .status(HttpStatus.OK.value())
                .data(Collections.singletonMap("settings", settingsData))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
