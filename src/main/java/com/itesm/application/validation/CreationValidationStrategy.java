package com.itesm.application.validation;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.security.CurrentUser;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ForbiddenException;

/** CreationValidationStrategy */
@ApplicationScoped
public class CreationValidationStrategy {
    private UserCreationValidator validator;

    public void setValidator(UserCreationValidator validator) {
        this.validator = validator;
    }

    public void validate(CreateUserDto dto, CurrentUser currentUser) throws ForbiddenException {
        validator.validate(dto, currentUser);
    }
}
