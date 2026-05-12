package com.itesm.application.security;

import com.itesm.domain.models.User;

import lombok.Data;

/** CurrentUser */
@Data
public class CurrentUser {
    private final Long id;
    private final String firebaseUid;
    private final String email;
    private final String role;
    private final String name;

    public CurrentUser(User user) {
        this.id = user.getId();
        this.firebaseUid = user.getProviderUuid();
        this.email = user.getEmail();
        this.role = user.getRole().getName();
        this.name = user.getName();
    }

    public boolean hasRole(String role) {
        return this.role.equals(role);
    }
}
