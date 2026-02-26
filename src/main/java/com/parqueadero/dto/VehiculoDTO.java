package com.parqueadero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoDTO {
    private Long id;
    private String placa;
    private String tipo;
    private String marca;
    private String modelo;
    private String color;
    private String nombrePropietario;
    private String telefonoPropietario;
    private LocalDateTime fechaRegistro;
}
