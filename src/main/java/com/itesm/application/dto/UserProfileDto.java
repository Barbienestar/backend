package com.itesm.application.dto;

import java.util.UUID;

/**
 * UserProfileDto
 */
public class UserProfileDto {
    private UUID id;
    private String name;
    private String lastName1;
    private String role;
    private String email;

    public UserProfileDto() {
    }

    public UserProfileDto(UUID id, String name, String lastName1, String role, String email) {
        this.id = id;
        this.name = name;
        this.lastName1 = lastName1;
        this.role = role;
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
