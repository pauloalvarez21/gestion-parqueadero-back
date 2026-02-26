// ============ REPOSITORIES ============
package com.parqueadero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.parqueadero.entity.Vehiculo;
import com.parqueadero.enums.TipoVehiculo;

import java.util.Optional;
import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
    List<Vehiculo> findByTipo(TipoVehiculo tipo);
    
    @Query("SELECT v FROM Vehiculo v WHERE v.placa LIKE %:placa%")
    List<Vehiculo> buscarPorPlacaParcial(String placa);
}