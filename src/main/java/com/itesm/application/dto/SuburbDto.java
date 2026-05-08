package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuburbDto {
    private Integer id;
    private String name;
    @JsonProperty("zip_code")
    private String zipCode;
}
