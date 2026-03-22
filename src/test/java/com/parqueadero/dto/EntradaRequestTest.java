package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntradaRequestTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        EntradaRequest request = new EntradaRequest();

        // Assert
        assertNotNull(request);
        assertNull(request.getPlaca());
        assertNull(request.getTipoVehiculo());
        assertNull(request.getTipoTarifa());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        EntradaRequest request = EntradaRequest.builder()
                .placa("ABC-123")
                .tipoVehiculo("CARRO")
                .tipoTarifa("POR_HORA")
                .build();

        // Assert
        assertNotNull(request);
        assertEquals("ABC-123", request.getPlaca());
        assertEquals("CARRO", request.getTipoVehiculo());
        assertEquals("POR_HORA", request.getTipoTarifa());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        EntradaRequest request = new EntradaRequest(
                "XYZ-789",
                "MOTO",
                "POR_MINUTO"
        );

        // Assert
        assertNotNull(request);
        assertEquals("XYZ-789", request.getPlaca());
        assertEquals("MOTO", request.getTipoVehiculo());
        assertEquals("POR_MINUTO", request.getTipoTarifa());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosValores() {
        // Arrange
        EntradaRequest request1 = EntradaRequest.builder().placa("ABC-123").build();
        EntradaRequest request2 = EntradaRequest.builder().placa("ABC-123").build();

        // Act & Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraPlacasDiferentes() {
        // Arrange
        EntradaRequest request1 = EntradaRequest.builder().placa("ABC-123").build();
        EntradaRequest request2 = EntradaRequest.builder().placa("XYZ-789").build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        EntradaRequest request = EntradaRequest.builder()
                .placa("ABC-123")
                .tipoVehiculo("CARRO")
                .tipoTarifa("POR_HORA")
                .build();

        // Act
        String resultado = request.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("ABC-123"));
        assertTrue(resultado.contains("CARRO"));
    }

    @Test
    void placa_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        EntradaRequest request1 = EntradaRequest.builder().placa("ABC-123").build();
        EntradaRequest request2 = EntradaRequest.builder().placa("MNO-456").build();
        EntradaRequest request3 = EntradaRequest.builder().placa("XYZ789").build();

        // Assert
        assertEquals("ABC-123", request1.getPlaca());
        assertEquals("MNO-456", request2.getPlaca());
        assertEquals("XYZ789", request3.getPlaca());
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        EntradaRequest request = EntradaRequest.builder()
                .placa("ABC-123")
                .tipoVehiculo(null)
                .tipoTarifa(null)
                .build();

        // Assert
        assertNotNull(request.getPlaca());
        assertNull(request.getTipoVehiculo());
        assertNull(request.getTipoTarifa());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        EntradaRequest request = EntradaRequest.builder()
                .placa("TEST-001")
                .build();

        // Assert
        assertEquals("TEST-001", request.getPlaca());
        assertNull(request.getTipoVehiculo());
        assertNull(request.getTipoTarifa());
    }
}
