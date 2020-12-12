

package com.company.micro.v1.product.projection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <h1>Admin Product Projection</h1>
 * Admin Product Projection represents the json structure of Product entity.
 */
@Setter
@Getter
@NoArgsConstructor
public class ProductProjection {

    private Long id;

    private Long tenantId;

    private String name;

    private String code;

    private BigDecimal price;

    private LocalDateTime createdTime;
    /**
     * Updated time of the product.
     */
    private LocalDateTime updatedTime;
}
