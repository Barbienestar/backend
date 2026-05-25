package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.StateSupplyDto;
import com.itesm.domain.models.StateSupplyData;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetStateSupplyHeatmapUseCaseTest {

    private MedicinesHospitalsRepository repository;
    private GetStateSupplyHeatmapUseCase useCase;

    @BeforeEach
    void setup() {
        repository = mock(MedicinesHospitalsRepository.class);
        useCase = new GetStateSupplyHeatmapUseCase(repository);
    }

    @Test
    void execute_shouldReturnDtoListForEachState() {
        when(repository.findAvgStockByState())
                .thenReturn(List.of(
                        new StateSupplyData((byte) 1, "Jalisco", 82.5),
                        new StateSupplyData((byte) 2, "Chiapas", 18.0)));

        List<StateSupplyDto> result = useCase.execute();

        assertEquals(2, result.size());
    }

    @Test
    void execute_shouldAssignCA01WhenAvgStockAbove75() {
        when(repository.findAvgStockByState())
                .thenReturn(List.of(new StateSupplyData((byte) 1, "Jalisco", 80.0)));

        StateSupplyDto dto = useCase.execute().get(0);

        assertEquals("CA-01", dto.getLevel());
        assertEquals(80.0, dto.getAvgStock());
        assertEquals("Jalisco", dto.getStateName());
    }

    @Test
    void execute_shouldAssignCA02WhenAvgStockBetween50And74() {
        when(repository.findAvgStockByState())
                .thenReturn(List.of(new StateSupplyData((byte) 2, "Oaxaca", 60.0)));

        StateSupplyDto dto = useCase.execute().get(0);

        assertEquals("CA-02", dto.getLevel());
    }

    @Test
    void execute_shouldAssignCA03WhenAvgStockBetween25And49() {
        when(repository.findAvgStockByState())
                .thenReturn(List.of(new StateSupplyData((byte) 3, "Guerrero", 35.0)));

        StateSupplyDto dto = useCase.execute().get(0);

        assertEquals("CA-03", dto.getLevel());
    }

    @Test
    void execute_shouldAssignCA04WhenAvgStockBelow25() {
        when(repository.findAvgStockByState())
                .thenReturn(List.of(new StateSupplyData((byte) 4, "Chiapas", 10.0)));

        StateSupplyDto dto = useCase.execute().get(0);

        assertEquals("CA-04", dto.getLevel());
    }

    @Test
    void execute_shouldAssignCA04WhenAvgStockIsNull() {
        when(repository.findAvgStockByState())
                .thenReturn(List.of(new StateSupplyData((byte) 5, "Tabasco", null)));

        StateSupplyDto dto = useCase.execute().get(0);

        assertEquals("CA-04", dto.getLevel());
    }

    @Test
    void execute_shouldReturnEmptyListWhenNoData() {
        when(repository.findAvgStockByState()).thenReturn(List.of());

        List<StateSupplyDto> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
