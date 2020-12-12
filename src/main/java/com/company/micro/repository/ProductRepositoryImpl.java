package com.company.micro.repository;

import com.company.micro.v1.product.projection.ProductProjection;
import com.company.micro.v1.product.query.sort.ProductSort;
import com.company.micro.entity.QProduct;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static final QProduct qProduct = QProduct.product;

    @Autowired
    private ProductSort productSort;

    /**
     * Fetch the page of products.
     *
     * @param predicate predicate
     * @param pageable sort, limit and offset
     * @return page of ProductProjection
     */
    @Override
    public Page<ProductProjection> getAllProducts(
            final Predicate predicate,
            final Pageable pageable) {
        final JPAQuery<?> jpaQuery = new JPAQuery<Void>(em);
        final List<OrderSpecifier> orderSpecifiers = productSort.buildOrderSpecifiers(pageable.getSort());

        final QBean<ProductProjection> productProjectionQBean = Projections.bean(ProductProjection.class,
                qProduct.id.as("id"),
                qProduct.id.as("productId"),
                qProduct.tenantId.as("tenantId"),
                qProduct.name.as("name"),
                qProduct.code.as("code"),
                qProduct.price.as("price"),
                qProduct.createdTime.as("createdTime"),
                qProduct.updatedTime.as("updatedTime"));

        final QueryResults<ProductProjection> productQueryResults = jpaQuery
                .from(qProduct)
                .where(predicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .select(productProjectionQBean)
                .fetchResults();

        return new PageImpl<ProductProjection>(productQueryResults.getResults(), pageable,
                productQueryResults.getTotal());
    }
    
}
