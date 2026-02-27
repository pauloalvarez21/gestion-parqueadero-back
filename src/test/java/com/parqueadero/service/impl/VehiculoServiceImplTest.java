package com.parqueadero.service.impl;

import com.parqueadero.dto.VehiculoDTO;
import com.parqueadero.entity.Vehiculo;
import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.exception.VehiculoNoEncontradoException;
import com.parqueadero.mapper.ParqueaderoMapper;
import com.parqueadero.repository.VehiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiculoServiceImplTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ParqueaderoMapper mapper;

    @InjectMocks
    private VehiculoServiceImpl vehiculoService;

    private Vehiculo vehiculo;
    private VehiculoDTO vehiculoDTO;

    @BeforeEach
    void setUp() {
        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPlaca("ABC-123");
        vehiculo.setTipo(TipoVehiculo.CARRO);
        vehiculo.setMarca("Toyota");

        vehiculoDTO = new VehiculoDTO();
        vehiculoDTO.setId(1L);
        vehiculoDTO.setPlaca("ABC-123");
        vehiculoDTO.setTipo("CARRO");
        vehiculoDTO.setMarca("Toyota");
    }

    @Test
    void obtenerVehiculoPorPlaca_cuandoVehiculoExiste_deberiaRetornarVehiculoDTO() {
        // 1. Arrange (Preparar)
        String placa = "ABC-123";
        // Simulamos que el repositorio encuentra el vehículo
        when(vehiculoRepository.findByPlaca(placa.toUpperCase())).thenReturn(Optional.of(vehiculo));
        // Simulamos que el mapper convierte la entidad a DTO
        when(mapper.toVehiculoDTO(any(Vehiculo.class))).thenReturn(vehiculoDTO);

        // 2. Act (Actuar)
        VehiculoDTO resultado = vehiculoService.obtenerVehiculoPorPlaca(placa);

        // 3. Assert (Afirmar)
        assertNotNull(resultado);
        assertEquals(placa, resultado.getPlaca());
        assertEquals("Toyota", resultado.getMarca());
    }

    @Test
    void obtenerVehiculoPorPlaca_cuandoVehiculoNoExiste_deberiaLanzarExcepcion() {
        // 1. Arrange (Preparar)
        String placa = "XYZ-789";
        // Simulamos que el repositorio NO encuentra el vehículo
        when(vehiculoRepository.findByPlaca(placa.toUpperCase())).thenReturn(Optional.empty());

        // 2. Act & 3. Assert (Actuar y Afirmar)
        assertThrows(VehiculoNoEncontradoException.class, () -> vehiculoService.obtenerVehiculoPorPlaca(placa));
    }

    @Test
    void registrarVehiculo_cuandoEsNuevo_deberiaGuardar() {
        when(vehiculoRepository.existsByPlaca(anyString())).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);
        when(mapper.toVehiculoDTO(any(Vehiculo.class))).thenReturn(vehiculoDTO);

        VehiculoDTO resultado = vehiculoService.registrarVehiculo(vehiculoDTO);

        assertNotNull(resultado);
        assertEquals("ABC-123", resultado.getPlaca());
    }

    @Test
    void registrarVehiculo_cuandoYaExiste_deberiaLanzarExcepcion() {
        when(vehiculoRepository.existsByPlaca(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> vehiculoService.registrarVehiculo(vehiculoDTO));
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        when(vehiculoRepository.findAll()).thenReturn(List.of(vehiculo));
        when(mapper.toVehiculoDTO(any(Vehiculo.class))).thenReturn(vehiculoDTO);

        List<VehiculoDTO> lista = vehiculoService.listarTodos();
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void buscarPorPlaca_deberiaRetornarLista() {
        when(vehiculoRepository.buscarPorPlacaParcial(anyString())).thenReturn(List.of(vehiculo));
        when(mapper.toVehiculoDTO(any(Vehiculo.class))).thenReturn(vehiculoDTO);

        List<VehiculoDTO> lista = vehiculoService.buscarPorPlaca("ABC");
        assertFalse(lista.isEmpty());
    }

    @Test
    void actualizarVehiculo_cuandoExiste_deberiaActualizar() {
        when(vehiculoRepository.findById(anyLong())).thenReturn(Optional.of(vehiculo));
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);
        when(mapper.toVehiculoDTO(any(Vehiculo.class))).thenReturn(vehiculoDTO);

        VehiculoDTO resultado = vehiculoService.actualizarVehiculo(1L, vehiculoDTO);
        assertNotNull(resultado);
    }

    @Test
    void eliminarVehiculo_deberiaLlamarRepository() {
        vehiculoService.eliminarVehiculo(1L);
        verify(vehiculoRepository, times(1)).deleteById(1L);
    }
}
 