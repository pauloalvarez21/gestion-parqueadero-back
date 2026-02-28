package com.parqueadero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntradaRequest {
    private String placa;
    private String tipoVehiculo;
    private String tipoTarifa;
}
