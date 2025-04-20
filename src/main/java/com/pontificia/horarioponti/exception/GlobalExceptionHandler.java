package com.pontificia.horarioponti.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GenericErrorResponse<String>> handleEntityNotFoundException(
            EntityNotFoundException ex) {

        GenericErrorResponse<String> respuesta = new GenericErrorResponse<>(
                "Recurso no encontrado",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericErrorResponse<String>> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        GenericErrorResponse<String> respuesta = new GenericErrorResponse<>(
                "Solicitud incorrecta",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericErrorResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errores.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        GenericErrorResponse<Map<String, String>> respuesta = new GenericErrorResponse<>(
                "Existen errores en la petición",
                errores
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericErrorResponse<String>> handleAllUncaughtException(
            Exception exception) {

        GenericErrorResponse<String> respuesta = new GenericErrorResponse<>(
                "Error interno del servidor",
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
