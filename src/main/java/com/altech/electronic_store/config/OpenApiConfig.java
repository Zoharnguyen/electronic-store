package com.altech.electronic_store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI electronicsStoreOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Electronics Store API")
                .description("RESTful API for Electronics Store checkout system")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Electronics Store Team")
                    .email("support@electronics-store.com")));
    }
} 