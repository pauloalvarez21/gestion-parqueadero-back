package com.parqueadero.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoTicketTest {

    @Test
    void should_HaveThreeValues() {
        // Arrange & Act
        EstadoTicket[] values = EstadoTicket.values();

        // Assert
        assertNotNull(values);
        assertEquals(3, values.length);
    }

    @Test
    void should_ReturnActivo() {
        // Arrange & Act
        EstadoTicket estado = EstadoTicket.ACTIVO;

        // Assert
        assertNotNull(estado);
        assertEquals("ACTIVO", estado.name());
        assertEquals(0, estado.ordinal());
    }

    @Test
    void should_ReturnPagado() {
        // Arrange & Act
        EstadoTicket estado = EstadoTicket.PAGADO;

        // Assert
        assertNotNull(estado);
        assertEquals("PAGADO", estado.name());
        assertEquals(1, estado.ordinal());
    }

    @Test
    void should_ReturnCancelado() {
        // Arrange & Act
        EstadoTicket estado = EstadoTicket.CANCELADO;

        // Assert
        assertNotNull(estado);
        assertEquals("CANCELADO", estado.name());
        assertEquals(2, estado.ordinal());
    }

    @Test
    void valueOf_deberiaRetornarEstadoValido() {
        // Arrange & Act
        EstadoTicket estado1 = EstadoTicket.valueOf("ACTIVO");
        EstadoTicket estado2 = EstadoTicket.valueOf("PAGADO");
        EstadoTicket estado3 = EstadoTicket.valueOf("CANCELADO");

        // Assert
        assertEquals(EstadoTicket.ACTIVO, estado1);
        assertEquals(EstadoTicket.PAGADO, estado2);
        assertEquals(EstadoTicket.CANCELADO, estado3);
    }

    @Test
    void values_deberiaRetornarArrayConTodosLosEstados() {
        // Arrange & Act
        EstadoTicket[] values = EstadoTicket.values();

        // Assert
        assertArrayEquals(
            new EstadoTicket[]{EstadoTicket.ACTIVO, EstadoTicket.PAGADO, EstadoTicket.CANCELADO},
            values
        );
    }

    @Test
    void should_AllValuesAreUnique() {
        // Arrange
        EstadoTicket[] values = EstadoTicket.values();

        // Act & Assert
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    void toString_deberiaRetornarNombreDelEstado() {
        // Arrange & Act
        String activo = EstadoTicket.ACTIVO.toString();
        String pagado = EstadoTicket.PAGADO.toString();

        // Assert
        assertEquals("ACTIVO", activo);
        assertEquals("PAGADO", pagado);
    }

    @Test
    void should_OrdinalValuesAreSequential() {
        // Arrange & Act
        EstadoTicket[] values = EstadoTicket.values();

        // Assert
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    @Test
    void valueOf_deberiaLanzarExcepcionParaValorInvalido() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoTicket.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_deberiaSerCaseSensitive() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoTicket.valueOf("activo");
        });
    }
}
