package com.nemless.school_wits.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        Server httpsServer = new Server()
                .url(apiServerUrl)
                .description("Secure Backend Server");

        return new OpenAPI()
                .info(new Info()
                        .title("SchoolWits backend API list")
                        .version("1.0")
                        .description("API documentation with JWT support"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .servers(List.of(httpsServer));
    }
}
