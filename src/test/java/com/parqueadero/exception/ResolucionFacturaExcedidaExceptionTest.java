package com.parqueadero.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolucionFacturaExcedidaExceptionTest {

    @Test
    void constructor_deberiaCrearExcepcionConMensaje() {
        // Arrange
        String mensaje = "Se ha excedido el límite de la resolución de factura";

        // Act
        ResolucionFacturaExcedidaException exception = new ResolucionFacturaExcedidaException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeNull() {
        // Act
        ResolucionFacturaExcedidaException exception = new ResolucionFacturaExcedidaException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void excepcion_deberiaSerLanzada() {
        // Arrange & Act & Assert
        assertThrows(ResolucionFacturaExcedidaException.class, () -> {
            throw new ResolucionFacturaExcedidaException("Resolución excedida");
        });
    }

    @Test
    void excepcion_deberiaHeredarDeRuntimeException() {
        // Arrange
        ResolucionFacturaExcedidaException exception = new ResolucionFacturaExcedidaException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }
}
