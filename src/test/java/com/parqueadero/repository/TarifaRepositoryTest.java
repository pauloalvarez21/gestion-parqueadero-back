package com.parqueadero.repository;

import com.parqueadero.entity.Tarifa;
import com.parqueadero.enums.TipoTarifa;
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
    void findByTipoTarifa_deberiaRetornarTarifa_cuandoExiste() {
        // Arrange
        Tarifa tarifa = Tarifa.builder()
                .tipoTarifa(TipoTarifa.POR_MINUTO)
                .valor(BigDecimal.valueOf(100.0))
                .build();
        entityManager.persist(tarifa);
        entityManager.flush();

        // Act
        Optional<Tarifa> encontrado = tarifaRepository.findByTipoTarifa(TipoTarifa.POR_MINUTO);

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getTipoTarifa()).isEqualTo(TipoTarifa.POR_MINUTO);
    }
}
