package com.itesm.domain.repository;

import com.itesm.application.dto.MonthlyReportsResponse;
import com.itesm.application.dto.StockAveragesResponse;
import com.itesm.application.dto.StockReportResponse;
import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.models.StateSupplyData;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicinesHospitalsRepository {
    List<MedicinesHospitalsStock> findByMedicineName(String medicineName);

    List<StateSupplyData> findAvgStockByState();

    void save(MedicinesHospitals medicineHospital);

    void saveAll(List<MedicinesHospitals> records);

    Optional<StockAveragesResponse> getStockAvg(Integer idHospital);

    Optional<StockReportResponse> getStockReport(Integer idHospital);

    Optional<MonthlyReportsResponse> getMonthlyReports(Integer idHospital);

    List<MedicinesHospitals> findLatestReportsByHospitalIds(List<Integer> hospitalIds, int page, int size);

    long countCriticalByHospitalIds(List<Integer> hospitalIds);

    List<Object[]> getPeriodStock(Integer idHospital, LocalDate startDate, LocalDate endDate);
}
