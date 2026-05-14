package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String lastName1;
    private String lastName2;
    private Integer age;
    private String email;
    private String providerUuid;
    private boolean active;
    private Role role;
    private Address address;
    private List<Hospital> hospitals;
}
