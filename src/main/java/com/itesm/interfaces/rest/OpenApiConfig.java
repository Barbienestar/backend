package com.itesm.interfaces.rest;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@OpenAPIDefinition(
    info = @Info(
        title = "BarBienestar API",
        version = "1.0.0",
        description = "REST API for the BarBienestar health and wellbeing platform"
    )
)
@SecurityScheme(
    securitySchemeName = "BearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Firebase Authentication Bearer token"
)
public class OpenApiConfig {
}
