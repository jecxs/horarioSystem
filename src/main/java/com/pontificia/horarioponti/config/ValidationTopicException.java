package com.pontificia.horarioponti.config;

import lombok.Getter;

@Getter
public class ValidationTopicException extends RuntimeException {

    public ValidationTopicException(String message) {
        super(message);
    }
}