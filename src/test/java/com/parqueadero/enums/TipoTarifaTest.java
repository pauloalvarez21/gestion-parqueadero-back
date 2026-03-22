package com.parqueadero.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoTarifaTest {

    @Test
    void should_HaveFiveValues() {
        // Arrange & Act
        TipoTarifa[] values = TipoTarifa.values();

        // Assert
        assertNotNull(values);
        assertEquals(5, values.length);
    }

    @Test
    void should_ReturnPorMinuto() {
        // Arrange & Act
        TipoTarifa tarifa = TipoTarifa.POR_MINUTO;

        // Assert
        assertNotNull(tarifa);
        assertEquals("POR_MINUTO", tarifa.name());
        assertEquals(0, tarifa.ordinal());
    }

    @Test
    void should_ReturnPorHora() {
        // Arrange & Act
        TipoTarifa tarifa = TipoTarifa.POR_HORA;

        // Assert
        assertNotNull(tarifa);
        assertEquals("POR_HORA", tarifa.name());
        assertEquals(1, tarifa.ordinal());
    }

    @Test
    void should_ReturnPorDia() {
        // Arrange & Act
        TipoTarifa tarifa = TipoTarifa.POR_DIA;

        // Assert
        assertNotNull(tarifa);
        assertEquals("POR_DIA", tarifa.name());
        assertEquals(2, tarifa.ordinal());
    }

    @Test
    void should_ReturnPorMes() {
        // Arrange & Act
        TipoTarifa tarifa = TipoTarifa.POR_MES;

        // Assert
        assertNotNull(tarifa);
        assertEquals("POR_MES", tarifa.name());
        assertEquals(3, tarifa.ordinal());
    }

    @Test
    void should_ReturnFraccion() {
        // Arrange & Act
        TipoTarifa tarifa = TipoTarifa.FRACCION;

        // Assert
        assertNotNull(tarifa);
        assertEquals("FRACCION", tarifa.name());
        assertEquals(4, tarifa.ordinal());
    }

    @Test
    void valueOf_deberiaRetornarTarifaValida() {
        // Arrange & Act
        TipoTarifa tarifa1 = TipoTarifa.valueOf("POR_MINUTO");
        TipoTarifa tarifa2 = TipoTarifa.valueOf("POR_HORA");
        TipoTarifa tarifa3 = TipoTarifa.valueOf("POR_DIA");
        TipoTarifa tarifa4 = TipoTarifa.valueOf("POR_MES");
        TipoTarifa tarifa5 = TipoTarifa.valueOf("FRACCION");

        // Assert
        assertEquals(TipoTarifa.POR_MINUTO, tarifa1);
        assertEquals(TipoTarifa.POR_HORA, tarifa2);
        assertEquals(TipoTarifa.POR_DIA, tarifa3);
        assertEquals(TipoTarifa.POR_MES, tarifa4);
        assertEquals(TipoTarifa.FRACCION, tarifa5);
    }

    @Test
    void values_deberiaRetornarArrayConTodasLasTarifas() {
        // Arrange & Act
        TipoTarifa[] values = TipoTarifa.values();

        // Assert
        assertArrayEquals(
            new TipoTarifa[]{TipoTarifa.POR_MINUTO, TipoTarifa.POR_HORA, TipoTarifa.POR_DIA, TipoTarifa.POR_MES, TipoTarifa.FRACCION},
            values
        );
    }

    @Test
    void should_AllValuesAreUnique() {
        // Arrange
        TipoTarifa[] values = TipoTarifa.values();

        // Act & Assert
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    void toString_deberiaRetornarNombreDeLaTarifa() {
        // Arrange & Act
        String porMinuto = TipoTarifa.POR_MINUTO.toString();
        String porHora = TipoTarifa.POR_HORA.toString();

        // Assert
        assertEquals("POR_MINUTO", porMinuto);
        assertEquals("POR_HORA", porHora);
    }

    @Test
    void should_OrdinalValuesAreSequential() {
        // Arrange & Act
        TipoTarifa[] values = TipoTarifa.values();

        // Assert
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    @Test
    void valueOf_deberiaLanzarExcepcionParaValorInvalido() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            TipoTarifa.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_deberiaSerCaseSensitive() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            TipoTarifa.valueOf("por_minuto");
        });
    }

    @Test
    void should_TiposDeTarifaPorTiempo() {
        // Arrange
        TipoTarifa porMinuto = TipoTarifa.POR_MINUTO;
        TipoTarifa porHora = TipoTarifa.POR_HORA;
        TipoTarifa porDia = TipoTarifa.POR_DIA;
        TipoTarifa porMes = TipoTarifa.POR_MES;

        // Assert
        assertTrue(porMinuto.name().contains("POR"));
        assertTrue(porHora.name().contains("POR"));
        assertTrue(porDia.name().contains("POR"));
        assertTrue(porMes.name().contains("POR"));
    }

    @Test
    void should_FraccionEsDiferente() {
        // Arrange
        TipoTarifa fraccion = TipoTarifa.FRACCION;

        // Assert
        assertFalse(fraccion.name().contains("POR"));
        assertEquals("FRACCION", fraccion.name());
    }
}
