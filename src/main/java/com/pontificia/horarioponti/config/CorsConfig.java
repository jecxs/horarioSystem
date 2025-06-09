package com.pontificia.horarioponti.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración CORS (Cross-Origin Resource Sharing) para la aplicación.
 * Define las políticas de orígenes permitidos, métodos HTTP, cabeceras,
 * y otras opciones para controlar las solicitudes cross-origin.
 */
@Configuration
public class CorsConfig {

    /**
     * Orígenes permitidos para solicitudes CORS, inyectados desde
     * la propiedad 'app.cors.allowed-origins' en el archivo de configuración.
     */
    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    /**
     * Define el bean {@link WebMvcConfigurer} que configura las políticas CORS
     * para la aplicación.
     *
     * @return un {@link WebMvcConfigurer} que registra las configuraciones CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Configura los mapeos CORS para todas las rutas de la aplicación.
             *
             * @param corsRegistry objeto para registrar las políticas CORS.
             */
            @Override
            public void addCorsMappings(CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .allowedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
