package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.ReportSummaryDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.*;
import com.itesm.domain.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class GetMyReportsUseCaseTest {

    private ReportRepository reportRepository;
    private MedicineRepository medicineRepository;
    private HospitalRepository hospitalRepository;
    private StatusRepository statusRepository;
    private AuthenticatedUserContext authUserContext;
    private GetMyReportsUseCase useCase;

    @BeforeEach
    void setup() {
        reportRepository = mock(ReportRepository.class);
        medicineRepository = mock(MedicineRepository.class);
        hospitalRepository = mock(HospitalRepository.class);
        statusRepository = mock(StatusRepository.class);
        authUserContext = mock(AuthenticatedUserContext.class);

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName1("Doe");
        when(authUserContext.getCurrentUser()).thenReturn(new CurrentUser(user));

        Medicine medicine = new Medicine();
        medicine.setId(1);
        medicine.setGenericName("Metformina 850mg");
        when(medicineRepository.findMedicineById(1)).thenReturn(medicine);

        Hospital hospital = new Hospital();
        hospital.setId(1);
        hospital.setName("IMSS Clínica 12");
        when(hospitalRepository.findHospitalById(1)).thenReturn(hospital);

        Status status = new Status((byte) 2, "reviewing");
        when(statusRepository.findStatusById((byte) 2)).thenReturn(status);

        Report report = new Report(100L, 1L, 1, 1, (byte) 2, "Descripción test", null, LocalDateTime.now(), LocalDateTime.now());
        when(reportRepository.findByUserId(1)).thenReturn(List.of(report));

        useCase = new GetMyReportsUseCase(reportRepository, medicineRepository, hospitalRepository, statusRepository, authUserContext);
    }

    @Test
    void execute_shouldReturnReportsForCurrentUser() {
        List<ReportSummaryDto> result = useCase.execute();

        assertEquals(1, result.size());
        ReportSummaryDto dto = result.get(0);
        assertEquals(100L, dto.getId());
        assertEquals("Metformina 850mg", dto.getMedicineName());
        assertEquals("IMSS Clínica 12", dto.getHospitalName());
        assertEquals("reviewing", dto.getStatus());
        assertEquals("Descripción test", dto.getDescription());
    }

    @Test
    void execute_shouldReturnEmptyListWhenNoReports() {
        when(reportRepository.findByUserId(1)).thenReturn(List.of());

        List<ReportSummaryDto> result = useCase.execute();

        assertTrue(result.isEmpty());
    }

    @Test
    void execute_shouldThrowWhenUserNotAuthenticated() {
        when(authUserContext.getCurrentUser()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> useCase.execute());
    }
}
