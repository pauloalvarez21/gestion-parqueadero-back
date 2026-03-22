package com.parqueadero.entity;

import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.TipoVehiculo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EspacioTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        Espacio espacio = new Espacio();

        // Assert
        assertNotNull(espacio);
        assertNull(espacio.getId());
        assertNull(espacio.getCodigo());
        assertNull(espacio.getTipoVehiculoPermitido());
        assertNull(espacio.getEstado());
        assertNull(espacio.getTarifaBase());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        Espacio espacio = Espacio.builder()
                .id(1L)
                .codigo("P-001")
                .tipoVehiculoPermitido(TipoVehiculo.CARRO)
                .estado(EstadoEspacio.DISPONIBLE)
                .tarifaBase(new BigDecimal("5000.00"))
                .build();

        // Assert
        assertNotNull(espacio);
        assertEquals(1L, espacio.getId());
        assertEquals("P-001", espacio.getCodigo());
        assertEquals(TipoVehiculo.CARRO, espacio.getTipoVehiculoPermitido());
        assertEquals(EstadoEspacio.DISPONIBLE, espacio.getEstado());
        assertEquals(new BigDecimal("5000.00"), espacio.getTarifaBase());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        Espacio espacio = new Espacio(
                1L,
                "P-002",
                TipoVehiculo.MOTO,
                EstadoEspacio.OCUPADO,
                new BigDecimal("3000.00")
        );

        // Assert
        assertNotNull(espacio);
        assertEquals(1L, espacio.getId());
        assertEquals("P-002", espacio.getCodigo());
        assertEquals(TipoVehiculo.MOTO, espacio.getTipoVehiculoPermitido());
        assertEquals(EstadoEspacio.OCUPADO, espacio.getEstado());
        assertEquals(new BigDecimal("3000.00"), espacio.getTarifaBase());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        Espacio espacio1 = Espacio.builder().id(1L).build();
        Espacio espacio2 = Espacio.builder().id(1L).build();

        // Act & Assert
        assertEquals(espacio1, espacio2);
        assertEquals(espacio1.hashCode(), espacio2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        Espacio espacio1 = Espacio.builder().id(1L).build();
        Espacio espacio2 = Espacio.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(espacio1, espacio2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        Espacio espacio = Espacio.builder()
                .id(1L)
                .codigo("P-001")
                .tipoVehiculoPermitido(TipoVehiculo.CARRO)
                .estado(EstadoEspacio.DISPONIBLE)
                .build();

        // Act
        String resultado = espacio.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("P-001"));
        assertTrue(resultado.contains("CARRO"));
        assertTrue(resultado.contains("DISPONIBLE"));
    }

    @Test
    void builder_deberiaSoportarTodosLosEstadosEspacio() {
        // Arrange & Act & Assert
        Espacio disponible = Espacio.builder().estado(EstadoEspacio.DISPONIBLE).build();
        Espacio ocupado = Espacio.builder().estado(EstadoEspacio.OCUPADO).build();
        Espacio reservado = Espacio.builder().estado(EstadoEspacio.RESERVADO).build();
        Espacio mantenimiento = Espacio.builder().estado(EstadoEspacio.MANTENIMIENTO).build();

        assertEquals(EstadoEspacio.DISPONIBLE, disponible.getEstado());
        assertEquals(EstadoEspacio.OCUPADO, ocupado.getEstado());
        assertEquals(EstadoEspacio.RESERVADO, reservado.getEstado());
        assertEquals(EstadoEspacio.MANTENIMIENTO, mantenimiento.getEstado());
    }

    @Test
    void builder_deberiaSoportarTodosLosTiposVehiculo() {
        // Arrange & Act & Assert
        Espacio carro = Espacio.builder().tipoVehiculoPermitido(TipoVehiculo.CARRO).build();
        Espacio moto = Espacio.builder().tipoVehiculoPermitido(TipoVehiculo.MOTO).build();
        Espacio camion = Espacio.builder().tipoVehiculoPermitido(TipoVehiculo.CAMION).build();
        Espacio bicicleta = Espacio.builder().tipoVehiculoPermitido(TipoVehiculo.BICICLETA).build();

        assertEquals(TipoVehiculo.CARRO, carro.getTipoVehiculoPermitido());
        assertEquals(TipoVehiculo.MOTO, moto.getTipoVehiculoPermitido());
        assertEquals(TipoVehiculo.CAMION, camion.getTipoVehiculoPermitido());
        assertEquals(TipoVehiculo.BICICLETA, bicicleta.getTipoVehiculoPermitido());
    }

    @Test
    void tarifaBase_deberiaAceptarValoresDecimales() {
        // Arrange
        BigDecimal tarifa = new BigDecimal("12345.67");

        // Act
        Espacio espacio = Espacio.builder().tarifaBase(tarifa).build();

        // Assert
        assertNotNull(espacio.getTarifaBase());
        assertEquals(tarifa, espacio.getTarifaBase());
    }

    @Test
    void codigo_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        Espacio espacio1 = Espacio.builder().codigo("P-001").build();
        Espacio espacio2 = Espacio.builder().codigo("MOTO-A1").build();
        Espacio espacio3 = Espacio.builder().codigo("ESP-999").build();

        // Assert
        assertEquals("P-001", espacio1.getCodigo());
        assertEquals("MOTO-A1", espacio2.getCodigo());
        assertEquals("ESP-999", espacio3.getCodigo());
    }
}
