package com.itesm.domain.repository;

import java.util.List;
import com.itesm.domain.models.MedicineHospital;

public interface MedicineHospitalRepository {
    void save(MedicineHospital medicineHospital);
    void saveAll(List<MedicineHospital> records);
}
