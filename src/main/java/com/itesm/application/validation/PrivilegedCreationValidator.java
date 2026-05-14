package com.itesm.application.validation;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.security.CurrentUser;

import jakarta.ws.rs.ForbiddenException;

/** PrivilegedCreationValidator */
public class PrivilegedCreationValidator implements UserCreationValidator {

    @Override
    public void validate(CreateUserDto dto, CurrentUser currentUser) throws ForbiddenException {
        if (currentUser == null || !currentUser.hasRole("admin")) {
            throw new ForbiddenException("Only admins can create privileged users");
        }
    }
}
