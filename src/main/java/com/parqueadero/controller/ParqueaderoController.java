package com.parqueadero.controller;

import com.parqueadero.dto.*;
import com.parqueadero.service.ParqueaderoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parqueadero")
@RequiredArgsConstructor
public class ParqueaderoController {

    private final ParqueaderoService parqueaderoService;

    @Operation(summary = "Registra la entrada de un vehículo",
               description = "Crea un nuevo ticket para un vehículo que ingresa al parqueadero. Si el vehículo no existe, se crea.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Entrada registrada exitosamente"),
                   @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o vehículo ya estacionado", content = @Content),
                   @ApiResponse(responseCode = "404", description = "No hay espacios disponibles para el tipo de vehículo", content = @Content)
               })
    @PostMapping("/entrada")
    public ResponseEntity<TicketDTO> registrarEntrada(@RequestBody EntradaRequest request) {
        TicketDTO ticket = parqueaderoService.registrarEntrada(request);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Registra la salida de un vehículo y calcula el pago")
    @PostMapping("/salida")
    public ResponseEntity<PagoResponse> registrarSalida(@RequestBody SalidaRequest request) {
        PagoResponse pago = parqueaderoService.registrarSalida(request);
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/tickets/{codigo}")
    public ResponseEntity<TicketDTO> obtenerTicket(@PathVariable String codigo) {
        return ResponseEntity.ok(parqueaderoService.obtenerTicket(codigo));
    }

    @GetMapping("/tickets/activos")
    public ResponseEntity<List<TicketDTO>> listarTicketsActivos() {
        return ResponseEntity.ok(parqueaderoService.listarTicketsActivos());
    }

    @GetMapping("/espacios")
    public ResponseEntity<List<EspacioDTO>> listarEspacios() {
        return ResponseEntity.ok(parqueaderoService.listarEspacios());
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasDTO> obtenerEstadisticas() {
        return ResponseEntity.ok(parqueaderoService.obtenerEstadisticas());
    }
}