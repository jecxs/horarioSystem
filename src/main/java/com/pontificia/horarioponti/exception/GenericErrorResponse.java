package com.pontificia.horarioponti.exception;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GenericErrorResponse<T> {
    private String error;
    private T detalle;
    private String uuid;

    public GenericErrorResponse(String error, T detalle) {
        this.error = error;
        this.detalle = detalle;
        this.uuid = UUID.randomUUID().toString();
    }
}