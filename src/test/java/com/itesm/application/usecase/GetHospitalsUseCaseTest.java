package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.HospitalDto;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.repository.HospitalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetHospitalsUseCaseTest {

    private HospitalRepository hospitalRepository;
    private GetHospitalsUseCase getHospitalsUseCase;

    @BeforeEach
    void setup() {
        hospitalRepository = mock(HospitalRepository.class);

        Hospital h1 = new Hospital();
        h1.setId(1);
        h1.setName("Hospital Norte");

        Hospital h2 = new Hospital();
        h2.setId(2);
        h2.setName("Hospital Sur");

        when(hospitalRepository.findAllHospitals()).thenReturn(List.of(h1, h2));

        getHospitalsUseCase = new GetHospitalsUseCase(hospitalRepository);
    }

    @Test
    public void execute_shouldReturnAllHospitals() {
        List<HospitalDto> result = getHospitalsUseCase.execute();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Hospital Norte", result.get(0).getName());
        assertEquals(2, result.get(1).getId());
        assertEquals("Hospital Sur", result.get(1).getName());
    }

    @Test
    public void execute_shouldReturnEmptyListWhenNoHospitals() {
        when(hospitalRepository.findAllHospitals()).thenReturn(List.of());

        List<HospitalDto> result = getHospitalsUseCase.execute();

        assertTrue(result.isEmpty());
    }

    @Test
    public void execute_shouldCallFindAllHospitals() {
        getHospitalsUseCase.execute();

        verify(hospitalRepository, times(1)).findAllHospitals();
    }
}