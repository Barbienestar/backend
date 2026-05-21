package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.MedicinesHospitalsStockDto;
import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.repository.MedicinesHospitalsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetStockByMedicineUseCaseTest {

    private MedicinesHospitalsRepository repository;
    private GetStockByMedicineUseCase getStockByMedicineUseCase;

    @BeforeEach
    void setup() {
        repository = mock(MedicinesHospitalsRepository.class);

        MedicinesHospitalsStock stockHigh = buildStock(1, "Hospital Norte", "Calle 1", 100, "https://maps/1");
        MedicinesHospitalsStock stockLow  = buildStock(2, "Hospital Sur",   "Calle 2", 5,   "https://maps/2");
        MedicinesHospitalsStock stockNone = buildStock(3, "Hospital Este",  "Calle 3", 0,   "https://maps/3");

        when(repository.findByMedicineName("Paracetamol"))
                .thenReturn(List.of(stockHigh, stockLow, stockNone));
        when(repository.findByMedicineName("Desconocido"))
                .thenReturn(List.of());

        getStockByMedicineUseCase = new GetStockByMedicineUseCase(repository);
    }

    private MedicinesHospitalsStock buildStock(
            int hospitalId, String name, String address, int stock, String mapsUrl) {
        MedicinesHospitalsStock s = new MedicinesHospitalsStock();
        s.setHospitalId(hospitalId);
        s.setHospitalName(name);
        s.setAddress(address);
        s.setStock(stock);
        s.setMapsUrl(mapsUrl);
        return s;
    }

    @Test
    public void execute_shouldReturnStockListForMedicine() {
        List<MedicinesHospitalsStockDto> result = getStockByMedicineUseCase.execute("Paracetamol");

        assertEquals(3, result.size());
    }

    @Test
    public void execute_shouldReturnEmptyListWhenMedicineNotFound() {
        List<MedicinesHospitalsStockDto> result = getStockByMedicineUseCase.execute("Desconocido");

        assertTrue(result.isEmpty());
    }

    @Test
    public void execute_shouldMapHospitalFieldsCorrectly() {
        List<MedicinesHospitalsStockDto> result = getStockByMedicineUseCase.execute("Paracetamol");

        MedicinesHospitalsStockDto dto = result.get(0);
        assertEquals(1, dto.getHospitalId());
        assertEquals("Hospital Norte", dto.getHospitalName());
        assertEquals("Calle 1", dto.getAddress());
        assertEquals("https://maps/1", dto.getMapsUrl());
    }

    @Test
    public void execute_shouldResolveLabelAndStatusAsStockAltoWhenStockAbove10() {
        List<MedicinesHospitalsStockDto> result = getStockByMedicineUseCase.execute("Paracetamol");

        MedicinesHospitalsStockDto high = result.get(0);
        assertEquals("Stock Alto", high.getStockLabel());
        assertEquals("Disponible", high.getStatus());
    }

    @Test
    public void execute_shouldResolveLabelAndStatusAsLimitadoWhenStockBetween1And10() {
        List<MedicinesHospitalsStockDto> result = getStockByMedicineUseCase.execute("Paracetamol");

        MedicinesHospitalsStockDto low = result.get(1);
        assertEquals("5 piezas restantes", low.getStockLabel());
        assertEquals("Limitado", low.getStatus());
    }

    @Test
    public void execute_shouldResolveLabelAndStatusAsAgotadoWhenStockIsZero() {
        List<MedicinesHospitalsStockDto> result = getStockByMedicineUseCase.execute("Paracetamol");

        MedicinesHospitalsStockDto none = result.get(2);
        assertEquals("No disponible", none.getStockLabel());
        assertEquals("Agotado", none.getStatus());
    }

}