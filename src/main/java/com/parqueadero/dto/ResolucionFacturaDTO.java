package com.parqueadero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolucionFacturaDTO {
    private String numeroResolucion;
    private LocalDate fechaResolucion;
    private String prefijo;
    private Long numeroDesde;
    private Long numeroHasta;
    private Long numeroActual;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activa;
    private String mensajePiePagina;
    private String nitEmpresa;
}
