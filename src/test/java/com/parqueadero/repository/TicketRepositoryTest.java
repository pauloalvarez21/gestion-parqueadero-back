package com.parqueadero.repository;

import com.parqueadero.entity.Espacio;
import com.parqueadero.entity.Ticket;
import com.parqueadero.entity.Vehiculo;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.EstadoTicket;

import com.parqueadero.enums.TipoVehiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    private Vehiculo vehiculo;
    private Espacio espacio;

    @BeforeEach
    void setUp() {
        vehiculo = Vehiculo.builder()
                .placa("ABC-123")
                .tipo(TipoVehiculo.CARRO)
                .marca("Toyota")
                .build();
        entityManager.persist(vehiculo);

        espacio = Espacio.builder()
                .codigo("A1")
                .tipoVehiculoPermitido(TipoVehiculo.CARRO)
                .estado(EstadoEspacio.OCUPADO)
                .tarifaBase(BigDecimal.valueOf(2000))
                .build();
        entityManager.persist(espacio);
    }

    @Test
    void findByCodigo_deberiaRetornarTicket() {
        Ticket ticket = Ticket.builder()
                .codigo("TK-123")
                .vehiculo(vehiculo)
                .espacio(espacio)
                .horaEntrada(LocalDateTime.now())
                .estado(EstadoTicket.ACTIVO)
                .build();
        entityManager.persist(ticket);
        entityManager.flush();

        Optional<Ticket> encontrado = ticketRepository.findByCodigo("TK-123");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getVehiculo().getPlaca()).isEqualTo("ABC-123");
    }

    @Test
    void findTicketActivoByPlaca_deberiaRetornarTicketActivo() {
        Ticket ticket = Ticket.builder()
                .codigo("TK-123")
                .vehiculo(vehiculo)
                .espacio(espacio)
                .horaEntrada(LocalDateTime.now())
                .estado(EstadoTicket.ACTIVO)
                .build();
        entityManager.persist(ticket);
        entityManager.flush();

        Optional<Ticket> encontrado = ticketRepository.findTicketActivoByPlaca("ABC-123");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEstado()).isEqualTo(EstadoTicket.ACTIVO);
    }

    @Test
    void countByEstado_deberiaRetornarCantidadCorrecta() {
        Ticket ticket = Ticket.builder()
                .codigo("TK-123")
                .vehiculo(vehiculo)
                .espacio(espacio)
                .horaEntrada(LocalDateTime.now())
                .estado(EstadoTicket.ACTIVO)
                .build();
        entityManager.persist(ticket);
        entityManager.flush();

        Long activos = ticketRepository.countByEstado(EstadoTicket.ACTIVO);
        assertThat(activos).isEqualTo(1L);
    }
}
