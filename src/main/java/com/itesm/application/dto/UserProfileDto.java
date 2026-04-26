package com.itesm.application.dto;

public class UserProfileDto {
    private Integer id;
    private String name;
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName1() { return lastName1; }
    public void setLastName1(String lastName1) { this.lastName1 = lastName1; }

    public Byte getRole() { return role; }
    public void setRole(Byte role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}