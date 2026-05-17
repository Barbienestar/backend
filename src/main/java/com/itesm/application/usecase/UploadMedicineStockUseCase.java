package com.itesm.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.itesm.application.dto.MedicineRowDto;
import com.itesm.application.dto.MedicineStockInputDto;
import com.itesm.application.dto.MedicineStockResultDto;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Medicine;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import com.itesm.domain.repository.MedicineRepository;

@ApplicationScoped
public class UploadMedicineStockUseCase {

    @Inject
    MedicineRepository medicineRepository;

    @Inject
    MedicinesHospitalsRepository medicinesHospitalsRepository;

    @Inject
    HospitalRepository hospitalRepository;

    public MedicineStockResultDto execute(MedicineStockInputDto input) {

        int inserted = 0;
        List<String> errors = new ArrayList<>();

        Hospital hospital = hospitalRepository.findHospitalById(input.getIdHospital());
        if (hospital == null) {
            throw new RuntimeException("Hospital no encontrado");
        }

        List<String> genericNames = input.getRows().stream()
                .map(MedicineRowDto::getGenericName)
                .toList();

        List<Medicine> existingMedicines = medicineRepository.findByNames(genericNames);

        List<Medicine> medicinesToSave = new ArrayList<>();

        List<MedicinesHospitals> relationsToSave = new ArrayList<>();

        for (MedicineRowDto row : input.getRows()) {
            try {
                Medicine medicine = existingMedicines.stream()
                        .filter(m -> m.getGenericName().equalsIgnoreCase(row.getGenericName()))
                        .findFirst()
                        .orElseGet(() -> {
                            Medicine newMedicine = new Medicine(
                                    row.getGenericName(),
                                    row.getDosageForm(),
                                    row.getStrength(),
                                    row.getPresentation()
                            );
                            medicinesToSave.add(newMedicine);
                            return newMedicine;
                        });


                relationsToSave.add(new MedicinesHospitals(medicine, hospital, row.getStock(), LocalDateTime.now()));
                inserted++;

            } catch (Exception e) {
                errors.add("Fila con medicamento '" + row.getGenericName() + "': " + e.getMessage());
            }
        }

        List<Medicine> savedMedicines = medicineRepository.saveAll(medicinesToSave);
            
        for (Medicine saved : savedMedicines) {

            for (MedicinesHospitals relation : relationsToSave) {
                if (relation.getMedicine().getGenericName().equalsIgnoreCase(saved.getGenericName())) {
                    relation.getMedicine().setId(saved.getId());
                }
            }
        }
        
        medicinesHospitalsRepository.saveAll(relationsToSave);

        return new MedicineStockResultDto(inserted, errors);
    }
}
