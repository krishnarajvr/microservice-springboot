

package com.company.micro.repository;

import com.company.micro.entity.Settings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * <h1>Tenant Settings Repository</h1>
 * Repository class for accessing tenant_settings table.
 */
@Repository
public interface FieldRepository extends CrudRepository<Settings, Long>,
        FieldRepositoryCustom {

    public  static final  String FIELD_QUERY_SELECT = "SELECT f1.name, f1.code, f1.meta, f1.definition_name, f1.definition \n";
    public  static final  String FIELD_QUERY_SELECT_PROJECTION = "SELECT f1.name, f1.code, f1.meta \n";

    public static final String FIELD_QUERY =  "FROM field f1 LEFT JOIN field f2\n" +
            " ON (f1.code = f2.code AND f1.level < f2.level) AND \n" +
            "\t((f2.level = 0 and f2.type='CORE' ) OR \n" +
            "\t (f2.level=1 and f2.type=?3) OR \n" +
            "\t (f2.level=2 and f2.entity_id=?1))" +
            "WHERE f2.level IS NULL AND " +
            "((f1.level = 0 and f1.type='CORE' ) OR\n" +
            "\t (f1.level=1 and f1.type=?3) OR\n" +
            "\t (f1.level=2 and f1.entity_id=?1)) \n" +
            " AND f1.code in ?2";

    @Query(value = FIELD_QUERY_SELECT + FIELD_QUERY, nativeQuery = true)
    ArrayList<Map> getFieldsByNames(Long tenantId, Set<String> fields, String domain);

    @Query(value = FIELD_QUERY_SELECT_PROJECTION + FIELD_QUERY, nativeQuery = true)
    ArrayList<Map> getFieldMetaByNames(Long tenantId, Set<String> fields, String domain);
}


