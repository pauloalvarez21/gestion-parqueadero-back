package com.parqueadero.entity;

import com.parqueadero.enums.EstadoTicket;
import com.parqueadero.enums.TipoTarifa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "espacio_id", nullable = false)
    private Espacio espacio;
    
    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;
    
    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tarifa")
    private TipoTarifa tipoTarifa;
    

    @Column(name = "valor_base", precision = 10, scale = 2)
    private BigDecimal valorBase;
    
    @Column(name = "valor_adicional", precision = 10, scale = 2)
    private BigDecimal valorAdicional;
    
    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTicket estado;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por_id")
    private Usuario creadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finalizado_por_id")
    private Usuario finalizadoPor;
}
