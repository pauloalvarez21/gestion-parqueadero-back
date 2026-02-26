package com.parqueadero.controller;

import com.parqueadero.dto.VehiculoDTO;
import com.parqueadero.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping
    public ResponseEntity<VehiculoDTO> registrarVehiculo(@RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO nuevoVehiculo = vehiculoService.registrarVehiculo(vehiculoDTO);
        return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED);
    }

    @GetMapping("/{placa}")
    public ResponseEntity<VehiculoDTO> obtenerVehiculoPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculoPorPlaca(placa));
    }

    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> listarVehiculos(@RequestParam(required = false) String placa) {
        if (placa != null && !placa.isEmpty()) {
            return ResponseEntity.ok(vehiculoService.buscarPorPlaca(placa));
        }
        return ResponseEntity.ok(vehiculoService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoDTO> actualizarVehiculo(@PathVariable Long id, @RequestBody VehiculoDTO vehiculoDTO) {
        return ResponseEntity.ok(vehiculoService.actualizarVehiculo(id, vehiculoDTO));
    }
}