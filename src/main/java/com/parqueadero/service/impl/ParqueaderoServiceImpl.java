package com.parqueadero.service.impl;

import com.parqueadero.dto.*;
import com.parqueadero.entity.*;
import com.parqueadero.enums.*;
import com.parqueadero.exception.*;
import com.parqueadero.mapper.ParqueaderoMapper;
import com.parqueadero.repository.*;
import com.parqueadero.service.ParqueaderoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParqueaderoServiceImpl implements ParqueaderoService {

    private final TicketRepository ticketRepository;
    private final EspacioRepository espacioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final HistorialRepository historialRepository;
    private final TarifaRepository tarifaRepository;
    private final ParqueaderoMapper mapper;

    @Override
    @Transactional
    public TicketDTO registrarEntrada(EntradaRequest request) {
        log.info("Registrando entrada para vehículo: {}", request.getPlaca());
        
        // Validar si ya existe un ticket activo para esta placa
        ticketRepository.findTicketActivoByPlaca(request.getPlaca().toUpperCase())
            .ifPresent(t -> { throw new VehiculoYaEstacionadoException("El vehículo ya tiene un ticket activo: " + t.getCodigo()); });
        
        // Buscar o crear vehículo
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(request.getPlaca().toUpperCase())
            .orElseGet(() -> crearNuevoVehiculo(request));
        
        // Determinar tipo de vehículo
        TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(request.getTipoVehiculo().toUpperCase());
        
        // Buscar espacio disponible
        Espacio espacio = espacioRepository.findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(tipoVehiculo, EstadoEspacio.DISPONIBLE)
            .orElseThrow(() -> new NoHayEspaciosDisponiblesException("No hay espacios disponibles para tipo: " + tipoVehiculo));

        espacio.setEstado(EstadoEspacio.OCUPADO);
        
        // Crear ticket
        Ticket ticket = Ticket.builder()
            .codigo(generarCodigoTicket())
            .vehiculo(vehiculo)
            .espacio(espacio)
            .horaEntrada(LocalDateTime.now())
            .tipoTarifa(TipoTarifa.valueOf(request.getTipoTarifa().toUpperCase()))
            .estado(EstadoTicket.ACTIVO)
            .build();
        
        ticket = ticketRepository.save(ticket);
        log.info("Ticket creado: {} para vehículo {}", ticket.getCodigo(), vehiculo.getPlaca());
        
        return mapper.toTicketDTO(ticket);
    }

    @Override
    @Transactional
    public PagoResponse registrarSalida(SalidaRequest request) {
        log.info("Procesando salida. Ticket: {}, Placa: {}", request.getCodigoTicket(), request.getPlaca());
        
        Ticket ticket;
        if (request.getCodigoTicket() != null && !request.getCodigoTicket().isBlank()) {
            ticket = ticketRepository.findByCodigo(request.getCodigoTicket().toUpperCase())
                .orElseThrow(() -> new TicketNoEncontradoException("Ticket no encontrado: " + request.getCodigoTicket()));
        } else if (request.getPlaca() != null && !request.getPlaca().isBlank()) {
            ticket = ticketRepository.findTicketActivoByPlaca(request.getPlaca().toUpperCase())
                .orElseThrow(() -> new TicketNoEncontradoException("No se encontró ticket activo para la placa: " + request.getPlaca()));
        } else {
            throw new IllegalArgumentException("Debe proporcionar el código del ticket o la placa.");
        }
        
        if (ticket.getEstado() != EstadoTicket.ACTIVO) {
            throw new TicketYaProcesadoException("El ticket ya ha sido procesado: " + ticket.getEstado());
        }
        
        LocalDateTime horaSalida = LocalDateTime.now();
        ticket.setHoraSalida(horaSalida);
        ticket.setObservaciones(request.getObservaciones());
        
        // Calcular pago
        PagoResponse pago = calcularPago(ticket);
        
        ticket.setValorBase(pago.getValorBase());
        ticket.setValorAdicional(pago.getValorAdicional());
        ticket.setDescuento(pago.getDescuento());
        ticket.setValorTotal(pago.getValorTotal());
        ticket.setEstado(EstadoTicket.PAGADO);
        ticket.setFechaPago(LocalDateTime.now());
        
        // Liberar espacio
        Espacio espacio = ticket.getEspacio();
        espacio.setEstado(EstadoEspacio.DISPONIBLE);
        espacioRepository.save(espacio);
        
        ticketRepository.save(ticket);
        
        // Guardar en historial
        guardarEnHistorial(ticket);
        
        log.info("Salida procesada. Total a pagar: {}", pago.getValorTotal());
        return pago;
    }

    private PagoResponse calcularPago(Ticket ticket) {
        LocalDateTime entrada = ticket.getHoraEntrada();
        LocalDateTime salida = ticket.getHoraSalida();
        
        long minutosTotales = ChronoUnit.MINUTES.between(entrada, salida);
        long horas = minutosTotales / 60; // Horas completas, para el descuento
        
        // Se obtiene la tarifa base del espacio, que se asume es la tarifa por hora.
        BigDecimal tarifaHora = ticket.getEspacio().getTarifaBase();
        if (tarifaHora == null || tarifaHora.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConfiguracionException("La tarifa base para el espacio " + ticket.getEspacio().getCodigo() + " no está configurada correctamente.");
        }
        
        BigDecimal valorBase = BigDecimal.ZERO;
        BigDecimal valorAdicional = BigDecimal.ZERO;
        
        switch (ticket.getTipoTarifa()) {
            case POR_MINUTO:
                BigDecimal tarifaMinuto = obtenerTarifaGlobal(TipoTarifa.POR_MINUTO);
                valorBase = tarifaMinuto.multiply(new BigDecimal(minutosTotales));
                break;
            case POR_HORA:
                // Si el tiempo es 0 o negativo, el costo es 0.
                if (minutosTotales <= 0) {
                    valorBase = BigDecimal.ZERO;
                    break;
                }
                
                // Se cobra siempre como mínimo una hora.
                long horasACobrar = 1;
                
                // Si la estadía es mayor a una hora, se calculan las horas adicionales.
                if (minutosTotales > 60) {
                    long minutosDespuesDePrimeraHora = minutosTotales - 60;
                    long horasAdicionales = minutosDespuesDePrimeraHora / 60;
                    long minutosRestantesAdicionales = minutosDespuesDePrimeraHora % 60;
                    
                    horasACobrar += horasAdicionales;
                    
                    // Se aplica el período de gracia de 10 minutos para las horas siguientes.
                    if (minutosRestantesAdicionales > 10) {
                        horasACobrar++;
                    }
                }
                valorBase = tarifaHora.multiply(new BigDecimal(horasACobrar));
                break;
            case POR_DIA:
                BigDecimal tarifaDia = obtenerTarifaGlobal(TipoTarifa.POR_DIA);
                long dias = ChronoUnit.DAYS.between(entrada.toLocalDate(), salida.toLocalDate()) + 1;
                valorBase = tarifaDia.multiply(new BigDecimal(dias));
                break;
            case POR_MES:
                BigDecimal tarifaMes = obtenerTarifaGlobal(TipoTarifa.POR_MES);
                valorBase = tarifaMes;
                break;
            case FRACCION:
                long fracciones = (minutosTotales / 15) + (minutosTotales % 15 > 0 ? 1 : 0);
                BigDecimal tarifaFraccion = tarifaHora.divide(new BigDecimal("4"), 0, RoundingMode.CEILING);
                valorBase = tarifaFraccion.multiply(new BigDecimal(fracciones));
                break;
        }
        
        // Descuento por tiempo largo (más de 8 horas)
        BigDecimal descuento = BigDecimal.ZERO;
        if (horas >= 8 && ticket.getTipoTarifa() == TipoTarifa.POR_HORA) {
            descuento = valorBase.multiply(new BigDecimal("0.10")); // 10% de descuento
        }
        
        BigDecimal valorTotal = valorBase.add(valorAdicional).subtract(descuento);
        
        return PagoResponse.builder()
            .codigoTicket(ticket.getCodigo())
            .horaEntrada(entrada)
            .horaSalida(salida)
            .duracionHoras(horas)
            .duracionMinutos(minutosTotales)
            .valorBase(valorBase)
            .valorAdicional(valorAdicional)
            .descuento(descuento)
            .valorTotal(valorTotal.setScale(2, RoundingMode.HALF_UP))
            .mensaje("Pago calculado exitosamente")
            .build();
    }

    private BigDecimal obtenerTarifaGlobal(TipoTarifa tipo) {
        return tarifaRepository.findByTipoTarifa(tipo)
                .map(Tarifa::getValor)
                .orElseThrow(() -> new ConfiguracionException("No existe tarifa configurada para " + tipo));
    }

    private void guardarEnHistorial(Ticket ticket) {
        long minutos = ChronoUnit.MINUTES.between(ticket.getHoraEntrada(), ticket.getHoraSalida());
        
        Historial historial = Historial.builder()
            .placaVehiculo(ticket.getVehiculo().getPlaca())
            .codigoEspacio(ticket.getEspacio().getCodigo())
            .horaEntrada(ticket.getHoraEntrada())
            .horaSalida(ticket.getHoraSalida())
            .duracionMinutos(minutos)
            .valorTotal(ticket.getValorTotal())
            .build();
        
        historialRepository.save(historial);
    }

    private Vehiculo crearNuevoVehiculo(EntradaRequest request) {
        Vehiculo vehiculo = Vehiculo.builder()
            .placa(request.getPlaca().toUpperCase())
            .tipo(TipoVehiculo.valueOf(request.getTipoVehiculo().toUpperCase()))
            .build();
        return vehiculoRepository.save(vehiculo);
    }

    private String generarCodigoTicket() {
        // Usar UUID para garantizar un código único y evitar colisiones.
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        return "TKT-" + uuid.substring(0, 8);
    }

    @Override
    public TicketDTO obtenerTicket(String codigo) {
        Ticket ticket = ticketRepository.findByCodigo(codigo.toUpperCase())
            .orElseThrow(() -> new TicketNoEncontradoException("Ticket no encontrado"));
        return mapper.toTicketDTO(ticket);
    }

    @Override
    public List<TicketDTO> listarTicketsActivos() {
        return ticketRepository.findByEstado(EstadoTicket.ACTIVO)
            .stream()
            .map(mapper::toTicketDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EspacioDTO> listarEspacios() {
        return espacioRepository.findAll()
            .stream()
            .map(mapper::toEspacioDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<EspacioDTO> listarEspaciosDisponibles() {
        return espacioRepository.findByEstado(EstadoEspacio.DISPONIBLE)
            .stream()
            .map(mapper::toEspacioDTO)
            .collect(Collectors.toList());
    }

    @Override
    public EstadisticasDTO obtenerEstadisticas() {
        Long activos = ticketRepository.countByEstado(EstadoTicket.ACTIVO);
        Long disponibles = espacioRepository.countByEstado(EstadoEspacio.DISPONIBLE);
        Long ocupados = espacioRepository.countByEstado(EstadoEspacio.OCUPADO);
        
        Double ingresosHoy = ticketRepository.sumIngresosHoy();
        Long ticketsHoy = ticketRepository.countTicketsHoy();
        
        return EstadisticasDTO.builder()
            .vehiculosActivos(activos)
            .espaciosDisponibles(disponibles)
            .espaciosOcupados(ocupados)
            .ingresosHoy(ingresosHoy != null ? BigDecimal.valueOf(ingresosHoy) : BigDecimal.ZERO)
            .ingresosMes(BigDecimal.ZERO) // Implementar consulta mensual
            .ticketsHoy(ticketsHoy)
            .ticketsMes(0L)
            .build();
    }

    @Override
    @Transactional
    public List<EspacioDTO> agregarEspacios(AgregarEspaciosRequest request) {
        TipoVehiculo tipo = TipoVehiculo.valueOf(request.getTipoVehiculo().toUpperCase());
        
        if (request.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (request.getTarifaBase() == null || request.getTarifaBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La tarifa base es requerida y debe ser positiva");
        }

        // Obtener espacios existentes para calcular la secuencia del código
        List<Espacio> espaciosExistentes = espacioRepository.findAll().stream()
                .filter(e -> e.getTipoVehiculoPermitido() == tipo)
                .collect(Collectors.toList());
                
        int maxSecuencia = espaciosExistentes.stream()
                .map(e -> {
                    try {
                        return Integer.parseInt(e.getCodigo().split("-")[1]);
                    } catch (Exception ex) {
                        return 0;
                    }
                })
                .max(Integer::compare)
                .orElse(0);
                
        String prefijo = tipo == TipoVehiculo.CARRO ? "C-" : (tipo == TipoVehiculo.MOTO ? "M-" : "B-");
        
        List<Espacio> nuevosEspacios = new java.util.ArrayList<>();
        for (int i = 1; i <= request.getCantidad(); i++) {
            Espacio espacio = new Espacio();
            espacio.setCodigo(prefijo + (maxSecuencia + i));
            espacio.setTipoVehiculoPermitido(tipo);
            espacio.setEstado(EstadoEspacio.DISPONIBLE);
            espacio.setTarifaBase(request.getTarifaBase());
            nuevosEspacios.add(espacio);
        }
        
        List<Espacio> guardados = espacioRepository.saveAll(nuevosEspacios);
        return guardados.stream().map(mapper::toEspacioDTO).collect(Collectors.toList());
    }
}