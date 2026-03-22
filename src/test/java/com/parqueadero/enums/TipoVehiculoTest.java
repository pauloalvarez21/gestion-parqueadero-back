package com.parqueadero.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoVehiculoTest {

    @Test
    void should_HaveFourValues() {
        // Arrange & Act
        TipoVehiculo[] values = TipoVehiculo.values();

        // Assert
        assertNotNull(values);
        assertEquals(4, values.length);
    }

    @Test
    void should_ReturnCarro() {
        // Arrange & Act
        TipoVehiculo tipo = TipoVehiculo.CARRO;

        // Assert
        assertNotNull(tipo);
        assertEquals("CARRO", tipo.name());
        assertEquals(0, tipo.ordinal());
    }

    @Test
    void should_ReturnMoto() {
        // Arrange & Act
        TipoVehiculo tipo = TipoVehiculo.MOTO;

        // Assert
        assertNotNull(tipo);
        assertEquals("MOTO", tipo.name());
        assertEquals(1, tipo.ordinal());
    }

    @Test
    void should_ReturnCamion() {
        // Arrange & Act
        TipoVehiculo tipo = TipoVehiculo.CAMION;

        // Assert
        assertNotNull(tipo);
        assertEquals("CAMION", tipo.name());
        assertEquals(2, tipo.ordinal());
    }

    @Test
    void should_ReturnBicicleta() {
        // Arrange & Act
        TipoVehiculo tipo = TipoVehiculo.BICICLETA;

        // Assert
        assertNotNull(tipo);
        assertEquals("BICICLETA", tipo.name());
        assertEquals(3, tipo.ordinal());
    }

    @Test
    void valueOf_deberiaRetornarTipoValido() {
        // Arrange & Act
        TipoVehiculo tipo1 = TipoVehiculo.valueOf("CARRO");
        TipoVehiculo tipo2 = TipoVehiculo.valueOf("MOTO");
        TipoVehiculo tipo3 = TipoVehiculo.valueOf("CAMION");
        TipoVehiculo tipo4 = TipoVehiculo.valueOf("BICICLETA");

        // Assert
        assertEquals(TipoVehiculo.CARRO, tipo1);
        assertEquals(TipoVehiculo.MOTO, tipo2);
        assertEquals(TipoVehiculo.CAMION, tipo3);
        assertEquals(TipoVehiculo.BICICLETA, tipo4);
    }

    @Test
    void values_deberiaRetornarArrayConTodosLosTipos() {
        // Arrange & Act
        TipoVehiculo[] values = TipoVehiculo.values();

        // Assert
        assertArrayEquals(
            new TipoVehiculo[]{TipoVehiculo.CARRO, TipoVehiculo.MOTO, TipoVehiculo.CAMION, TipoVehiculo.BICICLETA},
            values
        );
    }

    @Test
    void should_AllValuesAreUnique() {
        // Arrange
        TipoVehiculo[] values = TipoVehiculo.values();

        // Act & Assert
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    void toString_deberiaRetornarNombreDelTipo() {
        // Arrange & Act
        String carro = TipoVehiculo.CARRO.toString();
        String moto = TipoVehiculo.MOTO.toString();

        // Assert
        assertEquals("CARRO", carro);
        assertEquals("MOTO", moto);
    }

    @Test
    void should_OrdinalValuesAreSequential() {
        // Arrange & Act
        TipoVehiculo[] values = TipoVehiculo.values();

        // Assert
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    @Test
    void valueOf_deberiaLanzarExcepcionParaValorInvalido() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            TipoVehiculo.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_deberiaSerCaseSensitive() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            TipoVehiculo.valueOf("carro");
        });
    }

    @Test
    void should_TiposConMotor() {
        // Arrange
        TipoVehiculo carro = TipoVehiculo.CARRO;
        TipoVehiculo moto = TipoVehiculo.MOTO;
        TipoVehiculo camion = TipoVehiculo.CAMION;

        // Assert
        assertNotEquals(TipoVehiculo.BICICLETA, carro);
        assertNotEquals(TipoVehiculo.BICICLETA, moto);
        assertNotEquals(TipoVehiculo.BICICLETA, camion);
    }

    @Test
    void should_BicicletaEsUnico() {
        // Arrange
        TipoVehiculo bicicleta = TipoVehiculo.BICICLETA;

        // Assert
        assertNotEquals(TipoVehiculo.CARRO, bicicleta);
        assertNotEquals(TipoVehiculo.MOTO, bicicleta);
        assertNotEquals(TipoVehiculo.CAMION, bicicleta);
        assertEquals("BICICLETA", bicicleta.name());
    }

    @Test
    void should_CarroYCamionSonDiferentes() {
        // Arrange
        TipoVehiculo carro = TipoVehiculo.CARRO;
        TipoVehiculo camion = TipoVehiculo.CAMION;

        // Assert
        assertNotEquals(carro, camion);
        assertEquals(0, carro.ordinal());
        assertEquals(2, camion.ordinal());
    }
}
