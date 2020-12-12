package com.company.micro.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores request scoped data.
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter @Setter @NoArgsConstructor
public class RequestContext {

    /**
     * Request ID.
     */
    private String requestId;

    /**
     * Tenant ID.
     */
    private Long tenantId;

    /**
     * User ID.
     */
    private Long userId;

    /**
     * JWT token.
     */
    private String jwtToken;

    /**
     * Domain.
     */
    private String domain;

    /**
     * @return True if header contains tenant id.
     */
    public Boolean hasPublicToken() {
        return this.tenantId != null;
    }

}
