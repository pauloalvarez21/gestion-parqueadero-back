package com.parqueadero.service;

import com.parqueadero.dto.*;
import java.util.List;

public interface ParqueaderoService {
    TicketDTO registrarEntrada(EntradaRequest request);
    PagoResponse registrarSalida(SalidaRequest request);
    TicketDTO obtenerTicket(String codigo);
    List<TicketDTO> listarTicketsActivos();
    List<TarifaDTO> listarTarifas();
    List<EspacioDTO> listarEspacios();
    List<EspacioDTO> listarEspaciosDisponibles();
    List<EspacioDTO> agregarEspacios(AgregarEspaciosRequest request);
    List<EspacioDTO> eliminarEspacios(EliminarEspaciosRequest request);

    TarifaDTO guardarTarifa(TarifaDTO request);

    void eliminarTarifa(String tipoVehiculo, String tipoTarifa);

    EstadisticasDTO obtenerEstadisticas();
    List<HistorialDTO> listarHistorial();

    ResolucionFacturaDTO configurarResolucion(ResolucionFacturaDTO request);
    ResolucionFacturaDTO obtenerResolucionActiva();
}
