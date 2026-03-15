package com.parqueadero.repository;

import com.parqueadero.entity.Tarifa;
import com.parqueadero.enums.TipoVehiculo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TarifaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TarifaRepository tarifaRepository;

    @Test
    void findByTipoVehiculo_deberiaRetornarTarifa_cuandoExiste() {
        // Arrange
        Tarifa tarifa = Tarifa.builder()
                .tipoVehiculo(TipoVehiculo.CARRO)
                .valor(BigDecimal.valueOf(100.0))
                .build();
        entityManager.persist(tarifa);
        entityManager.flush();

        // Act
        Optional<Tarifa> encontrado = tarifaRepository.findByTipoVehiculo(TipoVehiculo.CARRO);

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getTipoVehiculo()).isEqualTo(TipoVehiculo.CARRO);
    }
}
