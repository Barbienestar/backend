package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfileDto {
    private Integer id;
    private String name;
    @JsonProperty("last_name_1")
    private String lastName1;
    private Byte role;
    private String email;

    public UserProfileDto() {}

    public UserProfileDto(Integer id, String name, String lastName1, Byte role, String email) {
        this.id = id;
        this.name = name;
        this.lastName1 = lastName1;
        this.role = role;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName1() {
        return lastName1;
    }

    public Byte getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}