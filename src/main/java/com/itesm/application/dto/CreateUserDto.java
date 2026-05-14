package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateUserDto {
    private String name;

    @JsonProperty("last_name_1")
    private String lastName1;

    @JsonProperty("last_name_2")
    private String lastName2;

    private Integer age;
    private String email;
    private String password;

    @JsonProperty("role_id")
    private Byte roleId;

    @JsonProperty("suburb_id")
    private Integer suburbId;

    @JsonProperty("hospital_ids")
    private List<Integer> hospitalIds;
}
