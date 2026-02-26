package com.parqueadero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.parqueadero.entity.Ticket;
import com.parqueadero.enums.EstadoTicket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCodigo(String codigo);
    List<Ticket> findByEstado(EstadoTicket estado);
    Long countByEstado(EstadoTicket estado);
    List<Ticket> findByVehiculoPlaca(String placa);
    
    @Query("SELECT t FROM Ticket t WHERE t.estado = 'ACTIVO' AND t.vehiculo.placa = :placa")
    Optional<Ticket> findTicketActivoByPlaca(String placa);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE CAST(t.horaEntrada AS DATE) = CURRENT_DATE")
    Long countTicketsHoy();
    
    @Query("SELECT SUM(t.valorTotal) FROM Ticket t WHERE t.estado = 'PAGADO' AND CAST(t.fechaPago AS DATE) = CURRENT_DATE")
    Double sumIngresosHoy();
    
    @Query("SELECT t FROM Ticket t WHERE t.horaEntrada BETWEEN :inicio AND :fin")
    List<Ticket> findByRangoFechas(LocalDateTime inicio, LocalDateTime fin);
}
