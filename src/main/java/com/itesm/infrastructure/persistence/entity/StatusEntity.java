package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "Statuses")
public class StatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;  // tinyint → Byte

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public StatusEntity() {}

    public StatusEntity(Byte id) { this.id = id; }

    public Byte getId() { return id; }
    public void setId(Byte id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}