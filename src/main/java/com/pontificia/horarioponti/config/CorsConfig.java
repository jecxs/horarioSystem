package com.pontificia.horarioponti.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
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