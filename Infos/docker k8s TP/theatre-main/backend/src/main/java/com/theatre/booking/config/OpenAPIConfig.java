package com.theatre.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration de la documentation OpenAPI/Swagger
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI theatreBookingOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Serveur de développement");

        Contact contact = new Contact();
        contact.setName("Équipe Théâtre");
        contact.setEmail("support@theatre.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("API Plateforme de Billetterie de Théâtre")
                .version("1.0.0")
                .contact(contact)
                .description("API REST pour la gestion des réservations de spectacles de théâtre")
                .termsOfService("https://theatre.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
