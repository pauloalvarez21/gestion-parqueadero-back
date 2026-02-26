package com.parqueadero.repository;

import com.parqueadero.entity.Espacio;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.TipoVehiculo;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface EspacioRepository extends JpaRepository<Espacio, Long> {

    // Este método bloquea la fila seleccionada hasta que la transacción termine, evitando race conditions.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Espacio> findFirstByTipoVehiculoPermitidoAndEstadoOrderByIdAsc(TipoVehiculo tipoVehiculo, EstadoEspacio estado);

    List<Espacio> findByEstado(EstadoEspacio estado);
    long countByEstado(EstadoEspacio estado);
}