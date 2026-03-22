package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PagoResponseTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        PagoResponse response = new PagoResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getCodigoTicket());
        assertNull(response.getHoraEntrada());
        assertNull(response.getHoraSalida());
        assertNull(response.getDuracionHoras());
        assertNull(response.getDuracionMinutos());
        assertNull(response.getValorBase());
        assertNull(response.getValorAdicional());
        assertNull(response.getDescuento());
        assertNull(response.getValorTotal());
        assertNull(response.getMensaje());
        assertNull(response.getCreadoPor());
        assertNull(response.getFinalizadoPor());
        assertNull(response.getNumeroFactura());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        PagoResponse response = PagoResponse.builder()
                .codigoTicket("TKT-001")
                .horaEntrada(ahora)
                .horaSalida(ahora.plusHours(2))
                .duracionHoras(2L)
                .duracionMinutos(120L)
                .valorBase(new BigDecimal("6000.00"))
                .valorAdicional(BigDecimal.ZERO)
                .descuento(BigDecimal.ZERO)
                .valorTotal(new BigDecimal("6000.00"))
                .mensaje("Pago exitoso")
                .creadoPor("admin")
                .finalizadoPor("operador1")
                .numeroFactura("FC-001")
                .build();

        // Assert
        assertNotNull(response);
        assertEquals("TKT-001", response.getCodigoTicket());
        assertEquals(2L, response.getDuracionHoras());
        assertEquals(new BigDecimal("6000.00"), response.getValorTotal());
        assertEquals("Pago exitoso", response.getMensaje());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        PagoResponse response = new PagoResponse(
                "TKT-002",
                ahora,
                ahora.plusHours(1),
                1L,
                60L,
                new BigDecimal("3000.00"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("3000.00"),
                "Pago realizado",
                "operador2",
                "operador2",
                "FC-002"
        );

        // Assert
        assertNotNull(response);
        assertEquals("TKT-002", response.getCodigoTicket());
        assertEquals(1L, response.getDuracionHoras());
        assertEquals(new BigDecimal("3000.00"), response.getValorTotal());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosValores() {
        // Arrange
        PagoResponse response1 = PagoResponse.builder().codigoTicket("TKT-001").build();
        PagoResponse response2 = PagoResponse.builder().codigoTicket("TKT-001").build();

        // Act & Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraCodigosDiferentes() {
        // Arrange
        PagoResponse response1 = PagoResponse.builder().codigoTicket("TKT-001").build();
        PagoResponse response2 = PagoResponse.builder().codigoTicket("TKT-002").build();

        // Act & Assert
        assertNotEquals(response1, response2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        PagoResponse response = PagoResponse.builder()
                .codigoTicket("TKT-001")
                .valorTotal(new BigDecimal("5000.00"))
                .mensaje("Pago exitoso")
                .build();

        // Act
        String resultado = response.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("TKT-001"));
        assertTrue(resultado.contains("5000.00"));
    }

    @Test
    void valoresMonetarios_deberiaAceptarValoresDecimales() {
        // Arrange
        BigDecimal base = new BigDecimal("10000.50");
        BigDecimal adicional = new BigDecimal("500.25");
        BigDecimal descuento = new BigDecimal("100.00");
        BigDecimal total = new BigDecimal("10400.75");

        // Act
        PagoResponse response = PagoResponse.builder()
                .valorBase(base)
                .valorAdicional(adicional)
                .descuento(descuento)
                .valorTotal(total)
                .build();

        // Assert
        assertEquals(base, response.getValorBase());
        assertEquals(adicional, response.getValorAdicional());
        assertEquals(descuento, response.getDescuento());
        assertEquals(total, response.getValorTotal());
    }

    @Test
    void duracion_deberiaAceptarValoresPositivos() {
        // Arrange & Act
        PagoResponse response = PagoResponse.builder()
                .duracionHoras(3L)
                .duracionMinutos(180L)
                .build();

        // Assert
        assertEquals(3L, response.getDuracionHoras());
        assertEquals(180L, response.getDuracionMinutos());
        assertTrue(response.getDuracionHoras() > 0);
        assertTrue(response.getDuracionMinutos() > 0);
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        PagoResponse response = PagoResponse.builder()
                .codigoTicket("TKT-003")
                .horaEntrada(null)
                .horaSalida(null)
                .mensaje(null)
                .creadoPor(null)
                .finalizadoPor(null)
                .numeroFactura(null)
                .build();

        // Assert
        assertNotNull(response.getCodigoTicket());
        assertNull(response.getHoraEntrada());
        assertNull(response.getMensaje());
        assertNull(response.getCreadoPor());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        PagoResponse response = PagoResponse.builder()
                .codigoTicket("TKT-004")
                .valorTotal(new BigDecimal("2500.00"))
                .mensaje("Parcial")
                .build();

        // Assert
        assertEquals("TKT-004", response.getCodigoTicket());
        assertEquals(new BigDecimal("2500.00"), response.getValorTotal());
        assertEquals("Parcial", response.getMensaje());
        assertNull(response.getHoraEntrada());
        assertNull(response.getHoraSalida());
    }

    @Test
    void numeroFactura_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        PagoResponse response1 = PagoResponse.builder().numeroFactura("FC-001").build();
        PagoResponse response2 = PagoResponse.builder().numeroFactura("FACT-2024-0001").build();
        PagoResponse response3 = PagoResponse.builder().numeroFactura("123456").build();

        // Assert
        assertEquals("FC-001", response1.getNumeroFactura());
        assertEquals("FACT-2024-0001", response2.getNumeroFactura());
        assertEquals("123456", response3.getNumeroFactura());
    }
}
