package br.com.fiap.restaurante.restaurante.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI restaurante() {
        return new OpenAPI()
                .info(
                        new Info().title("Restaurante API")
                                  .description("Restaurants' API documentation. " +
                                               "To access the JSON API documentation, see the path 'api-docs'.")
                                  .version("v0.0.1")
                );
    }
}
