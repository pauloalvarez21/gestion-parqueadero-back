package com.parqueadero.entity;

import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.TipoVehiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "espacios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Espacio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 10)
    private String codigo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVehiculo tipoVehiculoPermitido;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEspacio estado;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal tarifaBase;
}
