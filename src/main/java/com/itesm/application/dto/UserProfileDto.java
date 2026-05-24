package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String name;

    @JsonProperty("last_name1")
    private String lastName1;

    @JsonProperty("last_name2")
    private String lastName2;

    private Integer age;

    private SuburbDto suburb;

    private String role;
    private String email;
}
