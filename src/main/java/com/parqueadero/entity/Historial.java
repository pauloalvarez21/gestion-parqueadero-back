package com.parqueadero.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Historial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "placa_vehiculo", nullable = false, length = 20)
    private String placaVehiculo;
    
    @Column(name = "codigo_espacio", nullable = false, length = 10)
    private String codigoEspacio;
    
    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;
    
    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;
    
    @Column(name = "duracion_minutos")
    private Long duracionMinutos;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(name = "finalizado_por")
    private String finalizadoPor;

    @Column(name = "numero_factura", length = 30)
    private String numeroFactura;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
