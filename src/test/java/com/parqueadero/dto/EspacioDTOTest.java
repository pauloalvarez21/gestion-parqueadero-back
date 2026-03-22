package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EspacioDTOTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        EspacioDTO dto = new EspacioDTO();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getCodigo());
        assertNull(dto.getTipoVehiculoPermitido());
        assertNull(dto.getEstado());
        assertNull(dto.getTarifaBase());
        assertFalse(dto.isOcupado());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        EspacioDTO dto = EspacioDTO.builder()
                .id(1L)
                .codigo("P-001")
                .tipoVehiculoPermitido("CARRO")
                .estado("DISPONIBLE")
                .tarifaBase(new BigDecimal("5000.00"))
                .ocupado(false)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("P-001", dto.getCodigo());
        assertEquals("CARRO", dto.getTipoVehiculoPermitido());
        assertEquals("DISPONIBLE", dto.getEstado());
        assertEquals(new BigDecimal("5000.00"), dto.getTarifaBase());
        assertFalse(dto.isOcupado());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange & Act
        EspacioDTO dto = new EspacioDTO(
                1L,
                "P-002",
                "MOTO",
                "OCUPADO",
                new BigDecimal("3000.00"),
                true
        );

        // Assert
        assertNotNull(dto);
        assertEquals("P-002", dto.getCodigo());
        assertEquals("MOTO", dto.getTipoVehiculoPermitido());
        assertEquals("OCUPADO", dto.getEstado());
        assertTrue(dto.isOcupado());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        EspacioDTO dto1 = EspacioDTO.builder().id(1L).build();
        EspacioDTO dto2 = EspacioDTO.builder().id(1L).build();

        // Act & Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        EspacioDTO dto1 = EspacioDTO.builder().id(1L).build();
        EspacioDTO dto2 = EspacioDTO.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        EspacioDTO dto = EspacioDTO.builder()
                .id(1L)
                .codigo("P-001")
                .estado("DISPONIBLE")
                .ocupado(false)
                .build();

        // Act
        String resultado = dto.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("P-001"));
        assertTrue(resultado.contains("DISPONIBLE"));
    }

    @Test
    void ocupado_deberiaAceptarValoresBooleanos() {
        // Arrange & Act
        EspacioDTO disponible = EspacioDTO.builder().ocupado(false).build();
        EspacioDTO ocupado = EspacioDTO.builder().ocupado(true).build();

        // Assert
        assertFalse(disponible.isOcupado());
        assertTrue(ocupado.isOcupado());
    }

    @Test
    void tarifaBase_deberiaAceptarValoresDecimales() {
        // Arrange
        BigDecimal tarifa1 = new BigDecimal("0.50");
        BigDecimal tarifa2 = new BigDecimal("999999.99");
        BigDecimal tarifa3 = new BigDecimal("1234.56");

        // Act
        EspacioDTO dto1 = EspacioDTO.builder().tarifaBase(tarifa1).build();
        EspacioDTO dto2 = EspacioDTO.builder().tarifaBase(tarifa2).build();
        EspacioDTO dto3 = EspacioDTO.builder().tarifaBase(tarifa3).build();

        // Assert
        assertEquals(tarifa1, dto1.getTarifaBase());
        assertEquals(tarifa2, dto2.getTarifaBase());
        assertEquals(tarifa3, dto3.getTarifaBase());
    }

    @Test
    void codigo_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        EspacioDTO dto1 = EspacioDTO.builder().codigo("P-001").build();
        EspacioDTO dto2 = EspacioDTO.builder().codigo("MOTO-A1").build();
        EspacioDTO dto3 = EspacioDTO.builder().codigo("ESP-999").build();

        // Assert
        assertEquals("P-001", dto1.getCodigo());
        assertEquals("MOTO-A1", dto2.getCodigo());
        assertEquals("ESP-999", dto3.getCodigo());
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        EspacioDTO dto = EspacioDTO.builder()
                .id(1L)
                .codigo("P-001")
                .tipoVehiculoPermitido(null)
                .estado(null)
                .tarifaBase(null)
                .build();

        // Assert
        assertNotNull(dto.getId());
        assertNotNull(dto.getCodigo());
        assertNull(dto.getTipoVehiculoPermitido());
        assertNull(dto.getEstado());
        assertNull(dto.getTarifaBase());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        EspacioDTO dto = EspacioDTO.builder()
                .codigo("P-003")
                .estado("RESERVADO")
                .build();

        // Assert
        assertEquals("P-003", dto.getCodigo());
        assertEquals("RESERVADO", dto.getEstado());
        assertNull(dto.getId());
        assertNull(dto.getTarifaBase());
    }

    @Test
    void estados_deberiaAceptarTodosLosValores() {
        // Arrange & Act
        EspacioDTO disponible = EspacioDTO.builder().estado("DISPONIBLE").build();
        EspacioDTO ocupado = EspacioDTO.builder().estado("OCUPADO").build();
        EspacioDTO reservado = EspacioDTO.builder().estado("RESERVADO").build();
        EspacioDTO mantenimiento = EspacioDTO.builder().estado("MANTENIMIENTO").build();

        // Assert
        assertEquals("DISPONIBLE", disponible.getEstado());
        assertEquals("OCUPADO", ocupado.getEstado());
        assertEquals("RESERVADO", reservado.getEstado());
        assertEquals("MANTENIMIENTO", mantenimiento.getEstado());
    }
}
