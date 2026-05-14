package com.itesm.application.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineStockInputDto {

    @JsonProperty("id_hospital")
    private Integer idHospital;
    private List<MedicineRowDto> rows;
}