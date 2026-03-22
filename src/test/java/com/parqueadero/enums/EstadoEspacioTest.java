package com.parqueadero.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoEspacioTest {

    @Test
    void should_HaveFourValues() {
        // Arrange & Act
        EstadoEspacio[] values = EstadoEspacio.values();

        // Assert
        assertNotNull(values);
        assertEquals(4, values.length);
    }

    @Test
    void should_ReturnDisponibile() {
        // Arrange & Act
        EstadoEspacio estado = EstadoEspacio.DISPONIBLE;

        // Assert
        assertNotNull(estado);
        assertEquals("DISPONIBLE", estado.name());
        assertEquals(0, estado.ordinal());
    }

    @Test
    void should_ReturnOcupado() {
        // Arrange & Act
        EstadoEspacio estado = EstadoEspacio.OCUPADO;

        // Assert
        assertNotNull(estado);
        assertEquals("OCUPADO", estado.name());
        assertEquals(1, estado.ordinal());
    }

    @Test
    void should_ReturnReservado() {
        // Arrange & Act
        EstadoEspacio estado = EstadoEspacio.RESERVADO;

        // Assert
        assertNotNull(estado);
        assertEquals("RESERVADO", estado.name());
        assertEquals(2, estado.ordinal());
    }

    @Test
    void should_ReturnMantenimiento() {
        // Arrange & Act
        EstadoEspacio estado = EstadoEspacio.MANTENIMIENTO;

        // Assert
        assertNotNull(estado);
        assertEquals("MANTENIMIENTO", estado.name());
        assertEquals(3, estado.ordinal());
    }

    @Test
    void valueOf_deberiaRetornarEstadoValido() {
        // Arrange & Act
        EstadoEspacio estado1 = EstadoEspacio.valueOf("DISPONIBLE");
        EstadoEspacio estado2 = EstadoEspacio.valueOf("OCUPADO");
        EstadoEspacio estado3 = EstadoEspacio.valueOf("RESERVADO");
        EstadoEspacio estado4 = EstadoEspacio.valueOf("MANTENIMIENTO");

        // Assert
        assertEquals(EstadoEspacio.DISPONIBLE, estado1);
        assertEquals(EstadoEspacio.OCUPADO, estado2);
        assertEquals(EstadoEspacio.RESERVADO, estado3);
        assertEquals(EstadoEspacio.MANTENIMIENTO, estado4);
    }

    @Test
    void values_deberiaRetornarArrayConTodosLosEstados() {
        // Arrange & Act
        EstadoEspacio[] values = EstadoEspacio.values();

        // Assert
        assertArrayEquals(
            new EstadoEspacio[]{EstadoEspacio.DISPONIBLE, EstadoEspacio.OCUPADO, EstadoEspacio.RESERVADO, EstadoEspacio.MANTENIMIENTO},
            values
        );
    }

    @Test
    void should_AllValuesAreUnique() {
        // Arrange
        EstadoEspacio[] values = EstadoEspacio.values();

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
        String disponible = EstadoEspacio.DISPONIBLE.toString();
        String ocupado = EstadoEspacio.OCUPADO.toString();

        // Assert
        assertEquals("DISPONIBLE", disponible);
        assertEquals("OCUPADO", ocupado);
    }

    @Test
    void should_OrdinalValuesAreSequential() {
        // Arrange & Act
        EstadoEspacio[] values = EstadoEspacio.values();

        // Assert
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    @Test
    void valueOf_deberiaLanzarExcepcionParaValorInvalido() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoEspacio.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_deberiaSerCaseSensitive() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoEspacio.valueOf("disponible");
        });
    }
}
