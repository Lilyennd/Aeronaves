package cl.GestionDrones.v1.aeronaves.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Gestión de Drones - Aeronaves")
                .version("1.0.0")
                .description("Microservicio encargado de la administración, registro y control de estado de las aeronaves (drones)"));
    }
}