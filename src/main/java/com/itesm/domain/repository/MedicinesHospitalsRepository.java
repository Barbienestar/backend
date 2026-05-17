package com.itesm.domain.repository;

import java.util.List;
import java.util.Optional;

import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.models.MedicinesHospitalsStockAverages;
import com.itesm.domain.models.MedicinesHospitalsStockReport;

public interface MedicinesHospitalsRepository {
    List<MedicinesHospitalsStock> findByMedicineName(String medicineName);
    void save(MedicinesHospitals medicineHospital);
    void saveAll(List<MedicinesHospitals> records);
    Optional<MedicinesHospitalsStockAverages> getStockAvg(Integer idHospital);
    Optional<MedicinesHospitalsStockReport> getStockReport(Integer idHospital);
}
