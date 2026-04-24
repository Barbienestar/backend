package com.itesm.application.security;

import com.itesm.domain.models.User;
import java.util.UUID;

/**
 * CurrentUser
 */
public class CurrentUser {
    private final UUID id;
    private final String firebaseUid;
    private final String email;
    private final String role;
    private final String name;

    public CurrentUser(User user) {
        this.id = user.getId();
        this.firebaseUid = user.getProviderUid();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.name = user.getName();
    }

    public CurrentUser(UUID id, String firebaseUid, String email, String role, String name) {
        this.id = id;
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.role = role;
        this.name = name;
    }

    public Boolean hasRole(String role) {
        return this.role.equalsIgnoreCase(role);
    }

    public UUID getId() {
        return id;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
