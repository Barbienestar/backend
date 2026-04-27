package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserDto {
    private String name;
    @JsonProperty("last_name_1")
    private String lastName1;
    @JsonProperty("last_name_2")
    private String lastName2;
    private Integer age;
    private String email;
    private String password;
    private Byte role;
    @JsonProperty("suburb_id")
    private Integer suburbId;

    public CreateUserDto() {}

    public String getName() {
        return name;
    }

    public String getLastName1() {
        return lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Byte getRole() {
        return role;
    }

    public Integer getSuburbId() {
        return suburbId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Byte role) {
        this.role = role;
    }

    public void setSuburbId(Integer suburbId) {
        this.suburbId = suburbId;
    }
}