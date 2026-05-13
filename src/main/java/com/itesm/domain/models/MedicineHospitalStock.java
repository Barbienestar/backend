package com.itesm.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineHospitalStock {
    private Integer hospitalId;
    private String hospitalName;
    private String address;
    private Integer stock;
    private String mapsUrl;
}