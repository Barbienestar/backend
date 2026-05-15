package com.itesm.domain.repository;

import java.util.List;
import java.util.Optional;

import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.models.MedicinesHospitalsStockAverages;
import com.itesm.domain.models.MedicinesHospitalsStockReport;

public interface MedicinesHospitalsRepository {
    List<MedicinesHospitalsStock> findByMedicineName(String medicineName);
    Optional<MedicinesHospitalsStockAverages> getStockAvg(Integer idHospital);
    Optional<MedicinesHospitalsStockReport> getStockReport(Integer idHospital);
}
