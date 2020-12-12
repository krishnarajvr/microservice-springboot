package com.company.micro.v1.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.company.micro.entity.Product;
import com.company.micro.v1.product.projection.ProductProjection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * JSON representation of product entity to be used in request and response.
 */
@Getter
@Setter
@NoArgsConstructor
@Qualifier("ProductDtoV1")
public class ProductDto {

    /**
     * Product ID.
     */
    private Long id;

    /**
     * Product code.
     */
    private String code;

    /**
     * Product name.
     */
    private String name;

    private JsonNode meta;

    /**
     * Product Price.
     */
    private BigDecimal price;

    /**
     * Product name.
     */
    private List<Object> authors;

    /**
     * Created time of the product.
     */
    private LocalDateTime createdTime;

    /**
     * Updated time of the product.
     */
    private LocalDateTime updatedTime;

    @SneakyThrows
    public ProductDto(final Product entity)  {
        this.id = entity.getId();
        this.name = entity.getName();
        this.code = entity.getCode();
        this.price = entity.getPrice();
        ObjectMapper mapper = new ObjectMapper();
        this.meta = mapper.readTree(entity.getMeta());
        this.createdTime = entity.getCreatedTime();
        this.updatedTime = entity.getUpdatedTime();
        this.authors = new ArrayList<Object>();
    }

    public ProductDto(ProductProjection productProjection)  {
        this.id = productProjection.getId();
        this.name = productProjection.getName();
        this.code = productProjection.getCode();
        this.price = productProjection.getPrice();
        this.createdTime = productProjection.getCreatedTime();
        this.updatedTime = productProjection.getUpdatedTime();
    }
}
