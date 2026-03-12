package com.parqueadero.repository;

import com.parqueadero.entity.Historial;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HistorialRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HistorialRepository historialRepository;

    @Test
    void findByPlacaVehiculo_deberiaRetornarHistorial() {
        // Arrange
        Historial historial = Historial.builder()
                .placaVehiculo("ABC-123")
                .codigoEspacio("A1")
                .horaEntrada(LocalDateTime.now().minusHours(2))
                .horaSalida(LocalDateTime.now())
                .valorTotal(BigDecimal.valueOf(5000))
                .build();
        entityManager.persist(historial);
        entityManager.flush();

        // Act
        List<Historial> encontrados = historialRepository.findByPlacaVehiculo("ABC-123");

        // Assert
        assertThat(encontrados).hasSize(1);
        assertThat(encontrados.get(0).getPlacaVehiculo()).isEqualTo("ABC-123");
    }

    @Test
    void findByFechaRegistroBetween_deberiaRetornarRegistrosEnRango() {
        // Arrange
        Historial historial = Historial.builder()
                .placaVehiculo("ABC-123")
                .codigoEspacio("A1")
                .horaEntrada(LocalDateTime.now())
                .build();
        entityManager.persist(historial);
        entityManager.flush();

        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now().plusDays(1);

        // Act
        List<Historial> encontrados = historialRepository.findByFechaRegistroBetween(inicio, fin);

        // Assert
        assertThat(encontrados).isNotEmpty();
    }
}
