package com.parqueadero.service.impl;

import com.parqueadero.dto.EntradaRequest;
import com.parqueadero.dto.PagoResponse;
import com.parqueadero.dto.SalidaRequest;
import com.parqueadero.entity.Espacio;
import com.parqueadero.entity.Ticket;
import com.parqueadero.entity.Vehiculo;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.EstadoTicket;

import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.exception.NoHayEspaciosDisponiblesException;
import com.parqueadero.exception.VehiculoYaEstacionadoException;
import com.parqueadero.mapper.ParqueaderoMapper;
import com.parqueadero.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParqueaderoServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private EspacioRepository espacioRepository;
    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private HistorialRepository historialRepository;
    @Mock
    private TarifaRepository tarifaRepository;
    @Mock
    private ParqueaderoMapper mapper;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ResolucionFacturaRepository resolucionFacturaRepository;

    @InjectMocks
    private ParqueaderoServiceImpl parqueaderoService;

    private EntradaRequest entradaRequest;
    private Vehiculo vehiculo;
    private Espacio espacio;

    @BeforeEach
    void setUp() {
        entradaRequest = new EntradaRequest();
        entradaRequest.setPlaca("ABC-123");
        entradaRequest.setTipoVehiculo("CARRO");


        vehiculo = Vehiculo.builder().id(1L).placa("ABC-123").tipo(TipoVehiculo.CARRO).build();
        espacio = new Espacio();
        espacio.setId(1L);
        espacio.setCodigo("C-1");
        espacio.setEstado(EstadoEspacio.DISPONIBLE);
        espacio.setTipoVehiculoPermitido(TipoVehiculo.CARRO);
        espacio.setTarifaBase(new BigDecimal("3000"));

        // Configurar SecurityContext mock para tests que requieren autenticación (lenient)
        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(
            new UsernamePasswordAuthenticationToken("testUser", null)
        );
        SecurityContextHolder.setContext(securityContext);

        // Configurar mock de usuario autenticado (lenient porque no todos los tests lo usan)
        lenient().when(usuarioRepository.findByUsername("testUser")).thenReturn(
            Optional.of(com.parqueadero.entity.Usuario.builder()
                .id(1L)
                .username("testUser")
                .build())
        );

        // Configurar resolucion de factura (lenient)
        lenient().when(resolucionFacturaRepository.findActiveResolution()).thenReturn(
            Optional.of(com.parqueadero.entity.ResolucionFactura.builder()
                .id(1L)
                .prefijo("FC")
                .numeroActual(100L)
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .build())
        );
    }

    @Test
    void registrarEntrada_deberiaRegistrarVehiculoExitosamente() {
        // Arrange
        when(ticketRepository.findTicketActivoByPlaca("ABC-123")).thenReturn(Optional.empty());
        when(vehiculoRepository.findByPlaca("ABC-123")).thenReturn(Optional.of(vehiculo));
        when(espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(TipoVehiculo.CARRO, EstadoEspacio.DISPONIBLE))
                .thenReturn(Optional.of(espacio));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        parqueaderoService.registrarEntrada(entradaRequest);

        // Assert
        assertEquals(EstadoEspacio.OCUPADO, espacio.getEstado());
        verify(espacioRepository, times(1)).findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(any(), any());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(mapper, times(1)).toTicketDTO(any(Ticket.class));
    }

    @Test
    void registrarEntrada_deberiaLanzarExcepcion_siVehiculoYaEstaEstacionado() {
        // Arrange
        Ticket ticketActivo = Ticket.builder().estado(EstadoTicket.ACTIVO).codigo("TKT-001").build();
        when(ticketRepository.findTicketActivoByPlaca("ABC-123")).thenReturn(Optional.of(ticketActivo));

        // Act & Assert
        assertThrows(VehiculoYaEstacionadoException.class, () -> {
            parqueaderoService.registrarEntrada(entradaRequest);
        });

        verify(espacioRepository, never()).findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(any(), any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void registrarEntrada_deberiaLanzarExcepcion_siNoHayEspaciosDisponibles() {
        // Arrange
        when(ticketRepository.findTicketActivoByPlaca("ABC-123")).thenReturn(Optional.empty());
        when(vehiculoRepository.findByPlaca("ABC-123")).thenReturn(Optional.of(vehiculo));
        when(espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(TipoVehiculo.CARRO, EstadoEspacio.DISPONIBLE))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoHayEspaciosDisponiblesException.class, () -> {
            parqueaderoService.registrarEntrada(entradaRequest);
        });
    }

    @Test
    void registrarSalida_deberiaCalcularPagoCorrectamente_paraTarifaPorHora() {
        // Arrange
        SalidaRequest salidaRequest = new SalidaRequest();
        salidaRequest.setCodigoTicket("TKT-TEST");

        LocalDateTime entrada = LocalDateTime.now().minusHours(2).minusMinutes(30); // 2.5 horas -> debe cobrar 3 horas

        Ticket ticket = Ticket.builder()
                .id(1L)
                .codigo("TKT-TEST")
                .vehiculo(vehiculo)
                .espacio(espacio)
                .horaEntrada(entrada)
                .estado(EstadoTicket.ACTIVO)
                .build();

        when(ticketRepository.findByCodigo("TKT-TEST")).thenReturn(Optional.of(ticket));

        // Configurar tarifa POR_HORA para CARRO
        com.parqueadero.entity.Tarifa tarifaHora = com.parqueadero.entity.Tarifa.builder()
            .tipoVehiculo(TipoVehiculo.CARRO)
            .tipoTarifa(com.parqueadero.enums.TipoTarifa.POR_HORA)
            .valor(new BigDecimal("3000"))
            .build();
        when(tarifaRepository.findByTipoVehiculoAndTipoTarifa(TipoVehiculo.CARRO, com.parqueadero.enums.TipoTarifa.POR_HORA))
            .thenReturn(Optional.of(tarifaHora));

        // Act
        PagoResponse pagoResponse = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        // 3 horas * 3000/hora = 9000
        assertEquals(0, new BigDecimal("9000.00").compareTo(pagoResponse.getValorTotal()));
        assertEquals(EstadoEspacio.DISPONIBLE, espacio.getEstado());
        assertEquals(EstadoTicket.PAGADO, ticket.getEstado());

        verify(espacioRepository, times(1)).save(espacio);
        verify(ticketRepository, times(2)).save(ticket);
        verify(historialRepository, times(1)).save(any());
    }
}