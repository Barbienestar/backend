package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.CreateReportDto;
import com.itesm.application.dto.ReportDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Medicine;
import com.itesm.domain.models.Report;
import com.itesm.domain.models.Role;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.repository.MedicineRepository;
import com.itesm.domain.repository.ReportRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/** CreateReportUseCaseTest */
public class CreateReportUseCaseTest {
    private ReportRepository reportRepository;
    private AuthenticatedUserContext authUserContext;
    private CreateReportUseCase createReportUseCase;
    private HospitalRepository hospitalRepository;
    private MedicineRepository medicineRepository;

    @BeforeEach
    void setup() {
        reportRepository = mock(ReportRepository.class);
        authUserContext = mock(AuthenticatedUserContext.class);
        hospitalRepository = mock(HospitalRepository.class);
        medicineRepository = mock(MedicineRepository.class);

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setRole(new Role((byte) 1, "citizen"));
        user.setLastName1("Pork");
        CurrentUser currentUser = new CurrentUser(user);
        when(authUserContext.getCurrentUser()).thenReturn(currentUser);
        when(reportRepository.save(any(Report.class)))
                .thenAnswer(
                        invocation -> {
                            Report report = invocation.getArgument(0);
                            report.setId(1L);
                            return report;
                        });

        when(hospitalRepository.findHospitalById(anyInt()))
                .thenAnswer(
                        invocation -> {
                            Hospital hospital = new Hospital();
                            hospital.setId(1);
                            hospital.setName("Hospital name");
                            return hospital;
                        });
        when(medicineRepository.findMedicineById(anyInt()))
                .thenAnswer(
                        invocation -> {
                            Medicine medicine = new Medicine();
                            medicine.setId(1);
                            medicine.setGenericName("Paracetamol");
                            return medicine;
                        });
        createReportUseCase =
                new CreateReportUseCase(
                        reportRepository, authUserContext, hospitalRepository, medicineRepository);
    }

    @Test
    public void execute_shouldCreateReport() {
        CreateReportDto dto = new CreateReportDto();
        dto.setDescription("Test description");
        dto.setImageUrl("https://example.com/image.png");
        dto.setMedicineId(1);
        dto.setHospitalId(1);

        ReportDto result = createReportUseCase.execute(dto);
        assertEquals(1, result.getId());
        assertEquals("Test description", result.getDescription());
        assertEquals("Paracetamol", result.getMedicineName());
        assertEquals("Hospital name", result.getHospitalName());
    }

    @Test
    public void execute_shouldThrowExceptionIfUserIsNotAuthenticated() {
        when(authUserContext.getCurrentUser()).thenReturn(null);

        CreateReportDto dto = new CreateReportDto();
        dto.setDescription("Test description");
        dto.setImageUrl("https://example.com/image.png");
        dto.setMedicineId(1);
        dto.setHospitalId(1);

        assertThrows(NullPointerException.class, () -> createReportUseCase.execute(dto));
    }

    @Test
    public void execute_shouldCallSaveOnReportRepository() {
        CreateReportDto dto = new CreateReportDto();
        dto.setDescription("Test description");
        dto.setImageUrl("https://example.com/image.png");
        dto.setMedicineId(1);
        dto.setHospitalId(1);

        createReportUseCase.execute(dto);
        ArgumentCaptor<Report> rArgumentCaptor = ArgumentCaptor.forClass(Report.class);
        verify(reportRepository, times(1)).save(rArgumentCaptor.capture());
        Report report = rArgumentCaptor.getValue();
        assertEquals(1, report.getId());
        assertEquals(1, report.getMedicineId());
        assertEquals(1, report.getHospitalId());
        assertEquals("Test description", report.getDescription());
        assertEquals("https://example.com/image.png", report.getImageUrl());
    }
}
