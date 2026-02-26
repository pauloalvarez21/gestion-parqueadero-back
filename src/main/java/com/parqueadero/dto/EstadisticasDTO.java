package com.parqueadero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasDTO {
    private Long vehiculosActivos;
    private Long espaciosDisponibles;
    private Long espaciosOcupados;
    private BigDecimal ingresosHoy;
    private BigDecimal ingresosMes;
    private Long ticketsHoy;
    private Long ticketsMes;
}
