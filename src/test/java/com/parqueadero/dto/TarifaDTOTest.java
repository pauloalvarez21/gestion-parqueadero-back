package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TarifaDTOTest {

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        TarifaDTO dto = TarifaDTO.builder()
                .tipoVehiculo("CARRO")
                .tipoTarifa("POR_HORA")
                .valor(new BigDecimal("3000.00"))
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals("CARRO", dto.getTipoVehiculo());
        assertEquals("POR_HORA", dto.getTipoTarifa());
        assertEquals(new BigDecimal("3000.00"), dto.getValor());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosValores() {
        // Arrange
        TarifaDTO dto1 = TarifaDTO.builder().tipoVehiculo("CARRO").tipoTarifa("POR_HORA").build();
        TarifaDTO dto2 = TarifaDTO.builder().tipoVehiculo("CARRO").tipoTarifa("POR_HORA").build();

        // Act & Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraTiposDiferentes() {
        // Arrange
        TarifaDTO dto1 = TarifaDTO.builder().tipoVehiculo("CARRO").build();
        TarifaDTO dto2 = TarifaDTO.builder().tipoVehiculo("MOTO").build();

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        TarifaDTO dto = TarifaDTO.builder()
                .tipoVehiculo("CARRO")
                .tipoTarifa("POR_HORA")
                .valor(new BigDecimal("3000.00"))
                .build();

        // Act
        String resultado = dto.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("CARRO"));
        assertTrue(resultado.contains("POR_HORA"));
    }

    @Test
    void builder_deberiaSoportarTodosLosTiposVehiculo() {
        // Arrange & Act & Assert
        TarifaDTO carro = TarifaDTO.builder().tipoVehiculo("CARRO").build();
        TarifaDTO moto = TarifaDTO.builder().tipoVehiculo("MOTO").build();
        TarifaDTO camion = TarifaDTO.builder().tipoVehiculo("CAMION").build();
        TarifaDTO bicicleta = TarifaDTO.builder().tipoVehiculo("BICICLETA").build();

        assertEquals("CARRO", carro.getTipoVehiculo());
        assertEquals("MOTO", moto.getTipoVehiculo());
        assertEquals("CAMION", camion.getTipoVehiculo());
        assertEquals("BICICLETA", bicicleta.getTipoVehiculo());
    }

    @Test
    void builder_deberiaSoportarTodosLosTiposTarifa() {
        // Arrange & Act & Assert
        TarifaDTO porMinuto = TarifaDTO.builder().tipoTarifa("POR_MINUTO").build();
        TarifaDTO porHora = TarifaDTO.builder().tipoTarifa("POR_HORA").build();
        TarifaDTO porDia = TarifaDTO.builder().tipoTarifa("POR_DIA").build();
        TarifaDTO porMes = TarifaDTO.builder().tipoTarifa("POR_MES").build();
        TarifaDTO fraccion = TarifaDTO.builder().tipoTarifa("FRACCION").build();

        assertEquals("POR_MINUTO", porMinuto.getTipoTarifa());
        assertEquals("POR_HORA", porHora.getTipoTarifa());
        assertEquals("POR_DIA", porDia.getTipoTarifa());
        assertEquals("POR_MES", porMes.getTipoTarifa());
        assertEquals("FRACCION", fraccion.getTipoTarifa());
    }

    @Test
    void valor_deberiaAceptarValoresDecimales() {
        // Arrange
        BigDecimal valor1 = new BigDecimal("0.50");
        BigDecimal valor2 = new BigDecimal("999999.99");
        BigDecimal valor3 = new BigDecimal("1234.56");

        // Act
        TarifaDTO dto1 = TarifaDTO.builder().valor(valor1).build();
        TarifaDTO dto2 = TarifaDTO.builder().valor(valor2).build();
        TarifaDTO dto3 = TarifaDTO.builder().valor(valor3).build();

        // Assert
        assertEquals(valor1, dto1.getValor());
        assertEquals(valor2, dto2.getValor());
        assertEquals(valor3, dto3.getValor());
    }

    @Test
    void combinacionesTipoVehiculoTipoTarifa_deberianSerValidas() {
        // Arrange & Act
        TarifaDTO carroPorHora = TarifaDTO.builder()
                .tipoVehiculo("CARRO")
                .tipoTarifa("POR_HORA")
                .valor(new BigDecimal("3000.00"))
                .build();

        TarifaDTO motoPorMinuto = TarifaDTO.builder()
                .tipoVehiculo("MOTO")
                .tipoTarifa("POR_MINUTO")
                .valor(new BigDecimal("20.00"))
                .build();

        // Assert
        assertNotNull(carroPorHora);
        assertNotNull(motoPorMinuto);
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        TarifaDTO dto = TarifaDTO.builder()
                .tipoVehiculo("BICICLETA")
                .build();

        // Assert
        assertEquals("BICICLETA", dto.getTipoVehiculo());
        assertNull(dto.getTipoTarifa());
        assertNull(dto.getValor());
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        TarifaDTO dto = TarifaDTO.builder()
                .tipoVehiculo("CARRO")
                .tipoTarifa(null)
                .valor(null)
                .build();

        // Assert
        assertNotNull(dto.getTipoVehiculo());
        assertNull(dto.getTipoTarifa());
        assertNull(dto.getValor());
    }
}
