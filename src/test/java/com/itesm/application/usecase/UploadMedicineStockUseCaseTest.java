package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.MedicineRowDto;
import com.itesm.application.dto.MedicineStockInputDto;
import com.itesm.application.dto.MedicineStockResultDto;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Medicine;
import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.repository.MedicineRepository;
import com.itesm.domain.repository.MedicinesHospitalsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

/** UploadMedicineStockUseCaseTest */
public class UploadMedicineStockUseCaseTest {

    private MedicineRepository medicineRepository;
    private MedicinesHospitalsRepository medicinesHospitalsRepository;
    private HospitalRepository hospitalRepository;
    private UploadMedicineStockUseCase uploadMedicineStockUseCase;

    private Hospital hospital;

    @BeforeEach
    void setup() {
        medicineRepository = mock(MedicineRepository.class);
        medicinesHospitalsRepository = mock(MedicinesHospitalsRepository.class);
        hospitalRepository = mock(HospitalRepository.class);

        hospital = new Hospital(1, "Hospital General", "https://maps/link");
        when(hospitalRepository.findHospitalById(1)).thenReturn(hospital);
        when(medicineRepository.findByNames(anyList())).thenReturn(List.of());
        when(medicineRepository.saveAll(anyList())).thenReturn(List.of());

        uploadMedicineStockUseCase = new UploadMedicineStockUseCase(
                medicineRepository, medicinesHospitalsRepository, hospitalRepository);
    }

    // Happy path: todas las filas son medicamentos nuevos, deben insertarse y guardarse las relaciones
    @Test
    public void execute_shouldInsertNewMedicinesAndReturnCorrectCount() {
        MedicineRowDto row1 = new MedicineRowDto("Paracetamol", "Tableta", "500mg", "Caja 20", 100);
        MedicineRowDto row2 = new MedicineRowDto("Ibuprofeno", "Cápsula", "400mg", "Caja 30", 50);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(row1, row2));

