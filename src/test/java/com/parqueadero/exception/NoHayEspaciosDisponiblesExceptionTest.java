package com.parqueadero.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoHayEspaciosDisponiblesExceptionTest {

    @Test
    void constructor_deberiaCrearExcepcionConMensaje() {
        // Arrange
        String mensaje = "No hay espacios disponibles";

        // Act
        NoHayEspaciosDisponiblesException exception = new NoHayEspaciosDisponiblesException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeNull() {
        // Act
        NoHayEspaciosDisponiblesException exception = new NoHayEspaciosDisponiblesException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void excepcion_deberiaSerLanzada() {
        // Arrange & Act & Assert
        assertThrows(NoHayEspaciosDisponiblesException.class, () -> {
            throw new NoHayEspaciosDisponiblesException("No hay espacios");
        });
    }

    @Test
    void excepcion_deberiaHeredarDeRuntimeException() {
        // Arrange
        NoHayEspaciosDisponiblesException exception = new NoHayEspaciosDisponiblesException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }
}
