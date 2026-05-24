package com.itesm.domain.exceptions;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(Byte roleId) {
        super("Invalid role ID: " + roleId + ". Allowed values are 1, 2, or 3.");
    }
}
