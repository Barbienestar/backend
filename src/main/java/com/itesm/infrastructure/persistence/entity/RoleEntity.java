package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public RoleEntity() {}

    public RoleEntity(Byte id) { this.id = id; }

    public Byte getId() { return id; }
    public void setId(Byte id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}