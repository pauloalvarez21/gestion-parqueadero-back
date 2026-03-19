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
public class HistorialDTO {
    private Long id;
    private String placaVehiculo;
    private String codigoEspacio;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private Long duracionMinutos;
    private BigDecimal valorTotal;
    private LocalDateTime fechaRegistro;
    private String creadoPor;
    private String finalizadoPor;
}
