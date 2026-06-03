package com.itesm.domain.exceptions;

public class HospitalNotAssignedException extends RuntimeException {
    public HospitalNotAssignedException(Integer hospitalId, Long userId) {
        super("Hospital " + hospitalId + " not assigned to user " + userId);
    }
}
