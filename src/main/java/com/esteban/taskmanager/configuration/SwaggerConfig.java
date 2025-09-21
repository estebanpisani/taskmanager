package com.esteban.taskmanager.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class SwaggerConfig {

    @Bean
    public OpenAPI taskManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("API REST para la gestión de tareas con Spring Boot 3 y Java 17.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Esteban Pisani")
                                .url("https://github.com/estebanpisani/taskmanager")
                        )
                );
    }
}
