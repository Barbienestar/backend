package com.itesm.domain.repository;

import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.models.MedicinesHospitalsStockAverages;
import com.itesm.domain.models.MedicinesHospitalsStockReport;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicinesHospitalsRepository {
    List<MedicinesHospitalsStock> findByMedicineName(String medicineName);

    void save(MedicinesHospitals medicineHospital);

    void saveAll(List<MedicinesHospitals> records);

    Optional<MedicinesHospitalsStockAverages> getStockAvg(Integer idHospital);

    Optional<MedicinesHospitalsStockReport> getStockReport(Integer idHospital);

    List<MedicinesHospitals> findLatestReportsByHospitalIds(List<Integer> hospitalIds);

    List<Object[]> getPeriodStock(Integer idHospital, LocalDate startDate, LocalDate endDate);
}
