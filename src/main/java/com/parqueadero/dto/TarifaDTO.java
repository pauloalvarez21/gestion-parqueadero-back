package com.parqueadero.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TarifaDTO {
    private String tipoVehiculo;
    private String tipoTarifa;
    private BigDecimal valor;
}