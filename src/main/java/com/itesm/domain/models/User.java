package com.itesm.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private Integer id;
    private String name;
    private String lastName1;
    private String lastName2;
    private Integer age;
    private String email;
    private String providerUuid;
    private boolean active;
    private Byte role;
    private Integer suburbId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName1() { return lastName1; }
    public void setLastName1(String lastName1) { this.lastName1 = lastName1; }

    public String getLastName2() { return lastName2; }
    public void setLastName2(String lastName2) { this.lastName2 = lastName2; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProviderUuid() { return providerUuid; }
    public void setProviderUuid(String providerUuid) { this.providerUuid = providerUuid; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Byte getRole() { return role; }
    public void setRole(Byte role) { this.role = role; }

    public Integer getSuburbId() { return suburbId; }
    public void setSuburbId(Integer suburbId) { this.suburbId = suburbId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}