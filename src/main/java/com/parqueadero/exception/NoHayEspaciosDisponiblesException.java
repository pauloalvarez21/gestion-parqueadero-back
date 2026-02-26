package com.parqueadero.exception;

public class NoHayEspaciosDisponiblesException extends RuntimeException {
    public NoHayEspaciosDisponiblesException(String message) {
        super(message);
    }
}
