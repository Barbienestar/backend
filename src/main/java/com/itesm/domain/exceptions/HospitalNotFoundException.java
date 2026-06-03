package com.itesm.domain.exceptions;

public class HospitalNotFoundException extends RuntimeException {
    public HospitalNotFoundException(Integer idHospital) {
        super("Hospital not found: " + idHospital);
    }
}
