package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalidaRequestTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        SalidaRequest request = new SalidaRequest();

        // Assert
        assertNotNull(request);
        assertNull(request.getCodigoTicket());
        assertNull(request.getPlaca());
        assertNull(request.getObservaciones());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        SalidaRequest request = SalidaRequest.builder()
                .codigoTicket("TKT-001")
                .placa("ABC-123")
                .observaciones("Sin novedades")
                .build();

        // Assert
        assertNotNull(request);
        assertEquals("TKT-001", request.getCodigoTicket());
        assertEquals("ABC-123", request.getPlaca());
        assertEquals("Sin novedades", request.getObservaciones());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        SalidaRequest request = new SalidaRequest(
                "TKT-002",
                "XYZ-789",
                "Pago en efectivo"
        );

        // Assert
        assertNotNull(request);
        assertEquals("TKT-002", request.getCodigoTicket());
        assertEquals("XYZ-789", request.getPlaca());
        assertEquals("Pago en efectivo", request.getObservaciones());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosValores() {
        // Arrange
        SalidaRequest request1 = SalidaRequest.builder().codigoTicket("TKT-001").build();
        SalidaRequest request2 = SalidaRequest.builder().codigoTicket("TKT-001").build();

        // Act & Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraCodigosDiferentes() {
        // Arrange
        SalidaRequest request1 = SalidaRequest.builder().codigoTicket("TKT-001").build();
        SalidaRequest request2 = SalidaRequest.builder().codigoTicket("TKT-002").build();

        // Act & Assert
        assertNotEquals(request1, request2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        SalidaRequest request = SalidaRequest.builder()
                .codigoTicket("TKT-001")
                .placa("ABC-123")
                .build();

        // Act
        String resultado = request.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("TKT-001"));
        assertTrue(resultado.contains("ABC-123"));
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        SalidaRequest request = SalidaRequest.builder()
                .codigoTicket("TKT-001")
                .placa(null)
                .observaciones(null)
                .build();

        // Assert
        assertNotNull(request.getCodigoTicket());
        assertNull(request.getPlaca());
        assertNull(request.getObservaciones());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        SalidaRequest request = SalidaRequest.builder()
                .codigoTicket("TKT-003")
                .build();

        // Assert
        assertEquals("TKT-003", request.getCodigoTicket());
        assertNull(request.getPlaca());
        assertNull(request.getObservaciones());
    }

    @Test
    void observaciones_deberiaAceptarTextoLargo() {
        // Arrange
        String observacionLarga = "Esta es una observación de prueba que podría ser bastante larga para verificar que el campo observaciones acepta textos largos sin problemas.";

        // Act
        SalidaRequest request = SalidaRequest.builder().observaciones(observacionLarga).build();

        // Assert
        assertNotNull(request.getObservaciones());
        assertEquals(observacionLarga, request.getObservaciones());
    }
}
