package com.company.micro.domain.ebook.v1.product;

import com.company.micro.common.ApplicationMode;
import com.company.micro.common.MessageByLocale;
import com.company.micro.common.errors.ResourceNotFoundException;
import com.company.micro.entity.Product;
import com.company.micro.v1.product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("Scholar_ProductService_V1")
public class ProductService extends com.company.micro.v1.product.ProductService {

    @Autowired
    public ProductService(ProductRepo repository, ApplicationMode appMode, MessageByLocale messageByLocale) {
        super(repository, appMode, messageByLocale);
    }

    /**
     * Fetch a product by id.
     *
     * @param tenantId tenant id
     * @param id       identifier
     * @return {@link Product}
     */
    @Transactional
    public Product fetchProductByIdCustom(final Long tenantId, final Long id) {

        final Optional<Product> fetchedProduct = repository.fetchProductById(tenantId, id);

        if (!fetchedProduct.isPresent()) {
            throw new ResourceNotFoundException("product not found", "product");
        }

        return fetchedProduct.get();
    }
}
