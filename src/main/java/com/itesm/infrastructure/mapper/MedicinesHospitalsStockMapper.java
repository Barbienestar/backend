package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.infrastructure.persistence.entity.HospitalEntity;
import com.itesm.infrastructure.persistence.entity.MedicinesHospitalsEntity;

public class MedicinesHospitalsStockMapper {

    public static MedicinesHospitalsStock toDomain(MedicinesHospitalsEntity mh) {
        HospitalEntity h = mh.getHospital();

        String street  = h.getStreet().getName();
        String suburb  = h.getStreet().getIdSuburb().getName();
        String city    = h.getStreet().getIdSuburb().getIdCity().getName();
        String address = street + ", " + suburb + ", " + city;

        return new MedicinesHospitalsStock(
                h.getId(),
                h.getName(),
                address,
                mh.getStock(),
                h.getMapsUrl()
        );
    }
}