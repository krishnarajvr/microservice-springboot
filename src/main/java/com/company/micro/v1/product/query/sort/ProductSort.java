package com.company.micro.v1.product.query.sort;

import com.company.micro.entity.QProduct;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ProductSort {

    private static final Map<String, Path> queryDslParamsCache = new HashMap<>();
    private static final QProduct qProduct = QProduct.product;

    /**
     * Build Product sort query based on the sort parameters provided in the request URL.
     *
     * @param sortParams sort params in request URL
     * @return Product sort query
     */
    public List<OrderSpecifier> buildOrderSpecifiers(final Sort sortParams) {
        if (sortParams == null) {
            return Collections.singletonList(qProduct.updatedTime.desc());
        }

        final List<OrderSpecifier> sorts = new ArrayList<>();

        for (final Sort.Order sort : sortParams) {
            final Path path = queryDslParamsCache.get(sort.getProperty());

            if (Objects.isNull(path)) {
                continue;
            }

            if (sort.isAscending()) {
                sorts.add(new OrderSpecifier<>(Order.ASC, path));
            } else {
                sorts.add(new OrderSpecifier<>(Order.DESC, path));
            }
        }

        return sorts;
    }

}
