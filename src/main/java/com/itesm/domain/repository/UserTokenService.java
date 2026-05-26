package com.itesm.domain.repository;

public interface UserTokenService {
    String createUser(String email, String password);

    void deleteUser(String providerUuid);

}
