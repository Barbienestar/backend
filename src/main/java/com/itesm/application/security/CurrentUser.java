package com.itesm.application.security;

import com.itesm.domain.models.User;

/** CurrentUser */
public class CurrentUser {
    private final Integer id;
    private final String firebaseUid;
    private final String email;
    private final Byte role;
    private final String name;

    public CurrentUser(User user) {
        this.id = user.getId();
        this.firebaseUid = user.getProviderUuid();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.name = user.getName();
    }

    public CurrentUser(Integer id, String firebaseUid, String email, Byte role, String name) {
        this.id = id;
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.role = role;
        this.name = name;
    }

    public Boolean hasRole(Byte role) {
        if (role != null) {
            return true;
        }
        return false;
    }

    public Integer getId() {
        return id;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public String getEmail() {
        return email;
    }

    public Byte getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
