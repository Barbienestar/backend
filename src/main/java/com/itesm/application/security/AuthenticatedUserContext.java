package com.itesm.application.security;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class AuthenticatedUserContext {
    private CurrentUser currentuser;

    public CurrentUser getCurrentUser() {
        return currentuser;
    }

    public void setCurrentUser(CurrentUser currentuser) {
        this.currentuser = currentuser;
    }
}
