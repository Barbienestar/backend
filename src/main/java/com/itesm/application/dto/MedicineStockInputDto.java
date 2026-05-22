package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
