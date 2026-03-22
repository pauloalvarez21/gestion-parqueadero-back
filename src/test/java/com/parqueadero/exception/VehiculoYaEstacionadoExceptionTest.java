package com.parqueadero.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoYaEstacionadoExceptionTest {

    @Test
    void constructor_deberiaCrearExcepcionConMensaje() {
        // Arrange
        String mensaje = "El vehículo ya está estacionado";

        // Act
        VehiculoYaEstacionadoException exception = new VehiculoYaEstacionadoException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeNull() {
        // Act
        VehiculoYaEstacionadoException exception = new VehiculoYaEstacionadoException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void excepcion_deberiaSerLanzada() {
        // Arrange & Act & Assert
        assertThrows(VehiculoYaEstacionadoException.class, () -> {
            throw new VehiculoYaEstacionadoException("Vehículo ya estacionado");
        });
    }

    @Test
    void excepcion_deberiaHeredarDeRuntimeException() {
        // Arrange
        VehiculoYaEstacionadoException exception = new VehiculoYaEstacionadoException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }
}
