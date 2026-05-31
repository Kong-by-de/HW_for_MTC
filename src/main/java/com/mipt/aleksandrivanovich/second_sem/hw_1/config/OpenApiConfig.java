package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI для документирования API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(
        @Value("${app.name:Todo Manager}") String appName,
        @Value("${app.version:2.0.0}") String appVersion) {

        return new OpenAPI()
            .info(new Info()
                .title(appName + " API")
                .version(appVersion)
                .description("RESTful API для управления задачами (To-Do List Manager)")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                    .name("API Support")
                    .url("http://www.example.com/support")
                    .email("support@example.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")));
    }
}