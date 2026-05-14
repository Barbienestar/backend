package com.itesm.domain.repository;

import java.util.Optional;

import com.itesm.domain.models.MedicinesHospitalsStockAverages;

public interface MedicineHospitalStockAvgRepository {
    Optional<MedicinesHospitalsStockAverages> getStockAvg(Integer idHospital);
}
