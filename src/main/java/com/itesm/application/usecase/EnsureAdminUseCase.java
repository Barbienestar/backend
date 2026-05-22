package com.itesm.application.usecase;

import com.itesm.domain.models.User;
import com.itesm.domain.repository.RoleRepository;
import com.itesm.domain.repository.UserRepository;
import com.itesm.domain.repository.UserTokenService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class EnsureAdminUseCase {

    private final UserRepository userRepository;
    private final UserTokenService userTokenService;
    private final RoleRepository roleRepository;
    private final String email;
    private final String password;
    private final String name;
    private final String lastName;

    @Inject
    public EnsureAdminUseCase(
            UserRepository userRepository,
            UserTokenService userTokenService,
            RoleRepository roleRepository,
            @ConfigProperty(name = "admin.email") String email,
            @ConfigProperty(name = "admin.password") String password,
            @ConfigProperty(name = "admin.name") String name,
            @ConfigProperty(name = "admin.last-name") String lastName) {
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
        this.roleRepository = roleRepository;
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
    }

    public void execute() {
        var adminRole = roleRepository.findByName("admin");
        if (adminRole.isEmpty()) {
            return;
        }

        if (userRepository.countByRoleId(adminRole.get().getId()) > 0) {
            return;
        }

        String providerUuid = userTokenService.createUser(email, password);

        User user = new User();
        user.setName(name);
        user.setLastName1(lastName);
        user.setEmail(email);
        user.setProviderUuid(providerUuid);
        user.setActive(true);
        user.setRole(adminRole.get());
        user.setAddress(null);

        userRepository.save(user);
    }
}
