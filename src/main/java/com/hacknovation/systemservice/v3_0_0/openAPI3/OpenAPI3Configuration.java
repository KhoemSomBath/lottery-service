package com.hacknovation.systemservice.v3_0_0.openAPI3;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Sombath
 * create at 9/5/23 12:09 AM
 */
@Configuration
public class OpenAPI3Configuration {
    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "basicAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .bearerFormat("JWT"))
                        .addSecuritySchemes(AUTHORIZATION, new SecurityScheme()
                                .name(AUTHORIZATION)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
