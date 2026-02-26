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
public class PagoResponse {
    private String codigoTicket;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private Long duracionHoras;
    private Long duracionMinutos;
    private BigDecimal valorBase;
    private BigDecimal valorAdicional;
    private BigDecimal descuento;
    private BigDecimal valorTotal;
    private String mensaje;
}
