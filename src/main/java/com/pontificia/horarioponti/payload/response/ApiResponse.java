package com.pontificia.horarioponti.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String message;
    private T data;
    private String error;

    /** * Constructor para respuestas sin detalle técnico de error */
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
        this.error = null;
    }

    /** * Respuesta exitosa con datos */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(null, data);
    }

    /** * Respuesta exitosa con datos y mensaje */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>( message, data);
    }

    /** * Respuesta de error con mensaje para el usuario */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>( message, null);
    }

    /** * Respuesta de error con mensaje para el usuario y detalle técnico */
    public static <T> ApiResponse<T> error(String message, String errorDetail) {
        return new ApiResponse<>( message, null, errorDetail);
    }
}
