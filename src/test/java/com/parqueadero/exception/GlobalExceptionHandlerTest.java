package com.parqueadero.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(WebRequest.class);
    }

    @Test
    void handleVehiculoYaEstacionado_deberiaRetornar409() {
        // Arrange
        VehiculoYaEstacionadoException ex = new VehiculoYaEstacionadoException("Vehículo ya estacionado");

        // Act
        ResponseEntity<?> response = handler.handleVehiculoYaEstacionado(ex, request);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Vehículo ya estacionado", body.get("message"));
        assertEquals(409, body.get("status"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleNoHayEspacios_deberiaRetornar503() {
        // Arrange
        NoHayEspaciosDisponiblesException ex = new NoHayEspaciosDisponiblesException("No hay espacios");

        // Act
        ResponseEntity<?> response = handler.handleNoHayEspacios(ex, request);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("No hay espacios", body.get("message"));
        assertEquals(503, body.get("status"));
    }

    @Test
    void handleTicketNoEncontrado_deberiaRetornar404() {
        // Arrange
        TicketNoEncontradoException ex = new TicketNoEncontradoException("Ticket no encontrado");

        // Act
        ResponseEntity<?> response = handler.handleTicketNoEncontrado(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Ticket no encontrado", body.get("message"));
        assertEquals(404, body.get("status"));
    }

    @Test
    void handleTicketYaProcesado_deberiaRetornar400() {
        // Arrange
        TicketYaProcesadoException ex = new TicketYaProcesadoException("Ticket ya procesado");

        // Act
        ResponseEntity<?> response = handler.handleTicketYaProcesado(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Ticket ya procesado", body.get("message"));
        assertEquals(400, body.get("status"));
    }

    @Test
    void handleVehiculoNoEncontrado_deberiaRetornar404() {
        // Arrange
        VehiculoNoEncontradoException ex = new VehiculoNoEncontradoException("Vehículo no encontrado");

        // Act
        ResponseEntity<?> response = handler.handleVehiculoNoEncontrado(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Vehículo no encontrado", body.get("message"));
        assertEquals(404, body.get("status"));
    }

    @Test
    void handleIllegalArgument_deberiaRetornar400() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        // Act
        ResponseEntity<?> response = handler.handleIllegalArgument(ex, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Argumento inválido", body.get("message"));
        assertEquals(400, body.get("status"));
    }

    @Test
    void handleUsernameNotFound_deberiaRetornar404() {
        // Arrange
        UsernameNotFoundException ex = new UsernameNotFoundException("Usuario no encontrado");

        // Act
        ResponseEntity<?> response = handler.handleUsernameNotFound(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Usuario no encontrado", body.get("message"));
        assertEquals(404, body.get("status"));
    }

    @Test
    void handleNoResourceFound_deberiaRetornar404_conMensajeGenerico() {
        // Arrange
        NoResourceFoundException ex = mock(NoResourceFoundException.class);

        // Act
        ResponseEntity<?> response = handler.handleNoResourceFound(ex, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("La ruta solicitada no existe", body.get("message"));
        assertEquals(404, body.get("status"));
    }

    @Test
    void handleBadCredentials_deberiaRetornar401_conMensajeGenerico() {
        // Arrange
        BadCredentialsException ex = new BadCredentialsException("Credenciales incorrectas");

        // Act
        ResponseEntity<?> response = handler.handleBadCredentials(ex, request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Usuario o contraseña incorrectos", body.get("message"));
        assertEquals(401, body.get("status"));
    }

    @Test
    void handleGlobalException_deberiaRetornar500() {
        // Arrange
        Exception ex = new Exception("Error genérico");

        // Act
        ResponseEntity<?> response = handler.handleGlobalException(ex, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Error interno del servidor", body.get("message"));
        assertEquals(500, body.get("status"));
    }

    @Test
    void buildErrorResponse_deberiaIncluirTimestamp() {
        // Arrange
        VehiculoNoEncontradoException ex = new VehiculoNoEncontradoException("Test");

        // Act
        ResponseEntity<?> response = handler.handleVehiculoNoEncontrado(ex, request);

        // Assert
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void buildErrorResponse_deberiaIncluirTodosLosCampos() {
        // Arrange
        VehiculoNoEncontradoException ex = new VehiculoNoEncontradoException("Test");

        // Act
        ResponseEntity<?> response = handler.handleVehiculoNoEncontrado(ex, request);

        // Assert
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("message"));
        assertTrue(body.containsKey("status"));
        assertEquals(3, body.size());
    }
}
