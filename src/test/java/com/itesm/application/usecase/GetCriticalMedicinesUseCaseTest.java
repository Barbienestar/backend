package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.HospitalCriticalMedicinesDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Medicine;
import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetCriticalMedicinesUseCaseTest {

    private MedicinesHospitalsRepository medicinesHospitalsRepository;
    private HospitalRepository hospitalRepository;
    private AuthenticatedUserContext authUserContext;
    private GetCriticalMedicinesUseCase useCase;

    @BeforeEach
    void setup() {
        medicinesHospitalsRepository = mock(MedicinesHospitalsRepository.class);
        hospitalRepository = mock(HospitalRepository.class);
        authUserContext = mock(AuthenticatedUserContext.class);

        User user = new User();
        user.setId(1L);
        user.setRole(new Role((byte) 1, "health"));
        CurrentUser currentUser = new CurrentUser(user);
        when(authUserContext.getCurrentUser()).thenReturn(currentUser);

        useCase = new GetCriticalMedicinesUseCase(medicinesHospitalsRepository, hospitalRepository, authUserContext);
    }

    @Test
    void execute_shouldReturnOnlyCriticalMedicines() {
        Hospital hospital = new Hospital(1, "Hospital A", "https://maps/1");

        Medicine criticalMed = new Medicine();
        criticalMed.setId(1);
        criticalMed.setGenericName("Paracetamol");

        Medicine nonCriticalMed = new Medicine();
        nonCriticalMed.setId(2);
        nonCriticalMed.setGenericName("Ibuprofeno");

        MedicinesHospitals criticalRecord = new MedicinesHospitals(criticalMed, hospital, 5, LocalDateTime.now());
        MedicinesHospitals nonCriticalRecord =
                new MedicinesHospitals(nonCriticalMed, hospital, 11, LocalDateTime.now());

        when(hospitalRepository.findHospitalsByUserId(1L)).thenReturn(List.of(hospital));
        when(medicinesHospitalsRepository.findLatestReportsByHospitalIds(anyList()))
                .thenReturn(List.of(criticalRecord, nonCriticalRecord));

        List<HospitalCriticalMedicinesDto> result = useCase.execute(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getCriticalMedicines().size());
        assertEquals("Paracetamol", result.get(0).getCriticalMedicines().get(0).getGenericName());
        assertEquals(5, result.get(0).getCriticalMedicines().get(0).getStock());
    }

    // El hospital consultado no tiene medicamentos críticos — debe regresar lista vacía
    @Test
    void execute_shouldReturnEmptyWhenNoCriticalMedicines() {
        Hospital hospital = new Hospital(1, "Hospital A", "https://maps/1");

        Medicine med = new Medicine();
        med.setId(1);
        med.setGenericName("Ibuprofeno");

        MedicinesHospitals nonCriticalRecord = new MedicinesHospitals(med, hospital, 200, LocalDateTime.now());

        when(hospitalRepository.findHospitalsByUserId(1L)).thenReturn(List.of(hospital));
        when(medicinesHospitalsRepository.findLatestReportsByHospitalIds(anyList()))
                .thenReturn(List.of(nonCriticalRecord));

        List<HospitalCriticalMedicinesDto> result = useCase.execute(1);

        assertTrue(result.isEmpty());
    }

    // El hospital solicitado no está asignado al usuario — debe lanzar excepción
    @Test
    void execute_shouldThrowWhenHospitalDoesNotBelongToUser() {
        Hospital hospital = new Hospital(1, "Hospital A", "https://maps/1");

        when(hospitalRepository.findHospitalsByUserId(1L)).thenReturn(List.of(hospital));

        assertThrows(RuntimeException.class, () -> useCase.execute(99));
    }
}
