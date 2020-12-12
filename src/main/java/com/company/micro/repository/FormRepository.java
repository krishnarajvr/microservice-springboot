

package com.company.micro.repository;

import com.company.micro.entity.Form;
import com.company.micro.v1.form.projection.FormProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <h1>Tenant Settings Repository</h1>
 * Repository class for accessing tenant_settings table.
 */
@Repository
public interface FormRepository extends CrudRepository<Form, Long> {

    @Query(value = "SELECT f1.name, f1.code, f1.fields, f1.validations \n" +
            "FROM form f1 LEFT JOIN form f2\n" +
            " ON (f1.code = f2.code AND f1.level < f2.level) AND \n" +
            "\t((f2.level = 0 and f2.type='CORE' ) OR \n" +
            "\t (f2.level=1 and f2.type=?2) OR \n" +
            "\t (f2.level=2 and f2.entity_id=?1))" +
            "WHERE f2.level IS NULL AND " +
            "((f1.level = 0 and f1.type='CORE' ) OR\n" +
            "\t (f1.level=1 and f1.type=?2) OR\n" +
            "\t (f1.level=2 and f1.entity_id=?1)) \n" +
            " AND f1.code = ?3 " +
            " limit 0,1", nativeQuery = true)
    FormProjection getFormByName(Long tenantId, String domain, String name);



}