        Medicine savedParacetamol = new Medicine(10, "Paracetamol", "Tableta", "500mg", "Caja 20");
        Medicine savedIbuprofeno = new Medicine(11, "Ibuprofeno", "Cápsula", "400mg", "Caja 30");
        when(medicineRepository.saveAll(anyList())).thenReturn(List.of(savedParacetamol, savedIbuprofeno));

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(2, result.getInserted());
        assertTrue(result.getErrors().isEmpty());
    }

    // Happy path: los medicamentos ya existen en la BD, no se crean nuevos pero sí se guardan las relaciones
    @Test
    public void execute_shouldReuseExistingMedicinesWithoutCreatingNewOnes() {
        Medicine existingMedicine = new Medicine(5, "Amoxicilina", "Cápsula", "250mg", "Frasco");
        when(medicineRepository.findByNames(List.of("Amoxicilina"))).thenReturn(List.of(existingMedicine));

        MedicineRowDto row = new MedicineRowDto("Amoxicilina", "Cápsula", "250mg", "Frasco", 75);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(row));

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(1, result.getInserted());
        assertTrue(result.getErrors().isEmpty());
        // saveAll no debe recibir nuevos medicamentos porque ya existen
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Medicine>> medicinesCaptor = ArgumentCaptor.forClass(List.class);
        verify(medicineRepository).saveAll(medicinesCaptor.capture());
        assertTrue(medicinesCaptor.getValue().isEmpty());
    }

    // Happy path: IDs de medicamentos recién guardados se asignan correctamente a las relaciones antes de persistirlas
    @Test
    public void execute_shouldAssignSavedMedicineIdsToRelationsBeforePersisting() {
        MedicineRowDto row = new MedicineRowDto("Metformina", "Tableta", "850mg", "Caja 28", 200);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(row));

        Medicine savedMedicine = new Medicine(99, "Metformina", "Tableta", "850mg", "Caja 28");
        when(medicineRepository.saveAll(anyList())).thenReturn(List.of(savedMedicine));

        uploadMedicineStockUseCase.execute(input);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<MedicinesHospitals>> relationsCaptor = ArgumentCaptor.forClass(List.class);
        verify(medicinesHospitalsRepository).saveAll(relationsCaptor.capture());
        List<MedicinesHospitals> savedRelations = relationsCaptor.getValue();
        assertEquals(1, savedRelations.size());
        assertEquals(99, savedRelations.get(0).getMedicine().getId());
    }

    // Sad path: el hospital no existe en la BD, debe lanzarse una excepción
    @Test
    public void execute_shouldThrowExceptionWhenHospitalNotFound() {
        when(hospitalRepository.findHospitalById(99)).thenReturn(null);

        MedicineRowDto row = new MedicineRowDto("Paracetamol", "Tableta", "500mg", "Caja 20", 100);
        MedicineStockInputDto input = new MedicineStockInputDto(99, List.of(row));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> uploadMedicineStockUseCase.execute(input));
        assertEquals("Hospital no encontrado", exception.getMessage());
    }

    // Sad path: repositorio de relaciones lanza una excepción al guardar, debe propagarse
    @Test
    public void execute_shouldPropagateExceptionWhenSavingRelationsFails() {
        MedicineRowDto row = new MedicineRowDto("Paracetamol", "Tableta", "500mg", "Caja 20", 100);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(row));

        doThrow(new RuntimeException("DB error")).when(medicinesHospitalsRepository).saveAll(anyList());

        assertThrows(RuntimeException.class, () -> uploadMedicineStockUseCase.execute(input));
    }

    // Edge case: lista de filas vacía, no debe insertar nada ni llamar a saveAll de relaciones con datos
    @Test
    public void execute_shouldReturnZeroInsertedForEmptyRows() {
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of());

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(0, result.getInserted());
        assertTrue(result.getErrors().isEmpty());
        verify(medicinesHospitalsRepository).saveAll(List.of());
    }

    // Edge case: el mismo nombre de medicamento aparece dos veces en el lote y ninguno existe en la BD
    @Test
    public void execute_shouldInsertTwoRelationsForDuplicateGenericNameInBatch() {
        MedicineRowDto row1 = new MedicineRowDto("Paracetamol", "Tableta", "500mg", "Caja 20", 100);
        MedicineRowDto row2 = new MedicineRowDto("Paracetamol", "Tableta", "500mg", "Caja 20", 50);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(row1, row2));

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(2, result.getInserted());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<MedicinesHospitals>> relationsCaptor = ArgumentCaptor.forClass(List.class);
        verify(medicinesHospitalsRepository).saveAll(relationsCaptor.capture());
        assertEquals(2, relationsCaptor.getValue().size());
    }

    // Edge case: mezcla de medicamentos existentes y nuevos en el mismo lote
    @Test
    public void execute_shouldHandleMixOfExistingAndNewMedicines() {
        Medicine existingMedicine = new Medicine(5, "Amoxicilina", "Cápsula", "250mg", "Frasco");
        when(medicineRepository.findByNames(anyList())).thenReturn(List.of(existingMedicine));

        Medicine savedNew = new Medicine(20, "Ibuprofeno", "Cápsula", "400mg", "Caja 30");
        when(medicineRepository.saveAll(anyList())).thenReturn(List.of(savedNew));

        MedicineRowDto existingRow = new MedicineRowDto("Amoxicilina", "Cápsula", "250mg", "Frasco", 30);
        MedicineRowDto newRow = new MedicineRowDto("Ibuprofeno", "Cápsula", "400mg", "Caja 30", 60);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(existingRow, newRow));

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(2, result.getInserted());
        assertTrue(result.getErrors().isEmpty());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Medicine>> newMedicinesCaptor = ArgumentCaptor.forClass(List.class);
        verify(medicineRepository).saveAll(newMedicinesCaptor.capture());
        // Solo el medicamento nuevo debe pasarse a saveAll
        assertEquals(1, newMedicinesCaptor.getValue().size());
    }

    // Sad path: nombre genérico vacío acumula error en la lista y no incrementa inserted
    @Test
    public void execute_shouldAddErrorAndNotInsertWhenGenericNameIsBlank() {
        MedicineRowDto row = new MedicineRowDto("   ", "Tableta", "500mg", "Caja 20", 100);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(row));

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(0, result.getInserted());
        assertEquals(1, result.getErrors().size());
    }

    // Sad path: stock negativo acumula error en la lista y no incrementa inserted
    @Test
    public void execute_shouldAddErrorAndNotInsertWhenStockIsNegative() {
        MedicineRowDto row = new MedicineRowDto("Paracetamol", "Tableta", "500mg", "Caja 20", -1);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(row));

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(0, result.getInserted());
        assertEquals(1, result.getErrors().size());
    }

    // Edge case: batch con filas válidas e inválidas — las válidas se insertan y las inválidas acumulan errores
    @Test
    public void execute_shouldInsertValidRowsAndAccumulateErrorsForInvalidOnesInSameBatch() {
        MedicineRowDto validRow = new MedicineRowDto("Paracetamol", "Tableta", "500mg", "Caja 20", 100);
        MedicineRowDto blankNameRow = new MedicineRowDto("", "Tableta", "500mg", "Caja 20", 50);
        MedicineRowDto negativeStockRow = new MedicineRowDto("Ibuprofeno", "Cápsula", "400mg", "Caja 30", -5);
        MedicineStockInputDto input = new MedicineStockInputDto(1, List.of(validRow, blankNameRow, negativeStockRow));

        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        assertEquals(1, result.getInserted());
        assertEquals(2, result.getErrors().size());
    }
}
