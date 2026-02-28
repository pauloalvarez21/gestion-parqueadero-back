package com.parqueadero.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AgregarEspaciosRequest {
    private String tipoVehiculo;
    private int cantidad;
    private BigDecimal tarifaBase;
}