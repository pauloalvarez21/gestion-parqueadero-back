package com.parqueadero.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistorialTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        Historial historial = new Historial();

        // Assert
        assertNotNull(historial);
        assertNull(historial.getId());
        assertNull(historial.getPlacaVehiculo());
        assertNull(historial.getCodigoEspacio());
        assertNull(historial.getHoraEntrada());
        assertNull(historial.getHoraSalida());
        assertNull(historial.getDuracionMinutos());
        assertNull(historial.getValorTotal());
        assertNull(historial.getFechaRegistro());
        assertNull(historial.getCreadoPor());
        assertNull(historial.getFinalizadoPor());
        assertNull(historial.getNumeroFactura());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        Historial historial = Historial.builder()
                .id(1L)
                .placaVehiculo("ABC-123")
                .codigoEspacio("P-001")
                .horaEntrada(ahora)
                .horaSalida(ahora.plusHours(2))
                .duracionMinutos(120L)
                .valorTotal(new BigDecimal("6000.00"))
                .fechaRegistro(ahora)
                .creadoPor("admin")
                .finalizadoPor("operador1")
                .numeroFactura("FC-001")
                .build();

        // Assert
        assertNotNull(historial);
        assertEquals(1L, historial.getId());
        assertEquals("ABC-123", historial.getPlacaVehiculo());
        assertEquals("P-001", historial.getCodigoEspacio());
        assertEquals(120L, historial.getDuracionMinutos());
        assertEquals(new BigDecimal("6000.00"), historial.getValorTotal());
        assertEquals("FC-001", historial.getNumeroFactura());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        Historial historial = new Historial(
                1L,
                "XYZ-789",
                "P-002",
                ahora,
                ahora.plusHours(1),
                60L,
                new BigDecimal("3000.00"),
                ahora,
                "operador2",
                "operador2",
                "FC-002"
        );

        // Assert
        assertNotNull(historial);
        assertEquals("XYZ-789", historial.getPlacaVehiculo());
        assertEquals("P-002", historial.getCodigoEspacio());
        assertEquals(60L, historial.getDuracionMinutos());
        assertEquals(new BigDecimal("3000.00"), historial.getValorTotal());
    }

    @Test
    void prePersist_deberiaEstablecerFechaRegistro() {
        // Arrange
        Historial historial = Historial.builder()
                .placaVehiculo("TEST-001")
                .codigoEspacio("P-001")
                .horaEntrada(LocalDateTime.now())
                .build();

        // Act
        LocalDateTime antesDePersistir = LocalDateTime.now();
        historial.setFechaRegistro(null);
        historial.setFechaRegistro(antesDePersistir);

        // Assert
        assertNotNull(historial.getFechaRegistro());
        assertTrue(historial.getFechaRegistro().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        Historial historial1 = Historial.builder().id(1L).build();
        Historial historial2 = Historial.builder().id(1L).build();

        // Act & Assert
        assertEquals(historial1, historial2);
        assertEquals(historial1.hashCode(), historial2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        Historial historial1 = Historial.builder().id(1L).build();
        Historial historial2 = Historial.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(historial1, historial2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        Historial historial = Historial.builder()
                .id(1L)
                .placaVehiculo("ABC-123")
                .codigoEspacio("P-001")
                .valorTotal(new BigDecimal("5000.00"))
                .build();

        // Act
        String resultado = historial.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("ABC-123"));
        assertTrue(resultado.contains("P-001"));
    }

    @Test
    void horaSalida_deberiaSerPosteriorAHoraEntrada() {
        // Arrange
        LocalDateTime entrada = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime salida = entrada.plusHours(2);

        // Act
        Historial historial = Historial.builder()
                .horaEntrada(entrada)
                .horaSalida(salida)
                .build();

        // Assert
        assertNotNull(historial.getHoraEntrada());
        assertNotNull(historial.getHoraSalida());
        assertTrue(historial.getHoraSalida().isAfter(historial.getHoraEntrada()));
    }

    @Test
    void duracionMinutos_deberiaSerPositiva() {
        // Arrange & Act
        Historial historial = Historial.builder()
                .duracionMinutos(120L)
                .build();

        // Assert
        assertNotNull(historial.getDuracionMinutos());
        assertEquals(120L, historial.getDuracionMinutos());
        assertTrue(historial.getDuracionMinutos() > 0);
    }

    @Test
    void valorTotal_deberiaAceptarValoresDecimales() {
        // Arrange
        BigDecimal valor1 = new BigDecimal("0.50");
        BigDecimal valor2 = new BigDecimal("999999.99");
        BigDecimal valor3 = new BigDecimal("1234.56");

        // Act
        Historial historial1 = Historial.builder().valorTotal(valor1).build();
        Historial historial2 = Historial.builder().valorTotal(valor2).build();
        Historial historial3 = Historial.builder().valorTotal(valor3).build();

        // Assert
        assertEquals(valor1, historial1.getValorTotal());
        assertEquals(valor2, historial2.getValorTotal());
        assertEquals(valor3, historial3.getValorTotal());
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        Historial historial = Historial.builder()
                .placaVehiculo("TEST-001")
                .codigoEspacio("P-001")
                .horaEntrada(LocalDateTime.now())
                .horaSalida(null)
                .duracionMinutos(null)
                .creadoPor(null)
                .finalizadoPor(null)
                .numeroFactura(null)
                .build();

        // Assert
        assertNotNull(historial.getPlacaVehiculo());
        assertNotNull(historial.getCodigoEspacio());
        assertNotNull(historial.getHoraEntrada());
        assertNull(historial.getHoraSalida());
        assertNull(historial.getDuracionMinutos());
        assertNull(historial.getCreadoPor());
        assertNull(historial.getFinalizadoPor());
        assertNull(historial.getNumeroFactura());
    }

    @Test
    void camposOpcionales_deberianAceptarValores() {
        // Arrange & Act
        Historial historial = Historial.builder()
                .placaVehiculo("ABC-456")
                .codigoEspacio("P-002")
                .horaEntrada(LocalDateTime.now())
                .horaSalida(LocalDateTime.now().plusHours(1))
                .duracionMinutos(60L)
                .valorTotal(new BigDecimal("3000.00"))
                .creadoPor("admin")
                .finalizadoPor("operador1")
                .numeroFactura("FC-123")
                .build();

        // Assert
        assertEquals("admin", historial.getCreadoPor());
        assertEquals("operador1", historial.getFinalizadoPor());
        assertEquals("FC-123", historial.getNumeroFactura());
        assertEquals(60L, historial.getDuracionMinutos());
    }

    @Test
    void placaVehiculo_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        Historial historial1 = Historial.builder().placaVehiculo("ABC-123").build();
        Historial historial2 = Historial.builder().placaVehiculo("MNO-456").build();
        Historial historial3 = Historial.builder().placaVehiculo("XYZ789").build();

        // Assert
        assertEquals("ABC-123", historial1.getPlacaVehiculo());
        assertEquals("MNO-456", historial2.getPlacaVehiculo());
        assertEquals("XYZ789", historial3.getPlacaVehiculo());
    }

    @Test
    void codigoEspacio_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        Historial historial1 = Historial.builder().codigoEspacio("P-001").build();
        Historial historial2 = Historial.builder().codigoEspacio("ESP-A1").build();
        Historial historial3 = Historial.builder().codigoEspacio("PARQ-999").build();

        // Assert
        assertEquals("P-001", historial1.getCodigoEspacio());
        assertEquals("ESP-A1", historial2.getCodigoEspacio());
        assertEquals("PARQ-999", historial3.getCodigoEspacio());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        Historial historial = Historial.builder()
                .placaVehiculo("PARCIAL-01")
                .codigoEspacio("P-001")
                .horaEntrada(LocalDateTime.now())
                .build();

        // Assert
        assertEquals("PARCIAL-01", historial.getPlacaVehiculo());
        assertEquals("P-001", historial.getCodigoEspacio());
        assertNotNull(historial.getHoraEntrada());
        assertNull(historial.getHoraSalida());
        assertNull(historial.getValorTotal());
    }
}
