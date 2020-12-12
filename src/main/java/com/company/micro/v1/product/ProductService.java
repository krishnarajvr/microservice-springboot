package com.company.micro.v1.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.company.micro.common.ApplicationMode;
import com.company.micro.common.MessageByLocale;
import com.company.micro.common.errors.ConflictException;
import com.company.micro.common.errors.ResourceNotFoundException;
import com.company.micro.entity.Product;
import com.company.micro.v1.product.projection.ProductProjection;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Contains business logic for product related operations.
 */
@Service("productServiceV1")
public class ProductService {

    /**
     * Repository for products.
     */
    protected ProductRepo repository;

    /**
     * Application Mode.
     */
    protected ApplicationMode appMode;

    /**
     * Message Locale.
     */
    protected MessageByLocale messageByLocale;


    @Autowired
    public ProductService(@Qualifier("productRepositoryV1") final ProductRepo repository,
                          final ApplicationMode appMode,
                          final MessageByLocale messageByLocale
    ) {
        this.repository = repository;
        this.appMode = appMode;
        this.messageByLocale = messageByLocale;
    }

    /**
     * Fetch a product by id.
     *
     * @param tenantId tenant id
     * @param id       identifier
     * @return {@link Product}
     */
    @Transactional
    public Product fetchProductById(final Long tenantId, final Long id) {

        final Optional<Product> fetchedProduct = repository.fetchProductById(tenantId, id);

        if (!fetchedProduct.isPresent()) {
            throw new ResourceNotFoundException("product not found", "product");
        }

        return fetchedProduct.get();
    }


    /**
     * Fetch a product by id.
     *
     * @param tenantId tenant id
     * @param code code
     * @return {@link Product}
     */
    @Transactional
    public Product fetchProductByCode(final Long tenantId, final String code) {

        final Optional<Product> fetchedProduct = repository.fetchProductByCode(tenantId, code);
        return fetchedProduct.get();
    }

    /**
     * Save product.
     *
     * @param tenantId tenant id
     * @param productDto Product
     * @return {@link Product} product
     */
    @Transactional
    public Product saveProduct(final Long tenantId, final JsonNode productDto) {


        final LocalDateTime now = LocalDateTime.now();
        final Product product = Product
                .builder()
                .tenantId(tenantId)
                .code(productDto.path("code").asText())
                .meta(productDto.toString())
                .name(productDto.path("name").asText())
                .price(BigDecimal.valueOf(productDto.path("price").asDouble()))
                .createdTime(now)
                .updatedTime(now)
                .build();

        //Skipping save for non data update mode
        if (!appMode.isDataUpdateMode()) {
            product.setId(1L);

            return product;
        }

        if(repository.fetchProductByCode(tenantId, product.getCode()).isPresent()){
            String[] args = {"ProductCode", product.getCode()};
            throw new ConflictException(messageByLocale.getMessage("error-message.already-exists", args));
        }

        return repository.save(product);
    }


    /**
     * Save product.
     *
     * @param tenantId tenant id
     * @param productDto Product
     * @return {@link Product} product
     */
    @Transactional
    public Product updateProduct(final Long tenantId, final Long productId, final JsonNode productDto) {


        final LocalDateTime now = LocalDateTime.now();
        final Product product = Product
                .builder()
                .id(productId)
                .tenantId(tenantId)
                .code(productDto.path("code").asText())
                .meta(productDto.toString())
                .name(productDto.path("name").asText())
                .price(BigDecimal.valueOf(productDto.path("price").asDouble()))
                .createdTime(now)
                .updatedTime(now)
                .build();

        //Skipping save for non data update mode
        if (!appMode.isDataUpdateMode()) {
            return product;
        }

        return repository.update(productId, product);
    }


    /**
     * Returns paginated list of products for a tenant.
     *
     * @param tenantId tenant id
     * @param predicate predicate for product search
     * @param pageable sort, limit and offset
     * @return page of ProductProjection
     */
    public Page<ProductProjection> getProducts(
            final Long tenantId,
            final Predicate predicate,
            final Pageable pageable) {

        // fetch data from DB
        Page<ProductProjection> adminProductProjections = repository.fetchAdminProducts(predicate, pageable);

        return adminProductProjections;
    }
    

}
