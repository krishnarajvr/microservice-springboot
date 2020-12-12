package com.company.micro.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * <h1>Forms</h1>.
 * DB model for form entity
 */
@Entity
@Table(name = "form")
@Getter @Setter @ToString
public class Form extends Storable {

    @Column(nullable = false)
    private Long entityId;
    @Column(nullable = false, columnDefinition = "json")
    private String fields;
    @Column(nullable = false, columnDefinition = "json")
    private String validations;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code;


    /**
     *  Create method.
     */
    @PrePersist
    public void onCreate() {
        final DateTime datetime = new DateTime(DateTimeZone.UTC);
        this.setCreatedTime(datetime);
        this.setUpdatedTime(datetime);
    }

}
