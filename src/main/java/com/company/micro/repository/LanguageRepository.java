

package com.company.micro.repository;

import com.company.micro.entity.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <h1>Tenant Settings Repository</h1>
 * Repository class for accessing tenant_settings table.
 */
@Repository
public interface LanguageRepository extends CrudRepository<Language, Long>{
    /**
     * Fetch a field from db by tenant id and type.
     *
     * @param key key
     * @param locale    locale
     * @return <code>Form</code> of product
     */
    Language findByMessageKeyAndLocale(String key, String locale);

    /**
     * Fetch a field from db by tenant id and type.
     *
     * @param  tenantId tenantId
     * @param key key
     * @param locale    locale
     * @return <code>Form</code> of product
     */
    Language findByTenantIdAndMessageKeyAndLocale(Long tenantId, String key, String locale);

}


