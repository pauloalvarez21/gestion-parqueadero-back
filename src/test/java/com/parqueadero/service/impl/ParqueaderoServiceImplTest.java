package com.parqueadero.service.impl;

import com.parqueadero.dto.EntradaRequest;
import com.parqueadero.dto.EspacioDTO;
import com.parqueadero.dto.EstadisticasDTO;
import com.parqueadero.dto.PagoResponse;
import com.parqueadero.dto.SalidaRequest;
import com.parqueadero.dto.TicketDTO;
import com.parqueadero.entity.Espacio;
import com.parqueadero.entity.Historial;
import com.parqueadero.entity.Ticket;
import com.parqueadero.entity.Tarifa;
import com.parqueadero.entity.Vehiculo;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.EstadoTicket;
import com.parqueadero.enums.TipoTarifa;
import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.exception.ConfiguracionException;
import com.parqueadero.exception.NoHayEspaciosDisponiblesException;
import com.parqueadero.exception.TicketNoEncontradoException;
import com.parqueadero.exception.TicketYaProcesadoException;
import com.parqueadero.exception.VehiculoYaEstacionadoException;
import com.parqueadero.mapper.ParqueaderoMapper;
import com.parqueadero.repository.EspacioRepository;
import com.parqueadero.repository.HistorialRepository;
import com.parqueadero.repository.TarifaRepository;
import com.parqueadero.repository.TicketRepository;
import com.parqueadero.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @InjectMocks
    private ParqueaderoServiceImpl parqueaderoService;

    private EntradaRequest entradaRequest;
    private Vehiculo vehiculo;
    private Espacio espacio;
    private Ticket ticket;
    private TicketDTO ticketDTO;
    private SalidaRequest salidaRequest;

    @BeforeEach
    void setUp() {
        entradaRequest = new EntradaRequest();
        entradaRequest.setPlaca("NEW-456");
        entradaRequest.setTipoVehiculo("CARRO");
        entradaRequest.setTipoTarifa("POR_HORA");

        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPlaca("NEW-456");
        vehiculo.setTipo(TipoVehiculo.CARRO);

        espacio = new Espacio();
        espacio.setId(1L);
        espacio.setCodigo("C01");
        espacio.setEstado(EstadoEspacio.DISPONIBLE);
        espacio.setTipoVehiculoPermitido(TipoVehiculo.CARRO);
        espacio.setTarifaBase(new BigDecimal("3000")); // Tarifa por hora

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setCodigo("TKT-12345");
        ticket.setVehiculo(vehiculo);
        ticket.setEspacio(espacio);
        ticket.setHoraEntrada(LocalDateTime.now().minusHours(2)); // Estuvo 2 horas
        ticket.setEstado(EstadoTicket.ACTIVO);
        ticket.setTipoTarifa(TipoTarifa.POR_HORA);

        ticketDTO = new TicketDTO();
        ticketDTO.setCodigo("TKT-12345");

        salidaRequest = new SalidaRequest();
        salidaRequest.setCodigoTicket("TKT-12345");
        salidaRequest.setObservaciones("Sin novedades.");
    }

    @Test
    void registrarEntrada_cuandoVehiculoEsNuevoYHayEspacio_deberiaCrearTicket() {
        // Arrange
        // 1. No hay ticket activo para esta placa
        when(ticketRepository.findTicketActivoByPlaca(anyString())).thenReturn(Optional.empty());
        // 2. El vehículo no existe, se creará uno nuevo
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(Optional.empty());
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);
        // 3. Hay un espacio disponible
        when(espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(any(TipoVehiculo.class), any(EstadoEspacio.class)))
                .thenReturn(Optional.of(espacio));
        // 4. Se guarda el ticket y se mapea a DTO
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(mapper.toTicketDTO(any(Ticket.class))).thenReturn(ticketDTO);

        // Act
        TicketDTO resultado = parqueaderoService.registrarEntrada(entradaRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals("TKT-12345", resultado.getCodigo());
        assertEquals(EstadoEspacio.OCUPADO, espacio.getEstado()); // Verificamos que el estado del espacio cambió
        verify(ticketRepository, times(1)).save(any(Ticket.class)); // Verificamos que se llamó a guardar el ticket
    }

    @Test
    void registrarEntrada_cuandoVehiculoYaTieneTicketActivo_deberiaLanzarExcepcion() {
        // Arrange
        // Simulamos que el repositorio SÍ encuentra un ticket activo para la placa
        when(ticketRepository.findTicketActivoByPlaca(entradaRequest.getPlaca().toUpperCase()))
                .thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(VehiculoYaEstacionadoException.class, () -> parqueaderoService.registrarEntrada(entradaRequest));
    }

    @Test
    void registrarEntrada_cuandoNoHayEspaciosDisponibles_deberiaLanzarExcepcion() {
        // Arrange
        when(ticketRepository.findTicketActivoByPlaca(anyString())).thenReturn(Optional.empty());
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(Optional.empty());
        // Simulamos que no se encuentra ningún espacio disponible
        when(espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(any(TipoVehiculo.class), any(EstadoEspacio.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoHayEspaciosDisponiblesException.class, () -> parqueaderoService.registrarEntrada(entradaRequest));
    }

    @Test
    void registrarEntrada_cuandoVehiculoYaExiste_deberiaUsarVehiculoExistente() {
        // Arrange
        when(ticketRepository.findTicketActivoByPlaca(anyString())).thenReturn(Optional.empty());
        when(vehiculoRepository.findByPlaca(anyString())).thenReturn(Optional.of(vehiculo)); // El vehículo ya existe
        when(espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(any(), any())).thenReturn(Optional.of(espacio));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        parqueaderoService.registrarEntrada(entradaRequest);

        // Assert
        // Verificamos que NO se intentó guardar un nuevo vehículo
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    void registrarSalida_cuandoTicketEsValido_deberiaProcesarSalida() {
        // Arrange
        when(ticketRepository.findByCodigo(salidaRequest.getCodigoTicket().toUpperCase()))
                .thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        assertNotNull(resultado);
        // Verificamos el cálculo: 2 horas a 3000/hr = 6000
        assertEquals(0, new BigDecimal("6000.00").compareTo(resultado.getValorTotal()));
        assertEquals(EstadoTicket.PAGADO, ticket.getEstado());
        assertEquals(EstadoEspacio.DISPONIBLE, espacio.getEstado());

        verify(espacioRepository, times(1)).save(espacio);
        verify(ticketRepository, times(1)).save(ticket);
        verify(historialRepository, times(1)).save(any(Historial.class));
    }

    @Test
    void registrarSalida_cuandoTicketNoExiste_deberiaLanzarExcepcion() {
        // Arrange
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNoEncontradoException.class, () -> parqueaderoService.registrarSalida(salidaRequest));
    }

    @Test
    void registrarSalida_cuandoSeUsaPlaca_deberiaProcesarSalida() {
        // Arrange
        salidaRequest.setCodigoTicket(null);
        salidaRequest.setPlaca("NEW-456");

        when(ticketRepository.findTicketActivoByPlaca("NEW-456"))
                .thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoTicket.PAGADO, ticket.getEstado());
    }

    @Test
    void registrarSalida_cuandoTicketYaEstaPagado_deberiaLanzarExcepcion() {
        // Arrange
        ticket.setEstado(EstadoTicket.PAGADO); // Cambiamos el estado a pagado
        when(ticketRepository.findByCodigo(salidaRequest.getCodigoTicket().toUpperCase())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(TicketYaProcesadoException.class, () -> parqueaderoService.registrarSalida(salidaRequest));
    }

    @ParameterizedTest(name = "POR HORA: {2} ({0} mins) -> cobra {1} hora(s)")
    @CsvSource({
            "59,  1, 'Menos de 1 hora'",
            "60,  1, 'Exactamente 1 hora'",
            "70,  1, '1h y 10m (dentro de gracia)'",
            "71,  2, '1h y 11m (fuera de gracia)'",
            "120, 2, 'Exactamente 2 horas'",
            "130, 2, '2h y 10m (dentro de gracia)'",
            "131, 3, '2h y 11m (fuera de gracia)'"
    })
    void registrarSalida_conTarifaPorHora_deberiaAplicarPeriodoDeGraciaCorrectamente(long minutosEstadia, int horasACobrarEsperadas, String descripcionCaso) {
        // Arrange
        // Simulamos la duración de la estadía ajustando la hora de entrada
        ticket.setHoraEntrada(LocalDateTime.now().minusMinutes(minutosEstadia));
        ticket.setTipoTarifa(TipoTarifa.POR_HORA);

        when(ticketRepository.findByCodigo(salidaRequest.getCodigoTicket().toUpperCase()))
                .thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        BigDecimal valorEsperado = espacio.getTarifaBase().multiply(new BigDecimal(horasACobrarEsperadas));
        assertEquals(0, valorEsperado.compareTo(resultado.getValorTotal()), "El valor total cobrado no es el esperado para el caso: " + descripcionCaso);
    }

    @Test
    @DisplayName("POR HORA: Debería aplicar 10% de descuento para 8 horas o más")
    void registrarSalida_conEstadiaLargaPorHora_deberiaAplicarDescuento() {
        // Arrange
        ticket.setTipoTarifa(TipoTarifa.POR_HORA);
        ticket.setHoraEntrada(LocalDateTime.now().minusHours(9)); // 9 horas de estadía
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        BigDecimal valorBase = new BigDecimal("3000").multiply(new BigDecimal("9")); // 27000
        BigDecimal descuento = valorBase.multiply(new BigDecimal("0.10")); // 2700
        BigDecimal valorEsperado = valorBase.subtract(descuento); // 24300

        assertEquals(0, valorEsperado.compareTo(resultado.getValorTotal()));
        assertEquals(0, descuento.compareTo(resultado.getDescuento()));
    }

    @Test
    @DisplayName("POR FRACCION: Debería calcular correctamente por fracciones de 15 min")
    void registrarSalida_conTarifaPorFraccion_deberiaCalcularCorrectamente() {
        // Arrange
        ticket.setTipoTarifa(TipoTarifa.FRACCION);
        ticket.setHoraEntrada(LocalDateTime.now().minusMinutes(35)); // 35 minutos -> 3 fracciones
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        // Tarifa por hora 3000 -> Tarifa por fracción 750 (3000/4)
        // 3 fracciones * 750 = 2250
        BigDecimal valorEsperado = new BigDecimal("2250.00");
        assertEquals(0, valorEsperado.compareTo(resultado.getValorTotal()));
    }

    @Test
    @DisplayName("POR MES: Debería cobrar la tarifa fija mensual")
    void registrarSalida_conTarifaPorMes_deberiaCobrarTarifaFija() {
        // Arrange
        ticket.setTipoTarifa(TipoTarifa.POR_MES);
        Tarifa tarifaMes = new Tarifa(1L, TipoTarifa.POR_MES, new BigDecimal("200000.00"));
        when(tarifaRepository.findByTipoTarifa(TipoTarifa.POR_MES)).thenReturn(Optional.of(tarifaMes));
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        // La tarifa por mes está hardcodeada en 200000
        BigDecimal valorEsperado = new BigDecimal("200000.00");
        assertEquals(0, valorEsperado.compareTo(resultado.getValorTotal()));
    }

    @Test
    void obtenerEstadisticas_deberiaRetornarValoresCorrectos() {
        // Arrange
        when(ticketRepository.countByEstado(EstadoTicket.ACTIVO)).thenReturn(5L);
        when(espacioRepository.countByEstado(EstadoEspacio.DISPONIBLE)).thenReturn(10L);
        when(espacioRepository.countByEstado(EstadoEspacio.OCUPADO)).thenReturn(5L);
        when(ticketRepository.sumIngresosHoy()).thenReturn(125000.50);
        when(ticketRepository.countTicketsHoy()).thenReturn(20L);

        // Act
        EstadisticasDTO resultado = parqueaderoService.obtenerEstadisticas();

        // Assert
        assertNotNull(resultado);
        assertEquals(5L, resultado.getVehiculosActivos());
        assertEquals(10L, resultado.getEspaciosDisponibles());
        assertEquals(5L, resultado.getEspaciosOcupados());
        assertEquals(0, new BigDecimal("125000.50").compareTo(resultado.getIngresosHoy()));
        assertEquals(20L, resultado.getTicketsHoy());
    }

    @Test
    void obtenerEstadisticas_cuandoNoHayIngresosHoy_deberiaRetornarCero() {
        // Arrange
        // Simulamos que la consulta a la base de datos devuelve null porque no hubo ingresos.
        when(ticketRepository.sumIngresosHoy()).thenReturn(null);

        // Act
        EstadisticasDTO resultado = parqueaderoService.obtenerEstadisticas();

        // Assert
        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getIngresosHoy());
    }

    @ParameterizedTest(name = "POR DIA: Entrada hace {0} días -> Total a pagar: {1}")
    @CsvSource({
            "0, 15000.00", // Mismo día (0 días de diferencia) -> Cobra 1 día
            "1, 30000.00", // Día siguiente (1 día de diferencia) -> Cobra 2 días
            "4, 75000.00"  // 4 días de diferencia -> Cobra 5 días
    })
    void registrarSalida_conTarifaPorDia_deberiaCalcularCorrectamente(long diasAtras, String valorEsperadoStr) {
        // Arrange
        ticket.setTipoTarifa(TipoTarifa.POR_DIA);
        Tarifa tarifaDia = new Tarifa(2L, TipoTarifa.POR_DIA, new BigDecimal("15000.00"));
        when(tarifaRepository.findByTipoTarifa(TipoTarifa.POR_DIA)).thenReturn(Optional.of(tarifaDia));
        ticket.setHoraEntrada(LocalDateTime.now().minusDays(diasAtras));

        when(ticketRepository.findByCodigo(salidaRequest.getCodigoTicket().toUpperCase()))
                .thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        BigDecimal valorEsperado = new BigDecimal(valorEsperadoStr);
        assertEquals(0, valorEsperado.compareTo(resultado.getValorTotal()));
    }

    @Test
    void registrarSalida_cuandoTarifaNoConfigurada_deberiaLanzarExcepcion() {
        // Arrange
        espacio.setTarifaBase(null); // Simulamos que la tarifa no está configurada
        ticket.setEspacio(espacio);
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(ConfiguracionException.class, () -> parqueaderoService.registrarSalida(salidaRequest));
    }

    @Test
    void obtenerTicket_cuandoExiste_deberiaRetornarDTO() {
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.of(ticket));
        when(mapper.toTicketDTO(any(Ticket.class))).thenReturn(ticketDTO);

        TicketDTO result = parqueaderoService.obtenerTicket("TKT-12345");
        assertNotNull(result);
    }

    @Test
    void obtenerTicket_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.empty());
        assertThrows(TicketNoEncontradoException.class, () -> parqueaderoService.obtenerTicket("TKT-999"));
    }

    @Test
    void listarTicketsActivos_deberiaRetornarLista() {
        when(ticketRepository.findByEstado(EstadoTicket.ACTIVO)).thenReturn(List.of(ticket));
        when(mapper.toTicketDTO(any(Ticket.class))).thenReturn(ticketDTO);

        List<TicketDTO> lista = parqueaderoService.listarTicketsActivos();
        assertFalse(lista.isEmpty());
    }

    @Test
    void listarEspacios_deberiaRetornarLista() {
        when(espacioRepository.findAll()).thenReturn(List.of(espacio));
        when(mapper.toEspacioDTO(any(Espacio.class))).thenReturn(new EspacioDTO());

        List<EspacioDTO> lista = parqueaderoService.listarEspacios();
        assertFalse(lista.isEmpty());
    }

    @Test
    void listarEspaciosDisponibles_deberiaRetornarLista() {
        when(espacioRepository.findByEstado(EstadoEspacio.DISPONIBLE)).thenReturn(List.of(espacio));
        when(mapper.toEspacioDTO(any(Espacio.class))).thenReturn(new EspacioDTO());

        List<EspacioDTO> lista = parqueaderoService.listarEspaciosDisponibles();
        assertFalse(lista.isEmpty());
    }

    @Test
    void registrarSalida_conTiempoCero_deberiaCobrarCero() {
        // Arrange
        ticket.setTipoTarifa(TipoTarifa.POR_HORA);
        ticket.setHoraEntrada(LocalDateTime.now()); // Entra y sale al mismo tiempo
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        assertEquals(0, BigDecimal.ZERO.compareTo(resultado.getValorTotal()));
    }

    @Test
    void registrarEntrada_conDatosInvalidos_deberiaLanzarExcepcion() {
        entradaRequest.setTipoVehiculo("INVALIDO");
        assertThrows(IllegalArgumentException.class, () -> parqueaderoService.registrarEntrada(entradaRequest));
    }

    @Test
    void registrarSalida_conTarifaPorMinuto_deberiaCalcularCorrectamente() {
        // Arrange
        ticket.setTipoTarifa(TipoTarifa.POR_MINUTO);
        ticket.setHoraEntrada(LocalDateTime.now().minusMinutes(30)); // 30 minutos
        Tarifa tarifaMinuto = new Tarifa(3L, TipoTarifa.POR_MINUTO, new BigDecimal("50.00")); // $50 por minuto
        when(tarifaRepository.findByTipoTarifa(TipoTarifa.POR_MINUTO)).thenReturn(Optional.of(tarifaMinuto));
        when(ticketRepository.findByCodigo(anyString())).thenReturn(Optional.of(ticket));

        // Act
        PagoResponse resultado = parqueaderoService.registrarSalida(salidaRequest);

        // Assert
        assertEquals(0, new BigDecimal("1500.00").compareTo(resultado.getValorTotal()));
    }

    @Test
    void eliminarTarifa_cuandoExiste_deberiaEliminarla() {
        // Arrange
        String tipoStr = "POR_MINUTO";
        Tarifa tarifa = new Tarifa(1L, TipoTarifa.POR_MINUTO, new BigDecimal("50"));
        when(tarifaRepository.findByTipoTarifa(TipoTarifa.POR_MINUTO)).thenReturn(Optional.of(tarifa));

        // Act
        parqueaderoService.eliminarTarifa(tipoStr);

        // Assert
        verify(tarifaRepository, times(1)).delete(tarifa);
    }

    @Test
    void eliminarTarifa_cuandoTipoInvalido_deberiaLanzarExcepcion() {
        // Arrange
        String tipoStr = "INVALIDO";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> parqueaderoService.eliminarTarifa(tipoStr));
    }

    @Test
    void eliminarTarifa_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Arrange
        String tipoStr = "POR_MINUTO";
        when(tarifaRepository.findByTipoTarifa(TipoTarifa.POR_MINUTO)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ConfiguracionException.class, () -> parqueaderoService.eliminarTarifa(tipoStr));
    }
}