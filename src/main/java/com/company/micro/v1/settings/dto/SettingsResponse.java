package com.company.micro.v1.settings.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response body for LTI.
 */
@Getter
@Setter
@Builder
public class SettingsResponse {

    /**
     * Tenant id.
     */
    private String tenantId;

    /**
     * Settings.
     */
    private ObjectNode settings;

}
