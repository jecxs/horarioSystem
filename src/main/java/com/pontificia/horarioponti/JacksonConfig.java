package com.pontificia.horarioponti;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        // Configuración para manejar referencias circulares
        builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);

        // Importante: esta configuración evita el error de profundidad máxima
        builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        builder.modules(new JavaTimeModule());

        return builder;
    }
}
