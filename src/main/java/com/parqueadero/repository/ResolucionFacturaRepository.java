package com.parqueadero.repository;

import com.parqueadero.entity.ResolucionFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResolucionFacturaRepository extends JpaRepository<ResolucionFactura, Long> {

    @Query("SELECT r FROM ResolucionFactura r WHERE r.activa = true AND r.fechaFin >= CURRENT_DATE")
    Optional<ResolucionFactura> findActiveResolution();

    Optional<ResolucionFactura> findByNumeroResolucion(String numeroResolucion);
}
