package com.pontificia.horarioponti.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase genérica para representar respuestas API con un mensaje,
 * datos y, opcionalmente, detalles de error.
 *
 * @param <T> Tipo del dato que contiene la respuesta.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /** Mensaje descriptivo de la respuesta */
    private String message;

    /** Datos de la respuesta, si los hay */
    private T data;

    /** Detalle técnico del error, opcional */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object error;

    /**
     * Constructor para respuestas sin detalle técnico de error.
     *
     * @param message Mensaje de la respuesta.
     * @param data Datos asociados a la respuesta.
     */
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.error = null;
    }

    /**
     * Crea una respuesta exitosa con datos y sin mensaje.
     *
     * @param data Datos que se incluyen en la respuesta.
     * @param <T> Tipo de los datos.
     * @return Instancia de ApiResponse con datos y sin mensaje.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data);
    }

    /**
     * Crea una respuesta exitosa con datos y mensaje.
     *
     * @param data Datos que se incluyen en la respuesta.
     * @param message Mensaje descriptivo de la respuesta.
     * @param <T> Tipo de los datos.
     * @return Instancia de ApiResponse con datos y mensaje.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(message, data);
    }

    /**
     * Crea una respuesta de error con un mensaje para el usuario
     * y sin detalle técnico.
     *
     * @param message Mensaje descriptivo del error.
     * @param <T> Tipo genérico para mantener compatibilidad.
     * @return Instancia de ApiResponse con mensaje de error.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }

    /**
     * Crea una respuesta de error con mensaje para el usuario
     * y detalle técnico del error.
     *
     * @param message Mensaje descriptivo del error.
     * @param errorDetail Detalle técnico adicional del error.
     * @param <T> Tipo genérico para mantener compatibilidad.
     * @return Instancia de ApiResponse con mensaje y detalle de error.
     */
    public static <T> ApiResponse<T> error(String message, Object errorDetail) {
        return new ApiResponse<>(message, null, errorDetail);
    }
}
