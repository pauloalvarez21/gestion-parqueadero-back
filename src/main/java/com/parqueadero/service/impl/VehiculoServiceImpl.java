package com.parqueadero.service.impl;

import com.parqueadero.dto.VehiculoDTO;
import com.parqueadero.entity.Vehiculo;
import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.exception.VehiculoNoEncontradoException;
import com.parqueadero.mapper.ParqueaderoMapper;
import com.parqueadero.repository.VehiculoRepository;
import com.parqueadero.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ParqueaderoMapper mapper;

    @Override
    @Transactional
    public VehiculoDTO registrarVehiculo(VehiculoDTO dto) {
        if (vehiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new IllegalArgumentException("Ya existe un vehículo con placa: " + dto.getPlaca());
        }
        
        Vehiculo vehiculo = Vehiculo.builder()
            .placa(dto.getPlaca().toUpperCase())
            .tipo(TipoVehiculo.valueOf(dto.getTipo().toUpperCase()))
            .marca(dto.getMarca())
            .modelo(dto.getModelo())
            .color(dto.getColor())
            .nombrePropietario(dto.getNombrePropietario())
            .telefonoPropietario(dto.getTelefonoPropietario())
            .build();
        
        return mapper.toVehiculoDTO(vehiculoRepository.save(vehiculo));
    }

    @Override
    public VehiculoDTO obtenerVehiculoPorPlaca(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa.toUpperCase())
            .orElseThrow(() -> new VehiculoNoEncontradoException("Vehículo no encontrado: " + placa));
        return mapper.toVehiculoDTO(vehiculo);
    }

    @Override
    public List<VehiculoDTO> listarTodos() {
        return vehiculoRepository.findAll()
            .stream()
            .map(mapper::toVehiculoDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<VehiculoDTO> buscarPorPlaca(String placa) {
        return vehiculoRepository.buscarPorPlacaParcial(placa.toUpperCase())
            .stream()
            .map(mapper::toVehiculoDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehiculoDTO actualizarVehiculo(Long id, VehiculoDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
            .orElseThrow(() -> new VehiculoNoEncontradoException("Vehículo no encontrado"));
        
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setColor(dto.getColor());
        vehiculo.setNombrePropietario(dto.getNombrePropietario());
        vehiculo.setTelefonoPropietario(dto.getTelefonoPropietario());
        
        return mapper.toVehiculoDTO(vehiculoRepository.save(vehiculo));
    }

    @Override
    @Transactional
    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }
}
