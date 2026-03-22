package com.parqueadero.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "resoluciones_factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolucionFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String numeroResolucion;
    
    @Column(nullable = false)
    private LocalDate fechaResolucion;
    
    @Column(length = 10)
    private String prefijo;
    
    @Column(nullable = false)
    private Long numeroDesde;
    
    @Column(nullable = false)
    private Long numeroHasta;
    
    @Column(nullable = false)
    private Long numeroActual;
    
    @Column(nullable = false)
    private LocalDate fechaInicio;
    
    @Column(nullable = false)
    private LocalDate fechaFin;
    
    @Column(nullable = false)
    private boolean activa;
    
    @Column(name = "mensaje_pie_pagina", length = 500)
    private String mensajePiePagina;

    @Column(name = "nit_empresa", length = 20)
    private String nitEmpresa;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
    }
}
