package com.parqueadero.repository;

import com.parqueadero.entity.Espacio;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.TipoVehiculo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EspacioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EspacioRepository espacioRepository;

    @Test
    void findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc_deberiaRetornarPrimerEspacioDisponible() {
        // Arrange: Guardamos algunos espacios en la BD en memoria
        crearEspacio("E01", TipoVehiculo.MOTO, EstadoEspacio.DISPONIBLE);
        crearEspacio("E02", TipoVehiculo.CARRO, EstadoEspacio.OCUPADO); // No debería seleccionarse (Ocupado)
        crearEspacio("E03", TipoVehiculo.CARRO, EstadoEspacio.DISPONIBLE); // Candidato 1
        crearEspacio("E04", TipoVehiculo.CARRO, EstadoEspacio.DISPONIBLE); // Candidato 2

        // Act: Ejecutamos el método del repositorio
        Optional<Espacio> encontrado = espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(
                TipoVehiculo.CARRO, EstadoEspacio.DISPONIBLE);

        // Assert: Verificamos
        assertThat(encontrado).isPresent();
        // Debería ser E03 porque es el primero (por ID) que es CARRO y está DISPONIBLE
        assertThat(encontrado.get().getCodigo()).isEqualTo("E03");
    }

    @Test
    void findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc_cuandoNoHay_deberiaRetornarVacio() {
        // Arrange
        crearEspacio("E01", TipoVehiculo.MOTO, EstadoEspacio.DISPONIBLE);
        crearEspacio("E02", TipoVehiculo.CARRO, EstadoEspacio.OCUPADO);

        // Act
        Optional<Espacio> encontrado = espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(
                TipoVehiculo.CARRO, EstadoEspacio.DISPONIBLE);

        // Assert
        assertThat(encontrado).isEmpty();
    }

    private void crearEspacio(String codigo, TipoVehiculo tipo, EstadoEspacio estado) {
        Espacio espacio = new Espacio();
        espacio.setCodigo(codigo);
        espacio.setTipoVehiculoPermitido(tipo);
        espacio.setEstado(estado);
        espacio.setTarifaBase(new BigDecimal("3000"));
        entityManager.persist(espacio);
    }
}