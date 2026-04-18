package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * UserEntity
 */
@Entity
@Table(name = "users")
public class UserEntity {
    @Id private UUID id;

    @Column(name = "name") private String name;
    @Column(name = "last_name_1") private String lastName1;
    @Column(name = "last_name_2") private String lastName2;
    @Column(name = "email") private String email;
    @Column(name = "provider_uid") private String providerUid;
    @Column(name = "active") private boolean active;
    @Column(name = "role") private String role;

    public UserEntity() {
    }

    public UserEntity(UUID id, String name, String lastName1, String lastName2, String email,
        String providerUid, boolean active, String role) {
        this.id = id;
        this.name = name;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.email = email;
        this.providerUid = providerUid;
        this.active = active;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName1() {
        return lastName1;
    }
    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }
    public String getLastName2() {
        return lastName2;
    }
    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getProviderUid() {
        return providerUid;
    }
    public void setProviderUid(String providerUid) {
        this.providerUid = providerUid;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
