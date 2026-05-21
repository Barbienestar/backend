package com.itesm.application.validation;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.security.CurrentUser;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/** PrivilegedCreationValidator */
public class PrivilegedCreationValidator implements UserCreationValidator {

    @Override
    public void validate(CreateUserDto dto, CurrentUser currentUser) throws ForbiddenException {
        System.out.println(currentUser);
        if (currentUser == null || !currentUser.hasRole("admin")) {
            throw new ForbiddenException(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity(Map.of("message", "Only admins can create privileged users"))
                            .type(MediaType.APPLICATION_JSON)
                            .build());
        }
    }
}
