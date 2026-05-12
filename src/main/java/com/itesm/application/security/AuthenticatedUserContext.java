package com.itesm.application.security;

import jakarta.enterprise.context.RequestScoped;
import lombok.Data;

@Data
@RequestScoped
public class AuthenticatedUserContext {
    private CurrentUser currentUser;
}
