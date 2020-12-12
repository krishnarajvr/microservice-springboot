package com.company.micro.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * <h1>Forms</h1>.
 * DB model for form entity
 */
@Entity
@Table(name = "language")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Language {

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private Long tenantId;
    @Column(nullable = false)
    private String messageKey;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String domain;
    @Column(nullable = false)
    private String locale;


    /**
     *  Create method.
     */
    @PrePersist
    public void onCreate() {

    }
}
