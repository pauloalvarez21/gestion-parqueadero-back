package com.parqueadero.repository;

import com.parqueadero.entity.Vehiculo;
import com.parqueadero.enums.TipoVehiculo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VehiculoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Test
    void findByPlaca_deberiaRetornarVehiculo() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca("ABC-123");
        vehiculo.setTipo(TipoVehiculo.CARRO);
        vehiculo.setMarca("Mazda");
        entityManager.persist(vehiculo);
        entityManager.flush(); // Forzar la escritura en la BD

        // Act
        Optional<Vehiculo> encontrado = vehiculoRepository.findByPlaca("ABC-123");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getMarca()).isEqualTo("Mazda");
    }

    @Test
    void existsByPlaca_deberiaRetornarTrueSiExiste() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca("XYZ-999");
        vehiculo.setTipo(TipoVehiculo.MOTO);
        entityManager.persist(vehiculo);

        // Act & Assert
        assertThat(vehiculoRepository.existsByPlaca("XYZ-999")).isTrue();
        assertThat(vehiculoRepository.existsByPlaca("AAA-000")).isFalse();
    }

    @Test
    void buscarPorPlacaParcial_deberiaEncontrarCoincidencias() {
        // Arrange
        crearVehiculo("ABC-111");
        crearVehiculo("ABC-222");
        crearVehiculo("XYZ-333");

        // Act
        List<Vehiculo> encontrados = vehiculoRepository.buscarPorPlacaParcial("ABC");

        // Assert
        assertThat(encontrados).hasSize(2);
    }

    private void crearVehiculo(String placa) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(TipoVehiculo.CARRO);
        entityManager.persist(vehiculo);
    }
}