package com.parqueadero.entity;

import com.parqueadero.enums.TipoVehiculo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        Vehiculo vehiculo = new Vehiculo();

        // Assert
        assertNotNull(vehiculo);
        assertNull(vehiculo.getId());
        assertNull(vehiculo.getPlaca());
        assertNull(vehiculo.getTipo());
        assertNull(vehiculo.getMarca());
        assertNull(vehiculo.getModelo());
        assertNull(vehiculo.getColor());
        assertNull(vehiculo.getNombrePropietario());
        assertNull(vehiculo.getTelefonoPropietario());
        assertNull(vehiculo.getFechaRegistro());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        Vehiculo vehiculo = Vehiculo.builder()
                .id(1L)
                .placa("ABC-123")
                .tipo(TipoVehiculo.CARRO)
                .marca("Toyota")
                .modelo("Corolla")
                .color("Rojo")
                .nombrePropietario("Juan Pérez")
                .telefonoPropietario("3001234567")
                .fechaRegistro(ahora)
                .build();

        // Assert
        assertNotNull(vehiculo);
        assertEquals(1L, vehiculo.getId());
        assertEquals("ABC-123", vehiculo.getPlaca());
        assertEquals(TipoVehiculo.CARRO, vehiculo.getTipo());
        assertEquals("Toyota", vehiculo.getMarca());
        assertEquals("Corolla", vehiculo.getModelo());
        assertEquals("Rojo", vehiculo.getColor());
        assertEquals("Juan Pérez", vehiculo.getNombrePropietario());
        assertEquals("3001234567", vehiculo.getTelefonoPropietario());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        Vehiculo vehiculo = new Vehiculo(
                1L,
                "XYZ-789",
                TipoVehiculo.MOTO,
                "Yamaha",
                "MT-03",
                "Azul",
                "María García",
                "3109876543",
                ahora
        );

        // Assert
        assertNotNull(vehiculo);
        assertEquals("XYZ-789", vehiculo.getPlaca());
        assertEquals(TipoVehiculo.MOTO, vehiculo.getTipo());
        assertEquals("Yamaha", vehiculo.getMarca());
        assertEquals("María García", vehiculo.getNombrePropietario());
    }

    @Test
    void prePersist_deberiaEstablecerFechaRegistro() {
        // Arrange
        Vehiculo vehiculo = Vehiculo.builder()
                .placa("TEST-001")
                .tipo(TipoVehiculo.CARRO)
                .build();

        // Act
        LocalDateTime antesDePersistir = LocalDateTime.now();
        vehiculo.setFechaRegistro(null);
        vehiculo.setFechaRegistro(antesDePersistir);

        // Assert
        assertNotNull(vehiculo.getFechaRegistro());
        assertTrue(vehiculo.getFechaRegistro().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosIds() {
        // Arrange
        Vehiculo vehiculo1 = Vehiculo.builder().id(1L).build();
        Vehiculo vehiculo2 = Vehiculo.builder().id(1L).build();

        // Act & Assert
        assertEquals(vehiculo1, vehiculo2);
        assertEquals(vehiculo1.hashCode(), vehiculo2.hashCode());
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        Vehiculo vehiculo1 = Vehiculo.builder().id(1L).build();
        Vehiculo vehiculo2 = Vehiculo.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(vehiculo1, vehiculo2);
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        Vehiculo vehiculo = Vehiculo.builder()
                .id(1L)
                .placa("ABC-123")
                .tipo(TipoVehiculo.CARRO)
                .marca("Toyota")
                .modelo("Corolla")
                .build();

        // Act
        String resultado = vehiculo.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("ABC-123"));
        assertTrue(resultado.contains("Toyota"));
        assertTrue(resultado.contains("Corolla"));
    }

    @Test
    void builder_deberiaSoportarTodosLosTiposVehiculo() {
        // Arrange & Act & Assert
        Vehiculo carro = Vehiculo.builder().tipo(TipoVehiculo.CARRO).build();
        Vehiculo moto = Vehiculo.builder().tipo(TipoVehiculo.MOTO).build();
        Vehiculo camion = Vehiculo.builder().tipo(TipoVehiculo.CAMION).build();
        Vehiculo bicicleta = Vehiculo.builder().tipo(TipoVehiculo.BICICLETA).build();

        assertEquals(TipoVehiculo.CARRO, carro.getTipo());
        assertEquals(TipoVehiculo.MOTO, moto.getTipo());
        assertEquals(TipoVehiculo.CAMION, camion.getTipo());
        assertEquals(TipoVehiculo.BICICLETA, bicicleta.getTipo());
    }

    @Test
    void placa_deberiaAceptarDiferentesFormatos() {
        // Arrange & Act
        Vehiculo vehiculo1 = Vehiculo.builder().placa("ABC-123").build();
        Vehiculo vehiculo2 = Vehiculo.builder().placa("MNO-456").build();
        Vehiculo vehiculo3 = Vehiculo.builder().placa("XYZ789").build();

        // Assert
        assertEquals("ABC-123", vehiculo1.getPlaca());
        assertEquals("MNO-456", vehiculo2.getPlaca());
        assertEquals("XYZ789", vehiculo3.getPlaca());
    }

    @Test
    void camposOpcionales_deberianAceptarNull() {
        // Arrange & Act
        Vehiculo vehiculo = Vehiculo.builder()
                .placa("TEST-002")
                .tipo(TipoVehiculo.CARRO)
                .marca(null)
                .modelo(null)
                .color(null)
                .nombrePropietario(null)
                .telefonoPropietario(null)
                .build();

        // Assert
        assertNotNull(vehiculo.getPlaca());
        assertNotNull(vehiculo.getTipo());
        assertNull(vehiculo.getMarca());
        assertNull(vehiculo.getModelo());
        assertNull(vehiculo.getColor());
        assertNull(vehiculo.getNombrePropietario());
        assertNull(vehiculo.getTelefonoPropietario());
    }

    @Test
    void camposOpcionales_deberianAceptarValores() {
        // Arrange & Act
        Vehiculo vehiculo = Vehiculo.builder()
                .placa("TEST-003")
                .tipo(TipoVehiculo.MOTO)
                .marca("Honda")
                .modelo("CBR 600")
                .color("Negro")
                .nombrePropietario("Carlos López")
                .telefonoPropietario("3151234567")
                .build();

        // Assert
        assertEquals("Honda", vehiculo.getMarca());
        assertEquals("CBR 600", vehiculo.getModelo());
        assertEquals("Negro", vehiculo.getColor());
        assertEquals("Carlos López", vehiculo.getNombrePropietario());
        assertEquals("3151234567", vehiculo.getTelefonoPropietario());
    }

    @Test
    void builder_deberiaPermitirConstruirParcialmente() {
        // Arrange & Act
        Vehiculo vehiculo = Vehiculo.builder()
                .placa("PARCIAL-01")
                .tipo(TipoVehiculo.CAMION)
                .marca("Volvo")
                .build();

        // Assert
        assertEquals("PARCIAL-01", vehiculo.getPlaca());
        assertEquals(TipoVehiculo.CAMION, vehiculo.getTipo());
        assertEquals("Volvo", vehiculo.getMarca());
        assertNull(vehiculo.getModelo());
        assertNull(vehiculo.getColor());
    }
}
