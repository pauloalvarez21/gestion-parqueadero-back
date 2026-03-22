package com.parqueadero.entity;

import com.parqueadero.enums.EstadoTicket;
import com.parqueadero.enums.TipoTarifa;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        Ticket ticket = new Ticket();

        // Assert
        assertNotNull(ticket);
        assertNull(ticket.getId());
        assertNull(ticket.getCodigo());
        assertNull(ticket.getVehiculo());
        assertNull(ticket.getEspacio());
        assertNull(ticket.getHoraEntrada());
        assertNull(ticket.getHoraSalida());
        assertNull(ticket.getTipoTarifa());
        assertNull(ticket.getValorBase());
        assertNull(ticket.getValorAdicional());
        assertNull(ticket.getDescuento());
        assertNull(ticket.getValorTotal());
        assertNull(ticket.getEstado());
        assertNull(ticket.getObservaciones());
        assertNull(ticket.getFechaPago());
        assertNull(ticket.getCreadoPor());
        assertNull(ticket.getFinalizadoPor());
        assertNull(ticket.getNumeroFactura());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();
        Vehiculo vehiculo = Vehiculo.builder().placa("ABC-123").build();
        Espacio espacio = Espacio.builder().codigo("P-001").build();
        Usuario usuario = Usuario.builder().username("admin").build();

        // Act
        Ticket ticket = Ticket.builder()
                .id(1L)
                .codigo("TKT-001")
                .vehiculo(vehiculo)
                .espacio(espacio)
                .horaEntrada(ahora)
                .horaSalida(ahora.plusHours(2))
                .tipoTarifa(TipoTarifa.POR_HORA)
                .valorBase(new BigDecimal("6000.00"))
                .valorAdicional(BigDecimal.ZERO)
                .descuento(BigDecimal.ZERO)
                .valorTotal(new BigDecimal("6000.00"))
                .estado(EstadoTicket.ACTIVO)
                .observaciones("Sin observaciones")
                .fechaPago(null)
                .creadoPor(usuario)
                .finalizadoPor(null)
                .numeroFactura(null)
                .build();

        // Assert
        assertNotNull(ticket);
        assertEquals(1L, ticket.getId());
        assertEquals("TKT-001", ticket.getCodigo());
        assertEquals(vehiculo, ticket.getVehiculo());
        assertEquals(espacio, ticket.getEspacio());
        assertEquals(ahora, ticket.getHoraEntrada());
        assertEquals(TipoTarifa.POR_HORA, ticket.getTipoTarifa());
        assertEquals(EstadoTicket.ACTIVO, ticket.getEstado());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();
        Vehiculo vehiculo = Vehiculo.builder().placa("XYZ-789").build();
        Espacio espacio = Espacio.builder().codigo("P-002").build();

        // Act
        Ticket ticket = new Ticket(
                1L,
                "TKT-002",
                vehiculo,
                espacio,
                ahora,
                ahora.plusHours(1),
                TipoTarifa.POR_HORA,
                new BigDecimal("3000.00"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("3000.00"),
                EstadoTicket.PAGADO,
                "Pago en efectivo",
                ahora.plusHours(1),
                null,
                null,
                "FC-001"
        );

        // Assert
        assertNotNull(ticket);
        assertEquals("TKT-002", ticket.getCodigo());
        assertEquals(EstadoTicket.PAGADO, ticket.getEstado());
        assertEquals("FC-001", ticket.getNumeroFactura());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        Ticket ticket1 = Ticket.builder().id(1L).build();
        Ticket ticket2 = Ticket.builder().id(1L).build();

        // Act & Assert
        assertEquals(ticket1, ticket2);
        assertEquals(ticket1.hashCode(), ticket2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        Ticket ticket1 = Ticket.builder().id(1L).build();
        Ticket ticket2 = Ticket.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(ticket1, ticket2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        Ticket ticket = Ticket.builder()
                .id(1L)
                .codigo("TKT-003")
                .estado(EstadoTicket.ACTIVO)
                .valorTotal(new BigDecimal("5000.00"))
                .build();

        // Act
        String resultado = ticket.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("TKT-003"));
        assertTrue(resultado.contains("ACTIVO"));
    }

    @Test
    void builder_deberiaSoportarTodosLosEstadosTicket() {
        // Arrange & Act & Assert
        Ticket activo = Ticket.builder().estado(EstadoTicket.ACTIVO).build();
        Ticket pagado = Ticket.builder().estado(EstadoTicket.PAGADO).build();
        Ticket cancelado = Ticket.builder().estado(EstadoTicket.CANCELADO).build();

        assertEquals(EstadoTicket.ACTIVO, activo.getEstado());
        assertEquals(EstadoTicket.PAGADO, pagado.getEstado());
        assertEquals(EstadoTicket.CANCELADO, cancelado.getEstado());
    }

    @Test
    void builder_deberiaSoportarTodosLosTiposTarifa() {
        // Arrange & Act & Assert
        Ticket porMinuto = Ticket.builder().tipoTarifa(TipoTarifa.POR_MINUTO).build();
        Ticket porHora = Ticket.builder().tipoTarifa(TipoTarifa.POR_HORA).build();
        Ticket porDia = Ticket.builder().tipoTarifa(TipoTarifa.POR_DIA).build();
        Ticket porMes = Ticket.builder().tipoTarifa(TipoTarifa.POR_MES).build();
        Ticket fraccion = Ticket.builder().tipoTarifa(TipoTarifa.FRACCION).build();

        assertEquals(TipoTarifa.POR_MINUTO, porMinuto.getTipoTarifa());
        assertEquals(TipoTarifa.POR_HORA, porHora.getTipoTarifa());
        assertEquals(TipoTarifa.POR_DIA, porDia.getTipoTarifa());
        assertEquals(TipoTarifa.POR_MES, porMes.getTipoTarifa());
        assertEquals(TipoTarifa.FRACCION, fraccion.getTipoTarifa());
    }

    @Test
    void valoresMonetarios_deberiaAceptarValoresDecimales() {
        // Arrange
        BigDecimal base = new BigDecimal("10000.50");
        BigDecimal adicional = new BigDecimal("500.25");
        BigDecimal descuento = new BigDecimal("100.00");
        BigDecimal total = new BigDecimal("10400.75");

        // Act
        Ticket ticket = Ticket.builder()
                .valorBase(base)
                .valorAdicional(adicional)
                .descuento(descuento)
                .valorTotal(total)
                .build();

        // Assert
        assertEquals(base, ticket.getValorBase());
        assertEquals(adicional, ticket.getValorAdicional());
        assertEquals(descuento, ticket.getDescuento());
        assertEquals(total, ticket.getValorTotal());
    }

    @Test
    void horaSalida_deberiaSerPosteriorAHoraEntrada() {
        // Arrange
        LocalDateTime entrada = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime salida = entrada.plusHours(2);

        // Act
        Ticket ticket = Ticket.builder()
                .horaEntrada(entrada)
                .horaSalida(salida)
                .build();

        // Assert
        assertNotNull(ticket.getHoraEntrada());
        assertNotNull(ticket.getHoraSalida());
        assertTrue(ticket.getHoraSalida().isAfter(ticket.getHoraEntrada()));
    }

    @Test
    void codigo_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        Ticket ticket1 = Ticket.builder().codigo("TKT-001").build();
        Ticket ticket2 = Ticket.builder().codigo("T-2024-0001").build();
        Ticket ticket3 = Ticket.builder().codigo("PARKING-12345").build();

        // Assert
        assertEquals("TKT-001", ticket1.getCodigo());
        assertEquals("T-2024-0001", ticket2.getCodigo());
        assertEquals("PARKING-12345", ticket3.getCodigo());
    }

    @Test
    void observaciones_deberiaAceptarTextoLargo() {
        // Arrange
        String observacionLarga = "Esta es una observación de prueba que podría ser bastante larga para verificar que el campo observaciones acepta textos de hasta 500 caracteres. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

        // Act
        Ticket ticket = Ticket.builder().observaciones(observacionLarga).build();

        // Assert
        assertNotNull(ticket.getObservaciones());
        assertEquals(observacionLarga, ticket.getObservaciones());
    }
}
