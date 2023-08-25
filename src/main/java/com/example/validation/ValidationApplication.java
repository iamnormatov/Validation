package com.example.validation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//6ce9ea13-f4e9-4d3e-bd14-29b8cc2ce507

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                version = "v: 3.0.4",
                title = "First Swagger",
                description = "Learn Spring Boot project with Swagger",
                contact = @Contact(
                        name = "Swagger Project",
                        url = "https://t.me/iamnormatov",
                        email = "azizjonnormatov437@gmail.com"
                ),
                license = @License(
                        name = "Swagger Project License",
                        url = "https://t.me/iamnormatov"
                )

        ),
        servers = {@Server(url = "localhost:8080")},
        tags = {@Tag(name = "",description = "")}
)
public class ValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValidationApplication.class, args);
    }

}
