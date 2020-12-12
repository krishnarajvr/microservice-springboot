

package com.company.micro.entity;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * <h1>TenantSettings</h1>
 * DB model for tenant_settings entity.
 */
@Entity
@Table(name = "settings")
public class Settings extends Storable {
    /**
     * TenantId.
     */
    @Column(nullable = false)
    private Long tenantId;
    /**
     * Settings.
     */
    @Column(nullable = false, columnDefinition = "json")
    private String settings;

    /**
     * Type.
     */
    @Column(nullable = false)
    private String type;

    /**
     * @return Long tenantId
     */
    public Long getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId tenantId
     */
    public void setTenantId(final Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return settings
     */
    public String getSettings() {
        return settings;
    }

    /**
     * @param settings settings
     */
    public void setSettings(final String settings) {
        this.settings = settings;
    }

    /**
     * @return String type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type String type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     *  create method.
     */
    @PrePersist
    public void onCreate() {
        final DateTime datetime = new DateTime(DateTimeZone.UTC);
        this.setCreatedTime(datetime);
        this.setUpdatedTime(datetime);
    }

}

