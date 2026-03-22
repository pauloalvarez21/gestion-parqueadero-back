package com.parqueadero.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionExceptionTest {

    @Test
    void constructor_deberiaCrearExcepcionConMensaje() {
        // Arrange
        String mensaje = "Error de configuración";

        // Act
        ConfiguracionException exception = new ConfiguracionException(mensaje);

        // Assert
        assertNotNull(exception);
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    void constructor_deberiaCrearExcepcionConMensajeNull() {
        // Act
        ConfiguracionException exception = new ConfiguracionException(null);

        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    void excepcion_deberiaSerLanzada() {
        // Arrange & Act & Assert
        assertThrows(ConfiguracionException.class, () -> {
            throw new ConfiguracionException("Error de configuración");
        });
    }

    @Test
    void excepcion_deberiaHeredarDeRuntimeException() {
        // Arrange
        ConfiguracionException exception = new ConfiguracionException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void clase_deberiaTenerAnotacionResponseStatus() {
        // Arrange
        Class<ConfiguracionException> clazz = ConfiguracionException.class;

        // Act
        ResponseStatus responseStatus = clazz.getAnnotation(ResponseStatus.class);

        // Assert
        assertNotNull(responseStatus);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus.value());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatus.code());
    }

    @Test
    void excepcion_deberiaTenerStatus500() {
        // Arrange
        ConfiguracionException exception = new ConfiguracionException("Error interno");

        // Act & Assert
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(responseStatus);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseStatus.value().value());
    }
}
