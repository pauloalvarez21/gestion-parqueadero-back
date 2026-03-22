package com.parqueadero.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoDTOTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        VehiculoDTO dto = new VehiculoDTO();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getPlaca());
        assertNull(dto.getTipo());
        assertNull(dto.getMarca());
        assertNull(dto.getModelo());
        assertNull(dto.getColor());
        assertNull(dto.getNombrePropietario());
        assertNull(dto.getTelefonoPropietario());
        assertNull(dto.getFechaRegistro());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        VehiculoDTO dto = VehiculoDTO.builder()
                .id(1L)
                .placa("ABC-123")
                .tipo("CARRO")
                .marca("Toyota")
                .modelo("Corolla")
                .color("Rojo")
                .nombrePropietario("Juan Pérez")
                .telefonoPropietario("3001234567")
                .fechaRegistro(ahora)
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("ABC-123", dto.getPlaca());
        assertEquals("Toyota", dto.getMarca());
        assertEquals("Juan Pérez", dto.getNombrePropietario());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        VehiculoDTO dto = new VehiculoDTO(
                1L,
                "XYZ-789",
                "MOTO",
                "Yamaha",
                "MT-03",
                "Azul",
                "María García",
                "3109876543",
                ahora
        );

        // Assert
        assertNotNull(dto);
        assertEquals("XYZ-789", dto.getPlaca());
        assertEquals("MOTO", dto.getTipo());
        assertEquals("Yamaha", dto.getMarca());
        assertEquals("María García", dto.getNombrePropietario());
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        VehiculoDTO dto1 = VehiculoDTO.builder().id(1L).build();
        VehiculoDTO dto2 = VehiculoDTO.builder().id(1L).build();

        // Act & Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        VehiculoDTO dto1 = VehiculoDTO.builder().id(1L).build();
        VehiculoDTO dto2 = VehiculoDTO.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        VehiculoDTO dto = VehiculoDTO.builder()
                .id(1L)
                .placa("ABC-123")
                .tipo("CARRO")
                .marca("Toyota")
                .build();

        // Act
        String resultado = dto.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("ABC-123"));
        assertTrue(resultado.contains("Toyota"));
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        VehiculoDTO dto = VehiculoDTO.builder()
                .id(1L)
                .placa("ABC-123")
                .tipo("CARRO")
                .marca(null)
                .modelo(null)
                .color(null)
                .nombrePropietario(null)
                .telefonoPropietario(null)
                .fechaRegistro(null)
                .build();

        // Assert
        assertNotNull(dto.getPlaca());
        assertNotNull(dto.getTipo());
        assertNull(dto.getMarca());
        assertNull(dto.getNombrePropietario());
        assertNull(dto.getTelefonoPropietario());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        VehiculoDTO dto = VehiculoDTO.builder()
                .placa("TEST-001")
                .tipo("MOTO")
                .marca("Honda")
                .build();

        // Assert
        assertEquals("TEST-001", dto.getPlaca());
        assertEquals("MOTO", dto.getTipo());
        assertEquals("Honda", dto.getMarca());
        assertNull(dto.getModelo());
        assertNull(dto.getColor());
    }

    @Test
    void placa_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        VehiculoDTO dto1 = VehiculoDTO.builder().placa("ABC-123").build();
        VehiculoDTO dto2 = VehiculoDTO.builder().placa("MNO-456").build();
        VehiculoDTO dto3 = VehiculoDTO.builder().placa("XYZ789").build();

        // Assert
        assertEquals("ABC-123", dto1.getPlaca());
        assertEquals("MNO-456", dto2.getPlaca());
        assertEquals("XYZ789", dto3.getPlaca());
    }

    @Test
    void tipos_deberiaAceptarTodosLosValores() {
        // Arrange & Act
        VehiculoDTO carro = VehiculoDTO.builder().tipo("CARRO").build();
        VehiculoDTO moto = VehiculoDTO.builder().tipo("MOTO").build();
        VehiculoDTO camion = VehiculoDTO.builder().tipo("CAMION").build();
        VehiculoDTO bicicleta = VehiculoDTO.builder().tipo("BICICLETA").build();

        // Assert
        assertEquals("CARRO", carro.getTipo());
        assertEquals("MOTO", moto.getTipo());
        assertEquals("CAMION", camion.getTipo());
        assertEquals("BICICLETA", bicicleta.getTipo());
    }

    @Test
    void telefonoPropietario_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        VehiculoDTO dto1 = VehiculoDTO.builder().telefonoPropietario("3001234567").build();
        VehiculoDTO dto2 = VehiculoDTO.builder().telefonoPropietario("+57 310 123 4567").build();
        VehiculoDTO dto3 = VehiculoDTO.builder().telefonoPropietario("601 123 4567").build();

        // Assert
        assertEquals("3001234567", dto1.getTelefonoPropietario());
        assertEquals("+57 310 123 4567", dto2.getTelefonoPropietario());
        assertEquals("601 123 4567", dto3.getTelefonoPropietario());
    }

    @Test
    void camposTexto_deberianAceptarValoresVacios() {
        // Arrange & Act
        VehiculoDTO dto = VehiculoDTO.builder()
                .placa("ABC-123")
                .marca("")
                .modelo("")
                .color("")
                .build();

        // Assert
        assertEquals("", dto.getMarca());
        assertEquals("", dto.getModelo());
        assertEquals("", dto.getColor());
    }
}
