package com.itesm.application.validation;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.security.CurrentUser;

import jakarta.ws.rs.ForbiddenException;

/** CitizenCreationValidator */
public class CitizenCreationValidator implements UserCreationValidator {

    @Override
    public void validate(CreateUserDto dto, CurrentUser currentUser) throws ForbiddenException {
        // citizens can be created with no restrictions
        return;
    }
}
