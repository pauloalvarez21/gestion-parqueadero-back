package com.parqueadero.repository;

import com.parqueadero.entity.Tarifa;
import com.parqueadero.enums.TipoTarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findByTipoTarifa(TipoTarifa tipoTarifa);
}