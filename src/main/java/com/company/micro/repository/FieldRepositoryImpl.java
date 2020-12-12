

package com.company.micro.repository;

import com.company.micro.entity.Field;
import com.company.micro.entity.QField;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

/**
 * <h1>ImportRepositoryImpl</h1>.
 * <p>
 * Implementation class for {@link FieldRepositoryCustom} interface.
 */
public class FieldRepositoryImpl implements FieldRepositoryCustom {

    private static final QField qField = QField.field;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Field> getFieldsByNames(final Set<String> fields) {
        final JPAQuery<?> jpaQuery = new JPAQuery<Void>(em);

        final BooleanExpression whereCondition = qField.field.name.in(fields);

        return jpaQuery
                .from(qField)
                .where(whereCondition)
                .select(qField)
                .fetch();
    }
}
