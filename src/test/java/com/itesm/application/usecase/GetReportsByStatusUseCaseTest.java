package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.FullReportResponse;
import com.itesm.application.dto.PagedResult;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Medicine;
import com.itesm.domain.models.Report;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.ImageRepository;
import com.itesm.domain.repository.ReportRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetReportsByStatusUseCaseTest {

    private ReportRepository reportRepository;
    private ImageRepository imageRepository;
    private GetReportsByStatusUseCase useCase;

    @BeforeEach
    void setup() {
        reportRepository = mock(ReportRepository.class);
        imageRepository = mock(ImageRepository.class);
        useCase = new GetReportsByStatusUseCase(reportRepository, imageRepository);
    }

    @Test
    public void execute_shouldReturnPagedReports() {
        Integer statusId = 2;
        Integer page = 0;
        Integer pageSize = 10;

        User user = new User();
        user.setName("Maria");
        user.setLastName1("Garcia");
        user.setLastName2("Lopez");

        Medicine medicine = new Medicine();
        medicine.setGenericName("Paracetamol");
        medicine.setPresentation("Tableta");
        medicine.setDosageForm("500mg");

        Hospital hospital = new Hospital();
        hospital.setName("Hospital General");

        Report report = new Report();
        report.setId(1L);
        report.setDescription("Dolor de cabeza");
        report.setImageUrl("reports/image1.jpg");
        report.setUser(user);
        report.setMedicine(medicine);
        report.setHospital(hospital);
        report.setCreatedAt(LocalDateTime.of(2025, 1, 15, 10, 30));

        List<Report> reports = List.of(report);

        when(reportRepository.findByStatusId(statusId, page, pageSize)).thenReturn(reports);
        when(reportRepository.countByStatusId(statusId)).thenReturn(1L);
        when(imageRepository.generateSignedUrl("reports/image1.jpg", 30, TimeUnit.MINUTES))
                .thenReturn("https://signed-url.com/image1.jpg");

        PagedResult<FullReportResponse> result = useCase.execute(statusId, page, pageSize);

        assertNotNull(result);
        assertEquals(1, result.items().size());
        assertEquals(0, result.page());
        assertEquals(10, result.pageSize());
        assertEquals(1, result.totalItems());
        assertEquals(1, result.totalPages());

        FullReportResponse response = result.items().get(0);
        assertEquals(1L, response.getId());
        assertEquals("Dolor de cabeza", response.getDescription());
        assertEquals("https://signed-url.com/image1.jpg", response.getImageUrl());
        assertEquals("Maria Garcia Lopez", response.getUserFullName());
        assertEquals("Paracetamol", response.getMedicineName());
        assertEquals("Tableta", response.getMedicinePresentation());
        assertEquals("500mg", response.getMedicineDosageForm());
        assertEquals("Hospital General", response.getHospitalName());
        assertEquals(LocalDateTime.of(2025, 1, 15, 10, 30), response.getCreatedAt());
    }

    @Test
    public void execute_shouldReturnEmptyPageWhenNoReports() {
        Integer statusId = 99;
        Integer page = 0;
        Integer pageSize = 10;

        when(reportRepository.findByStatusId(statusId, page, pageSize)).thenReturn(List.of());
        when(reportRepository.countByStatusId(statusId)).thenReturn(0L);

        PagedResult<FullReportResponse> result = useCase.execute(statusId, page, pageSize);

        assertNotNull(result);
        assertTrue(result.items().isEmpty());
        assertEquals(0, result.totalItems());
        assertEquals(0, result.totalPages());
    }

    @Test
    public void execute_shouldGenerateSignedUrlForEachReport() {
        User user = new User();
        user.setName("A");
        user.setLastName1("B");
        user.setLastName2("C");

        Medicine medicine = new Medicine();
        medicine.setGenericName("M");
        medicine.setPresentation("P");
        medicine.setDosageForm("D");

        Hospital hospital = new Hospital();
        hospital.setName("H");

        Report r1 = new Report();
        r1.setId(1L);
        r1.setDescription("d1");
        r1.setImageUrl("img1.jpg");
        r1.setUser(user);
        r1.setMedicine(medicine);
        r1.setHospital(hospital);
        r1.setCreatedAt(LocalDateTime.now());

        Report r2 = new Report();
        r2.setId(2L);
        r2.setDescription("d2");
        r2.setImageUrl("img2.jpg");
        r2.setUser(user);
        r2.setMedicine(medicine);
        r2.setHospital(hospital);
        r2.setCreatedAt(LocalDateTime.now());

        when(reportRepository.findByStatusId(1, 0, 10)).thenReturn(List.of(r1, r2));
        when(reportRepository.countByStatusId(1)).thenReturn(2L);
        when(imageRepository.generateSignedUrl(anyString(), eq(30L), eq(TimeUnit.MINUTES)))
                .thenReturn("https://signed.url");

        useCase.execute(1, 0, 10);

        verify(imageRepository, times(2)).generateSignedUrl(anyString(), eq(30L), eq(TimeUnit.MINUTES));
        verify(imageRepository).generateSignedUrl(eq("img1.jpg"), eq(30L), eq(TimeUnit.MINUTES));
        verify(imageRepository).generateSignedUrl(eq("img2.jpg"), eq(30L), eq(TimeUnit.MINUTES));
    }
}
