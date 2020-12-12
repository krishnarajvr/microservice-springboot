package com.company.micro.domain.ebook.customer1.v1.product.dto;

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
@Qualifier("WileyScholarProductDtoV1")
public class ProductDto extends com.company.micro.v1.product.dto.ProductDto {


    /**
     * custom attribute of wiley product.
     */
    private String wileyCustomAttribute;

    public ProductDto(final Product entity) {
        super(entity);
        this.wileyCustomAttribute = "Wiley custom attribure 1";
    }

    public ProductDto(ProductProjection productProjection) {
        super(productProjection);
        this.wileyCustomAttribute = "Wiley custom attribure 1";
    }
}
