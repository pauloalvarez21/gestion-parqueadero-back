package com.parqueadero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.dto.*;
import com.parqueadero.service.ParqueaderoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.math.BigDecimal;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParqueaderoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParqueaderoService parqueaderoService;

    @Test
    @WithMockUser(authorities = "OPERADOR")
    void registrarEntrada_deberiaRetornar200() throws Exception {
        EntradaRequest request = new EntradaRequest();
        request.setPlaca("ABC-123");
        request.setTipoVehiculo("CARRO");

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setCodigo("TK-123");

        when(parqueaderoService.registrarEntrada(any(EntradaRequest.class))).thenReturn(ticketDTO);

        mockMvc.perform(post("/api/parqueadero/entrada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("TK-123"));
    }

    @Test
    @WithMockUser(authorities = "OPERADOR")
    void registrarSalida_deberiaRetornar200() throws Exception {
        SalidaRequest request = new SalidaRequest();
        request.setCodigoTicket("TK-123");

        PagoResponse pagoResponse = new PagoResponse();
        pagoResponse.setValorTotal(BigDecimal.valueOf(5000.0));

        when(parqueaderoService.registrarSalida(any(SalidaRequest.class))).thenReturn(pagoResponse);

        mockMvc.perform(post("/api/parqueadero/salida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal").value(5000.0));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void obtenerEstadisticas_cuandoAdmin_deberiaRetornar200() throws Exception {
        EstadisticasDTO estadisticasDTO = new EstadisticasDTO();
        estadisticasDTO.setIngresosHoy(BigDecimal.valueOf(10000.0));

        when(parqueaderoService.obtenerEstadisticas()).thenReturn(estadisticasDTO);

        mockMvc.perform(get("/api/parqueadero/estadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingresosHoy").value(10000.0));

    }

    @Test
    @WithMockUser(authorities = "OPERADOR")
    void obtenerEstadisticas_cuandoNoAdmin_deberiaRetornar403() throws Exception {
        mockMvc.perform(get("/api/parqueadero/estadisticas"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void agregarEspacios_cuandoAdmin_deberiaRetornar200() throws Exception {
        AgregarEspaciosRequest request = new AgregarEspaciosRequest();
        request.setCantidad(5);
        request.setTipoVehiculo("CARRO");

        when(parqueaderoService.agregarEspacios(any(AgregarEspaciosRequest.class))).thenReturn(List.of());

        mockMvc.perform(post("/api/parqueadero/espacios/agregar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
