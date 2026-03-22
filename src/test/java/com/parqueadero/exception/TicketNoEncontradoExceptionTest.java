package com.parqueadero.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketNoEncontradoExceptionTest {

    @Test
    void constructor_deberiaCrearExcepcionConMensaje() {
        // Arrange
        String mensaje = "Ticket no encontrado: TKT-001";

        // Act
        TicketNoEncontradoException exception = new TicketNoEncontradoException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeNull() {
        // Act
        TicketNoEncontradoException exception = new TicketNoEncontradoException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void excepcion_deberiaSerLanzada() {
        // Arrange & Act & Assert
        assertThrows(TicketNoEncontradoException.class, () -> {
            throw new TicketNoEncontradoException("Ticket no encontrado");
        });
    }

    @Test
    void excepcion_deberiaHeredarDeRuntimeException() {
        // Arrange
        TicketNoEncontradoException exception = new TicketNoEncontradoException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }
}
