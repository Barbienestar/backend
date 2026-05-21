package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.MedicineDto;
import com.itesm.domain.models.Medicine;
import com.itesm.domain.repository.MedicineRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetMedicinesUseCaseTest {

    private MedicineRepository medicineRepository;
    private GetMedicinesUseCase getMedicinesUseCase;

    @BeforeEach
    void setup() {
        medicineRepository = mock(MedicineRepository.class);

        Medicine m1 = new Medicine();
        m1.setId(1);
        m1.setGenericName("Paracetamol");
        m1.setDosageForm("Tableta");
        m1.setStrength("500mg");
        m1.setPresentation("Caja 20 tabletas");

        Medicine m2 = new Medicine();
        m2.setId(2);
        m2.setGenericName("Ibuprofeno");
        m2.setDosageForm("Cápsula");
        m2.setStrength("400mg");
        m2.setPresentation("Caja 10 cápsulas");

        when(medicineRepository.findAllMedicines()).thenReturn(List.of(m1, m2));
        when(medicineRepository.searchMedicines("Para")).thenReturn(List.of(m1));
        when(medicineRepository.searchMedicines("nonexistent")).thenReturn(List.of());

        getMedicinesUseCase = new GetMedicinesUseCase(medicineRepository);
    }

    @Test
    public void execute_shouldReturnAllMedicines() {
        List<MedicineDto> result = getMedicinesUseCase.execute();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Paracetamol", result.get(0).getGenericName());
        assertEquals("Tableta", result.get(0).getDosageForm());
        assertEquals("500mg", result.get(0).getStrength());
        assertEquals("Caja 20 tabletas", result.get(0).getPresentation());
    }

    @Test
    public void execute_shouldReturnEmptyListWhenNoMedicines() {
        when(medicineRepository.findAllMedicines()).thenReturn(List.of());

        List<MedicineDto> result = getMedicinesUseCase.execute();

        assertTrue(result.isEmpty());
    }

    @Test
    public void search_shouldReturnMatchingMedicines() {
        List<MedicineDto> result = getMedicinesUseCase.search("Para");

        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getGenericName());
    }

    @Test
    public void search_shouldReturnEmptyListWhenNoMatch() {
        List<MedicineDto> result = getMedicinesUseCase.search("nonexistent");

        assertTrue(result.isEmpty());
    }

    @Test
    public void search_shouldMapAllFieldsCorrectly() {
        List<MedicineDto> result = getMedicinesUseCase.search("Para");

        MedicineDto dto = result.get(0);
        assertEquals(1, dto.getId());
        assertEquals("Paracetamol", dto.getGenericName());
        assertEquals("Tableta", dto.getDosageForm());
        assertEquals("500mg", dto.getStrength());
        assertEquals("Caja 20 tabletas", dto.getPresentation());
    }
}