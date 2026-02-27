package com.parqueadero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.dto.VehiculoDTO;
import com.parqueadero.exception.VehiculoNoEncontradoException;
import com.parqueadero.service.VehiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehiculoController.class)
class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehiculoService vehiculoService;

    private VehiculoDTO vehiculoDTO;

    @BeforeEach
    void setUp() {
        vehiculoDTO = new VehiculoDTO();
        vehiculoDTO.setId(1L);
        vehiculoDTO.setPlaca("FNS-541");
        vehiculoDTO.setMarca("Ford");
        vehiculoDTO.setTipo("CARRO");
    }

    @Test
    void obtenerVehiculoPorPlaca_cuandoVehiculoExiste_deberiaRetornar200_y_VehiculoDTO() throws Exception {
        // Arrange
        String placa = "FNS-541";
        when(vehiculoService.obtenerVehiculoPorPlaca(placa)).thenReturn(vehiculoDTO);

        // Act & Assert
        mockMvc.perform(get("/api/vehiculos/{placa}", placa)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value(placa))
                .andExpect(jsonPath("$.marca").value("Ford"));
    }

    @Test
    void obtenerVehiculoPorPlaca_cuandoVehiculoNoExiste_deberiaRetornar404() throws Exception {
        // Arrange
        String placa = "XYZ-999";
        String mensajeError = "El vehículo con placa " + placa + " no fue encontrado.";
        when(vehiculoService.obtenerVehiculoPorPlaca(placa))
                .thenThrow(new VehiculoNoEncontradoException(mensajeError));

        // Act & Assert
        mockMvc.perform(get("/api/vehiculos/{placa}", placa)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensajeError));
    }

    @Test
    void registrarVehiculo_deberiaRetornar201() throws Exception {
        when(vehiculoService.registrarVehiculo(any(VehiculoDTO.class))).thenReturn(vehiculoDTO);

        mockMvc.perform(post("/api/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa").value("FNS-541"));
    }

    @Test
    void listarVehiculos_deberiaRetornarLista() throws Exception {
        when(vehiculoService.listarTodos()).thenReturn(List.of(vehiculoDTO));

        mockMvc.perform(get("/api/vehiculos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}