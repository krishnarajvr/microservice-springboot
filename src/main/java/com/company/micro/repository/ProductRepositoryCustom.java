package com.company.micro.repository;

import com.company.micro.v1.product.projection.ProductProjection;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    /**
     * Fetch the page of products.
     *
     * @param predicate predicate
     * @param pageable sort, limit and offset
     * @return page of AdminProductProjection
     */
    Page<ProductProjection> getAllProducts(Predicate predicate, Pageable pageable);
}
