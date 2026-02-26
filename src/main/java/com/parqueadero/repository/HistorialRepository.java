package com.parqueadero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.parqueadero.entity.Historial;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {
    List<Historial> findByPlacaVehiculo(String placa);
    List<Historial> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);
}