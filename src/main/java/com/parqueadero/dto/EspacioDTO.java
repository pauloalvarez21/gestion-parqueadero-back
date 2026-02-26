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
public class EspacioDTO {
    private Long id;
    private String codigo;
    private String tipoVehiculoPermitido;
    private String estado;
    private BigDecimal tarifaBase;
    private boolean ocupado;
}
