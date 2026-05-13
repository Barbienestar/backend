package com.itesm.domain.repository;

import com.itesm.domain.models.MedicineHospitalStock;
import java.util.List;

public interface MedicineHospitalStockRepository {
    List<MedicineHospitalStock> findByMedicineName(String medicineName);
}