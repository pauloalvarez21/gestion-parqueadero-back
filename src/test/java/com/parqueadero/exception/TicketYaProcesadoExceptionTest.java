package com.parqueadero.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketYaProcesadoExceptionTest {

    @Test
    void constructor_deberiaCrearExcepcionConMensaje() {
        // Arrange
        String mensaje = "El ticket ya ha sido procesado";

        // Act
        TicketYaProcesadoException exception = new TicketYaProcesadoException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeNull() {
        // Act
        TicketYaProcesadoException exception = new TicketYaProcesadoException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void excepcion_deberiaSerLanzada() {
        // Arrange & Act & Assert
        assertThrows(TicketYaProcesadoException.class, () -> {
            throw new TicketYaProcesadoException("Ticket ya procesado");
        });
    }

    @Test
    void excepcion_deberiaHeredarDeRuntimeException() {
        // Arrange
        TicketYaProcesadoException exception = new TicketYaProcesadoException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }
}
