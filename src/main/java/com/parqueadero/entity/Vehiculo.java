// ============ ENTITIES ============
package com.parqueadero.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.parqueadero.enums.TipoVehiculo;


@Entity
@Table(name = "vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 20)
    private String placa;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVehiculo tipo;
    
    @Column(length = 50)
    private String marca;
    
    @Column(length = 50)
    private String modelo;
    
    @Column(length = 20)
    private String color;
    
    @Column(name = "nombre_propietario", length = 100)
    private String nombrePropietario;
    
    @Column(name = "telefono_propietario", length = 20)
    private String telefonoPropietario;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}
