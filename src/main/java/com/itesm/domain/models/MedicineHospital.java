package com.itesm.domain.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineHospital {
    private Integer id;
    private Medicine medicine;
    private Hospital hospital;
    private int stock;
    private LocalDateTime entryDate;

    public MedicineHospital(Medicine medicine, Hospital hospital, int stock, LocalDateTime entryDate) {
        this.medicine = medicine;
        this.hospital = hospital;
        this.stock = stock;
        this.entryDate = entryDate;
    }
}
