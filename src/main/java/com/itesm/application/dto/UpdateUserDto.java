package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateUserDto {
    private String name;

    @JsonProperty("last_name_1")
    private String lastName1;

    @JsonProperty("last_name_2")
    private String lastName2;

    private Byte age;

    @JsonProperty("suburb_id")
    private Integer suburbId;
}
