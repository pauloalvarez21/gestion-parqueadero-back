package com.parqueadero.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoNoEncontradoExceptionTest {

    @Test
    void constructor_deberiaCrearExcepcionConMensaje() {
        // Arrange
        String mensaje = "Vehículo no encontrado: ABC-123";

        // Act
        VehiculoNoEncontradoException exception = new VehiculoNoEncontradoException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeNull() {
        // Act
        VehiculoNoEncontradoException exception = new VehiculoNoEncontradoException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeVacio() {
        // Arrange
        String mensaje = "";

        // Act
        VehiculoNoEncontradoException exception = new VehiculoNoEncontradoException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void excepcion_deberiaSerLanzada() {
        // Arrange & Act & Assert
        assertThrows(VehiculoNoEncontradoException.class, () -> {
            throw new VehiculoNoEncontradoException("Vehículo no encontrado");
        });
    }

    @Test
    void excepcion_deberiaHeredarDeRuntimeException() {
        // Arrange
        VehiculoNoEncontradoException exception = new VehiculoNoEncontradoException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }
}
