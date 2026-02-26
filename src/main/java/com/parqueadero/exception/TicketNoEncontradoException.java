package com.parqueadero.exception;

public class TicketNoEncontradoException extends RuntimeException {
    public TicketNoEncontradoException(String message) {
        super(message);
    }
}
