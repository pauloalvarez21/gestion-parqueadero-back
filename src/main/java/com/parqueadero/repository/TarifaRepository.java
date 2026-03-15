package com.parqueadero.repository;

import com.parqueadero.entity.Tarifa;
import com.parqueadero.enums.TipoTarifa;
import com.parqueadero.enums.TipoVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findByTipoVehiculoAndTipoTarifa(TipoVehiculo tipoVehiculo, TipoTarifa tipoTarifa);
    Optional<Tarifa> findByTipoVehiculo(TipoVehiculo tipoVehiculo);
}