package com.company.micro.v1.product;

import java.util.Optional;

import com.company.micro.entity.Product;
import com.company.micro.repository.ProductRepository;
import com.company.micro.v1.product.projection.ProductProjection;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Repository for product related operations (V1).
 */
@Repository("productRepositoryV1")
public class ProductRepo {

    /**
     * Repository provided by JPA containing general operations.
     */
    private ProductRepository productRepository;

    @Autowired
    public ProductRepo(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Fetch a product from its tenant id and primary key.
     * @param tenantId tenant ID
     * @param id primary key
     * @return <code>Optional</code> of product
     */
    public Optional<Product> fetchProductById(final Long tenantId, final Long id) {
        return productRepository.findByTenantIdAndId(tenantId, id);
    }

    /**
     * Fetch a product from its tenant id and primary key.
     * @param tenantId tenant ID
     * @param code product code
     * @return <code>Optional</code> of product
     */
    public Optional<Product> fetchProductByCode(final Long tenantId, final String code) {
        return productRepository.findByTenantIdAndCode(tenantId, code);
    }

    /**
     * Save Product.
     *
     * @param product Product
     * @return product Product
     */
    public Product save(final Product product) {
        return productRepository.save(product);
    }


    /**
     * Save Product.
     *
     * @param product Product
     * @return product Product
     */
    public Product update(final Long id, final Product product) {
        Product pdb = productRepository.findById(id).get();
        pdb.setName(product.getName());
        pdb.setMeta(product.getMeta());
        pdb.setPrice(product.getPrice());
        return productRepository.save(pdb);
    }

    /**
     * @param predicate predicate for product search
     * @param pageable sort, limit and offset
     * @return page of AdminProductProjection
     */
    public Page<ProductProjection> fetchAdminProducts(
            final Predicate predicate,
            final Pageable pageable) {

        return productRepository.getAllProducts(predicate, pageable);
    }
}
