package com.parqueadero.controller;

import com.parqueadero.entity.Tarifa;
import com.parqueadero.enums.TipoTarifa;
import com.parqueadero.repository.TarifaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://gaelectronica.infinityfreeapp.com", "http://localhost:4200"}, allowedHeaders = "*", exposedHeaders = "Authorization")
public class TarifaController {

    private final TarifaRepository tarifaRepository;

    @GetMapping
    public ResponseEntity<List<Tarifa>> listarTarifas() {
        return ResponseEntity.ok(tarifaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Tarifa> guardarOActualizar(@RequestBody Tarifa tarifa) {
        Tarifa guardada = tarifaRepository.findByTipoVehiculoAndTipoTarifa(tarifa.getTipoVehiculo(), tarifa.getTipoTarifa())
                .map(existing -> {
                    existing.setValor(tarifa.getValor());
                    return tarifaRepository.save(existing);
                })
                .orElseGet(() -> tarifaRepository.save(tarifa));
        return ResponseEntity.ok(guardada);
    }
}