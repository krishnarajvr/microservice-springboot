

package com.company.micro.v1.product.query.filter;

import com.company.micro.entity.QProduct;
import com.querydsl.core.BooleanBuilder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * POJO for storing query filters passed in admin product listing API.
 */
@Getter
@Setter
public class ProductFilter {

    /**
     * query filter.
     */
    private String query;

    /**
     * name filter
     */
    private List<String> name;

    /**
     * code filter
     */
    private List<String> code;

    /**
     * id filter
     */
    private List<Long> id;

    /**
     * parentId filter
     */
    private Long parentId;

    /**
     * languageCode filter
     */
    private Set<String> languageCode;

    /**
     * language filter
     */
    private Set<String> language;

    /**
     * isActive filter.
     */
    private Boolean isActive;

    /**
     * isPublished filter.
     */
    private Boolean isPublished;

    /**
     * Generate Boolean Builder for the "where" conditions part passed in to the
     * API as query filters/parameters.
     *
     * @return Boolean builder
     */
    public BooleanBuilder generateBooleanBuilder() {
        final QProduct qProduct = QProduct.product;
        final BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.isNotBlank(query)) {
            booleanBuilder.and(qProduct.name.containsIgnoreCase(query)
                    .or(qProduct.name.containsIgnoreCase(query))
            );
        }

        if (CollectionUtils.isNotEmpty(name)) {
            booleanBuilder.and(qProduct.name.in(name));
        }

        if (CollectionUtils.isNotEmpty(code)) {
            booleanBuilder.and(qProduct.code.in(code));
        }

        if (CollectionUtils.isNotEmpty(id)) {
            booleanBuilder.and(qProduct.id.in(id));
        }

        return booleanBuilder;
    }
}
