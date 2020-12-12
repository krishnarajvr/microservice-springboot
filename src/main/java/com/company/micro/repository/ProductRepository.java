package com.company.micro.repository;

import java.util.Optional;

import com.company.micro.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Product repository containing basic CRUD methods.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> ,
        ProductRepositoryCustom {

    /**
     * Fetch a product from DB by its tenant id and primary key.
     * @param tenantId tenant ID
     * @param id primary key
     * @return <code>Optional</code> of product
     */
    Optional<Product> findByTenantIdAndId(Long tenantId, Long id);

    /**
     * Fetch a product from DB by its tenant id and product code.
     * @param tenantId tenant ID
     * @param code product code
     * @return <code>Optional</code> of product
     */
    Optional<Product> findByTenantIdAndCode(Long tenantId, String code);

    /**
     * Check product exists  by its tenant id and product code.
     * @param tenantId tenant ID
     * @param code product code
     * @return <code>Optional</code> of product
     */
    Optional<Product> existsByTenantIdAndCode(Long tenantId, String code);

}
