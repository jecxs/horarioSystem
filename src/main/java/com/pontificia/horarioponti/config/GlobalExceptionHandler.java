package com.pontificia.horarioponti.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que maneja las excepciones globalmente para los controladores REST.
 * Define handlers para distintos tipos de excepciones y devuelve respuestas HTTP
 * adecuadas con mensajes estandarizados para el cliente.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo ValidationTopicException.
     * Se usa para errores de validación personalizados lanzados explícitamente en la lógica de negocio.
     *
     * @param ex Excepción de validación personalizada.
     * @return ResponseEntity con estado 400 BAD REQUEST y mensaje de error personalizado.
     */
    @ExceptionHandler(ValidationTopicException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationTopicException(ValidationTopicException ex) {
        String message = ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, "Error de validación personalizado"));
    }

    /**
     * Maneja las excepciones EntityNotFoundException.
     * Se lanza cuando un recurso solicitado no existe en la base de datos.
     *
     * @param ex Excepción que indica que no se encontró la entidad.
     * @return ResponseEntity con estado 404 NOT FOUND y mensaje indicando que no se encontró el recurso.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Recurso no encontrado", ex.getMessage()));
    }

    /**
     * Maneja las excepciones IllegalArgumentException.
     * Se usa para indicar que los argumentos recibidos son inválidos o incorrectos.
     *
     * @param ex Excepción de argumento ilegal.
     * @return ResponseEntity con estado 400 BAD REQUEST y mensaje indicando la solicitud incorrecta.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Solicitud incorrecta", ex.getMessage()));
    }

    /**
     * Maneja las excepciones MethodArgumentNotValidException.
     * Se lanza cuando la validación de los argumentos de un método controlado por anotaciones falla.
     *
     * @param ex Excepción con detalles de validación de campos.
     * @return ResponseEntity con estado 400 BAD REQUEST y lista de errores de validación por campo.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errores = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, String> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("message", fieldError.getDefaultMessage());
            errores.add(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Existen errores en la petición", errores));
    }

    /**
     * Maneja todas las excepciones no controladas que no tienen un handler específico.
     * Es un catch-all para evitar que errores no previstos causen una respuesta sin formato.
     *
     * @param exception Excepción no controlada.
     * @return ResponseEntity con estado 500 INTERNAL SERVER ERROR y mensaje de error genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllUncaughtException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor", exception.getMessage()));
    }
}
