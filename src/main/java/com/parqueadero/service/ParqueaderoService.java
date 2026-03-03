package com.parqueadero.service;

import com.parqueadero.dto.*;
import java.util.List;

public interface ParqueaderoService {
    TicketDTO registrarEntrada(EntradaRequest request);
    PagoResponse registrarSalida(SalidaRequest request);
    TicketDTO obtenerTicket(String codigo);
    List<TicketDTO> listarTicketsActivos();
    List<EspacioDTO> listarEspacios();
    List<EspacioDTO> listarEspaciosDisponibles();
    List<EspacioDTO> agregarEspacios(AgregarEspaciosRequest request);
    List<EspacioDTO> eliminarEspacios(EliminarEspaciosRequest request);
    List<TarifaDTO> listarTarifas();
    EstadisticasDTO obtenerEstadisticas();
    void eliminarTarifa(String tipoTarifa);
}
