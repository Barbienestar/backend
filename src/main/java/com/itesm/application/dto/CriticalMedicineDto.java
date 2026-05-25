package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriticalMedicineDto {
    private Integer id;

    @JsonProperty("generic_name")
    private String genericName;

    private int stock;
}
