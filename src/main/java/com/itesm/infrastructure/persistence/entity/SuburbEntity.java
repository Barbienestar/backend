package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Suburbs")
public class SuburbEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    public SuburbEntity() {}

    public SuburbEntity(Integer id) { this.id = id; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}