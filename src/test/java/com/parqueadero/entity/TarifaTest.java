package com.parqueadero.entity;

import com.parqueadero.enums.TipoTarifa;
import com.parqueadero.enums.TipoVehiculo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TarifaTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        Tarifa tarifa = new Tarifa();

        // Assert
        assertNotNull(tarifa);
        assertNull(tarifa.getId());
        assertNull(tarifa.getTipoVehiculo());
        assertNull(tarifa.getTipoTarifa());
        assertNull(tarifa.getValor());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        Tarifa tarifa = Tarifa.builder()
                .id(1L)
                .tipoVehiculo(TipoVehiculo.CARRO)
                .tipoTarifa(TipoTarifa.POR_HORA)
                .valor(new BigDecimal("3000.00"))
                .build();

        // Assert
        assertNotNull(tarifa);
        assertEquals(1L, tarifa.getId());
        assertEquals(TipoVehiculo.CARRO, tarifa.getTipoVehiculo());
        assertEquals(TipoTarifa.POR_HORA, tarifa.getTipoTarifa());
        assertEquals(new BigDecimal("3000.00"), tarifa.getValor());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        Tarifa tarifa = new Tarifa(
                1L,
                TipoVehiculo.MOTO,
                TipoTarifa.POR_MINUTO,
                new BigDecimal("20.00")
        );

        // Assert
        assertNotNull(tarifa);
        assertEquals(TipoVehiculo.MOTO, tarifa.getTipoVehiculo());
        assertEquals(TipoTarifa.POR_MINUTO, tarifa.getTipoTarifa());
        assertEquals(new BigDecimal("20.00"), tarifa.getValor());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        Tarifa tarifa1 = Tarifa.builder().id(1L).build();
        Tarifa tarifa2 = Tarifa.builder().id(1L).build();

        // Act & Assert
        assertEquals(tarifa1, tarifa2);
        assertEquals(tarifa1.hashCode(), tarifa2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        Tarifa tarifa1 = Tarifa.builder().id(1L).build();
        Tarifa tarifa2 = Tarifa.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(tarifa1, tarifa2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        Tarifa tarifa = Tarifa.builder()
                .id(1L)
                .tipoVehiculo(TipoVehiculo.CARRO)
                .tipoTarifa(TipoTarifa.POR_HORA)
                .valor(new BigDecimal("3000.00"))
                .build();

        // Act
        String resultado = tarifa.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("CARRO"));
        assertTrue(resultado.contains("POR_HORA"));
    }

    @Test
    void builder_deberiaSoportarTodosLosTiposVehiculo() {
        // Arrange & Act & Assert
        Tarifa carro = Tarifa.builder().tipoVehiculo(TipoVehiculo.CARRO).build();
        Tarifa moto = Tarifa.builder().tipoVehiculo(TipoVehiculo.MOTO).build();
        Tarifa camion = Tarifa.builder().tipoVehiculo(TipoVehiculo.CAMION).build();
        Tarifa bicicleta = Tarifa.builder().tipoVehiculo(TipoVehiculo.BICICLETA).build();

        assertEquals(TipoVehiculo.CARRO, carro.getTipoVehiculo());
        assertEquals(TipoVehiculo.MOTO, moto.getTipoVehiculo());
        assertEquals(TipoVehiculo.CAMION, camion.getTipoVehiculo());
        assertEquals(TipoVehiculo.BICICLETA, bicicleta.getTipoVehiculo());
    }

    @Test
    void builder_deberiaSoportarTodosLosTiposTarifa() {
        // Arrange & Act & Assert
        Tarifa porMinuto = Tarifa.builder().tipoTarifa(TipoTarifa.POR_MINUTO).build();
        Tarifa porHora = Tarifa.builder().tipoTarifa(TipoTarifa.POR_HORA).build();
        Tarifa porDia = Tarifa.builder().tipoTarifa(TipoTarifa.POR_DIA).build();
        Tarifa porMes = Tarifa.builder().tipoTarifa(TipoTarifa.POR_MES).build();
        Tarifa fraccion = Tarifa.builder().tipoTarifa(TipoTarifa.FRACCION).build();

        assertEquals(TipoTarifa.POR_MINUTO, porMinuto.getTipoTarifa());
        assertEquals(TipoTarifa.POR_HORA, porHora.getTipoTarifa());
        assertEquals(TipoTarifa.POR_DIA, porDia.getTipoTarifa());
        assertEquals(TipoTarifa.POR_MES, porMes.getTipoTarifa());
        assertEquals(TipoTarifa.FRACCION, fraccion.getTipoTarifa());
    }

    @Test
    void valor_deberiaAceptarValoresDecimales() {
        // Arrange
        BigDecimal valor1 = new BigDecimal("0.50");
        BigDecimal valor2 = new BigDecimal("999999.99");
        BigDecimal valor3 = new BigDecimal("1234.56");

        // Act
        Tarifa tarifa1 = Tarifa.builder().valor(valor1).build();
        Tarifa tarifa2 = Tarifa.builder().valor(valor2).build();
        Tarifa tarifa3 = Tarifa.builder().valor(valor3).build();

        // Assert
        assertEquals(valor1, tarifa1.getValor());
        assertEquals(valor2, tarifa2.getValor());
        assertEquals(valor3, tarifa3.getValor());
    }

    @Test
    void combinacionesTipoVehiculoTipoTarifa_deberianSerValidas() {
        // Arrange & Act
        Tarifa carroPorHora = Tarifa.builder()
                .tipoVehiculo(TipoVehiculo.CARRO)
                .tipoTarifa(TipoTarifa.POR_HORA)
                .valor(new BigDecimal("3000.00"))
                .build();

        Tarifa motoPorMinuto = Tarifa.builder()
                .tipoVehiculo(TipoVehiculo.MOTO)
                .tipoTarifa(TipoTarifa.POR_MINUTO)
                .valor(new BigDecimal("20.00"))
                .build();

        Tarifa camionPorDia = Tarifa.builder()
                .tipoVehiculo(TipoVehiculo.CAMION)
                .tipoTarifa(TipoTarifa.POR_DIA)
                .valor(new BigDecimal("50000.00"))
                .build();

        // Assert
        assertNotNull(carroPorHora);
        assertNotNull(motoPorMinuto);
        assertNotNull(camionPorDia);
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        Tarifa tarifa = Tarifa.builder()
                .tipoVehiculo(TipoVehiculo.BICICLETA)
                .tipoTarifa(TipoTarifa.POR_HORA)
                .build();

        // Assert
        assertEquals(TipoVehiculo.BICICLETA, tarifa.getTipoVehiculo());
        assertEquals(TipoTarifa.POR_HORA, tarifa.getTipoTarifa());
        assertNull(tarifa.getId());
        assertNull(tarifa.getValor());
    }
}
