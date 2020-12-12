package com.company.micro.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product represents any type of digital content in Micro.
 */
@Entity
@Table(name = "product")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Product {

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tenant id.
     */
    private Long tenantId;

    /**
     * Product code.
     */
    private String code;

    /**
     * Product name.
     */
    private String name;

    /**
     * Product price.
     */
    private BigDecimal price;

    /**
     * Product meta.
     */
    @Column(nullable = false, columnDefinition = "json")
    private String meta;

    /**
     * Created time of product.
     */
    private LocalDateTime createdTime;

    /**
     * Last updated time.
     */
    private LocalDateTime updatedTime;

}
