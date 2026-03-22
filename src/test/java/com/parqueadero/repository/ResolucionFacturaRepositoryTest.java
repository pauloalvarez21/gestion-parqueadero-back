package com.parqueadero.repository;

import com.parqueadero.entity.ResolucionFactura;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ResolucionFacturaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResolucionFacturaRepository resolucionFacturaRepository;

    @Test
    void findActiveResolution_deberiaRetornarResolucion_cuandoHayResolucionActiva() {
        // Arrange
        ResolucionFactura resolucionActiva = ResolucionFactura.builder()
                .numeroResolucion("RES-001")
                .fechaResolucion(LocalDate.of(2024, 1, 15))
                .prefijo("FC")
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(100L)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .activa(true)
                .mensajePiePagina("Gracias por su compra")
                .nitEmpresa("900123456-1")
                .build();
        entityManager.persist(resolucionActiva);
        entityManager.flush();

        // Act
        Optional<ResolucionFactura> encontrado = resolucionFacturaRepository.findActiveResolution();

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNumeroResolucion()).isEqualTo("RES-001");
        assertThat(encontrado.get().isActiva()).isTrue();
    }

    @Test
    void findActiveResolution_deberiaRetornarVacio_cuandoNoHayResolucionActiva() {
        // Arrange
        ResolucionFactura resolucionInactiva = ResolucionFactura.builder()
                .numeroResolucion("RES-002")
                .fechaResolucion(LocalDate.of(2024, 1, 15))
                .prefijo("FC")
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(100L)
                .fechaInicio(LocalDate.now().minusYears(2))
                .fechaFin(LocalDate.now().minusYears(1)) // Fecha ya vencida
                .activa(false)
                .mensajePiePagina("Gracias por su compra")
                .nitEmpresa("900123456-1")
                .build();
        entityManager.persist(resolucionInactiva);
        entityManager.flush();

        // Act
        Optional<ResolucionFactura> encontrado = resolucionFacturaRepository.findActiveResolution();

        // Assert
        assertThat(encontrado).isEmpty();
    }

    @Test
    void findActiveResolution_deberiaRetornarVacio_cuandoFechaFinEsAnteriorAHoy() {
        // Arrange
        ResolucionFactura resolucionVencida = ResolucionFactura.builder()
                .numeroResolucion("RES-003")
                .fechaResolucion(LocalDate.of(2023, 1, 15))
                .prefijo("FC")
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(500L)
                .fechaInicio(LocalDate.now().minusYears(2))
                .fechaFin(LocalDate.now().minusDays(1)) // Fecha vencida
                .activa(true) // Activa pero vencida
                .mensajePiePagina("Gracias por su compra")
                .nitEmpresa("900123456-1")
                .build();
        entityManager.persist(resolucionVencida);
        entityManager.flush();

        // Act
        Optional<ResolucionFactura> encontrado = resolucionFacturaRepository.findActiveResolution();

        // Assert
        assertThat(encontrado).isEmpty();
    }

    @Test
    void findByNumeroResolucion_deberiaRetornarResolucion_cuandoExiste() {
        // Arrange
        String numeroResolucion = "RES-004";
        ResolucionFactura resolucion = ResolucionFactura.builder()
                .numeroResolucion(numeroResolucion)
                .fechaResolucion(LocalDate.of(2024, 1, 15))
                .prefijo("FC")
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(100L)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .activa(true)
                .mensajePiePagina("Gracias por su compra")
                .nitEmpresa("900123456-1")
                .build();
        entityManager.persist(resolucion);
        entityManager.flush();

        // Act
        Optional<ResolucionFactura> encontrado = resolucionFacturaRepository.findByNumeroResolucion(numeroResolucion);

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNumeroResolucion()).isEqualTo(numeroResolucion);
    }

    @Test
    void findByNumeroResolucion_deberiaRetornarVacio_cuandoNoExiste() {
        // Arrange
        String numeroInexistente = "RES-INEXISTENTE";

        // Act
        Optional<ResolucionFactura> encontrado = resolucionFacturaRepository.findByNumeroResolucion(numeroInexistente);

        // Assert
        assertThat(encontrado).isEmpty();
    }

    @Test
    void save_deberiaGuardarResolucionFactura() {
        // Arrange
        ResolucionFactura resolucion = ResolucionFactura.builder()
                .numeroResolucion("RES-005")
                .fechaResolucion(LocalDate.of(2024, 1, 15))
                .prefijo("FC")
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(1L)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .activa(true)
                .mensajePiePagina("Gracias por su compra")
                .nitEmpresa("900123456-1")
                .build();

        // Act
        ResolucionFactura guardada = resolucionFacturaRepository.save(resolucion);
        entityManager.flush();

        // Assert
        assertThat(guardada).isNotNull();
        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getNumeroResolucion()).isEqualTo("RES-005");
        assertThat(guardada.getCreadoEn()).isNotNull();
    }

    @Test
    void findAll_deberiaRetornarTodasLasResoluciones() {
        // Arrange
        ResolucionFactura resolucion1 = ResolucionFactura.builder()
                .numeroResolucion("RES-006")
                .fechaResolucion(LocalDate.of(2024, 1, 15))
                .prefijo("FC")
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(1L)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .activa(true)
                .build();

        ResolucionFactura resolucion2 = ResolucionFactura.builder()
                .numeroResolucion("RES-007")
                .fechaResolucion(LocalDate.of(2024, 2, 15))
                .prefijo("FE")
                .numeroDesde(1L)
                .numeroHasta(500L)
                .numeroActual(50L)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .activa(false)
                .build();

        entityManager.persist(resolucion1);
        entityManager.persist(resolucion2);
        entityManager.flush();

        // Act
        Iterable<ResolucionFactura> resultado = resolucionFacturaRepository.findAll();

        // Assert
        assertThat(resultado).isNotEmpty();
        assertThat(resultado).hasSize(2);
    }
}
