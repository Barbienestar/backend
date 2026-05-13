package com.itesm.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineHospitalStockDto {
    private Integer hospitalId;
    private String hospitalName;
    private String address;
    private String stockLabel;
    private String status;
    private String mapsUrl;
}