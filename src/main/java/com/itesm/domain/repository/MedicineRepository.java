package com.itesm.domain.repository;

import com.itesm.domain.models.Medicine;
import java.util.List;

public interface MedicineRepository {
    List<Medicine> findAllMedicines();
    Medicine findMedicineById(Integer id);
   List<Medicine> findByNames(List<String> genericNames);
   List<Medicine> saveAll(List<Medicine> medicines);
    List<Medicine> searchMedicines(String query);
}