package com.parqueadero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private Long id;
    private String codigo;
    private VehiculoDTO vehiculo;
    private EspacioDTO espacio;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private String tipoTarifa;
    private BigDecimal valorBase;
    private BigDecimal valorAdicional;
    private BigDecimal descuento;
    private BigDecimal valorTotal;
    private String estado;
    private String observaciones;
}
