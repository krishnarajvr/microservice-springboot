

package com.company.micro.repository;

import com.company.micro.entity.Field;

import java.util.List;
import java.util.Set;

/**
 * <h1>ImportRepositoryCustom</h1>.
 */
public interface FieldRepositoryCustom {

    /**
     * Get field by name.
     *
     * @param fields fields
     * @return List of {@link Field}
     */
    List<Field> getFieldsByNames(Set<String> fields);
}
