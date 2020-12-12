package com.company.micro.domain.ebook.v1.product.dto;

import com.company.micro.entity.Product;
import com.company.micro.v1.product.projection.ProductProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * JSON representation of product entity to be used in request and response.
 */
@Getter @Setter @NoArgsConstructor
@Qualifier("Scholar_ProductDto_V1")
public class ProductDto extends com.company.micro.v1.product.dto.ProductDto {

    /**
     * Updated time of the product.
     */
    protected String customAttribute;

    public ProductDto(final Product entity, final String customValue) {
        super(entity);
        this.customAttribute = customValue;
    }

    public ProductDto(ProductProjection productProjection,final String customValue) {
        super(productProjection);
        this.customAttribute = customValue;
    }
}
