package com.parqueadero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ConfiguracionException extends RuntimeException {
    public ConfiguracionException(String message) {
        super(message);
    }
}