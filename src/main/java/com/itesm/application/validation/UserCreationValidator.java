package com.itesm.application.validation;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.security.CurrentUser;

import jakarta.ws.rs.ForbiddenException;

/** UserCreationValidator */
public interface UserCreationValidator {

    void validate(CreateUserDto dto, CurrentUser currentUser) throws ForbiddenException;
}
