// ============ SERVICES ============
package com.parqueadero.service;

import com.parqueadero.dto.*;
import java.util.List;

public interface VehiculoService {
    VehiculoDTO registrarVehiculo(VehiculoDTO vehiculoDTO);
    VehiculoDTO obtenerVehiculoPorPlaca(String placa);
    List<VehiculoDTO> listarTodos();
    List<VehiculoDTO> buscarPorPlaca(String placa);
    VehiculoDTO actualizarVehiculo(Long id, VehiculoDTO vehiculoDTO);
    void eliminarVehiculo(Long id);
}
