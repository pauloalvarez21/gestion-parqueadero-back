package com.parqueadero.dto;

import lombok.Data;

@Data
public class EliminarEspaciosRequest {
    private String tipoVehiculo;
    private int cantidad;
}