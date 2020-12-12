

package com.company.micro.repository;

import com.company.micro.entity.Settings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <h1>Tenant Settings Repository</h1>
 * Repository class for accessing tenant_settings table.
 */
@Repository
public interface SettingsRepository extends CrudRepository<Settings, Long> {
    /**
     * Fetch a settings from db by tenantId.
     * @param tenantId tenant ID
     * @param type type
     * @return <code>type</code> of settings
     */
    Settings findOneByTenantIdAndType(Long tenantId, String type);

    /**
     * Fetch a settings from db by tenantId.
     *
     * @param tenantId tenant ID
     * @return <code>type</code> of settings
     */
    Settings findOneByTenantId(Long tenantId);
}

