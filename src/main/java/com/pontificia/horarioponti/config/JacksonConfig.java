package com.pontificia.horarioponti.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuración personalizada de Jackson para la serialización y deserialización JSON.
 * Se configura el manejo de referencias circulares, exclusión de valores nulos,
 * formato de fecha y soporte para tipos Java 8 Time API.
 */
@Configuration
public class JacksonConfig {

    /**
     * Construye y configura un {@link Jackson2ObjectMapperBuilder} personalizado para
     * la aplicación Spring.
     * Configuraciones realizadas:
     *     Deshabilita la excepción ante beans vacíos para evitar fallos con referencias circulares.
     *     Excluye propiedades con valores nulos en la serialización JSON.
     *     Define un formato estándar para fechas: "yyyy-MM-dd HH:mm:ss".
     *     Agrega soporte para la API de fechas y horas de Java 8 mediante {@link JavaTimeModule}.
     *
     * @return un {@link Jackson2ObjectMapperBuilder} configurado para uso en la aplicación.
     */
    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        // Configuración para manejar referencias circulares sin fallar
        builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // No incluir propiedades con valor null en la serialización JSON
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);

        // Formato estándar para fechas y horas
        builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Soporte para tipos de fecha/hora de Java 8
        builder.modules(new JavaTimeModule());

        return builder;
    }
}
