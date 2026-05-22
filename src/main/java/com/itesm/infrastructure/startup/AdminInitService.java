package com.itesm.infrastructure.startup;

import com.itesm.application.usecase.EnsureAdminUseCase;

import io.quarkus.runtime.Startup;
import io.quarkus.arc.profile.UnlessBuildProfile;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
@ApplicationScoped
@UnlessBuildProfile("test")
public class AdminInitService {

    private static final Logger log = LoggerFactory.getLogger(AdminInitService.class);

    private final EnsureAdminUseCase ensureAdminUseCase;

    @Inject
    public AdminInitService(EnsureAdminUseCase ensureAdminUseCase) {
        this.ensureAdminUseCase = ensureAdminUseCase;
    }

    @PostConstruct
    void init() {
        log.info("Checking for admin user...");
        try {
            ensureAdminUseCase.execute();
            log.info("Admin user check complete");
        } catch (Exception e) {
            log.error("Failed to initialize admin user", e);
        }
    }
}
