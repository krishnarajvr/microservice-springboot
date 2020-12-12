package com.company.micro.entity;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * <h1>TenantSettings</h1>.
 *
 * DB model for tenant_settings entity
 */
@Entity
@Table(name = "field")
@Getter @Setter @ToString
public class Field extends Storable {
    /**
     * Identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long entity_id;
    @Column(nullable = false, columnDefinition = "json")
    private String meta;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private Byte level;

    /**
     * Create method.
     */
    @PrePersist
    public void onCreate() {
        final DateTime datetime = new DateTime(DateTimeZone.UTC);
        this.setCreatedTime(datetime);
        this.setUpdatedTime(datetime);
    }

}
