package com.parqueadero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.entity.Tarifa;
import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.repository.TarifaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TarifaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TarifaRepository tarifaRepository;

    private Tarifa tarifa;

    @BeforeEach
    void setUp() {
        tarifa = new Tarifa();
        tarifa.setTipoVehiculo(TipoVehiculo.CARRO);
        tarifa.setValor(BigDecimal.valueOf(100.0));
    }


    @Test
    @WithMockUser
    void listarTarifas_deberiaRetornar200() throws Exception {
        when(tarifaRepository.findAll()).thenReturn(List.of(tarifa));

        mockMvc.perform(get("/api/tarifas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoVehiculo").value("CARRO"));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void guardarOActualizar_cuandoAdmin_deberiaRetornar200() throws Exception {
        when(tarifaRepository.findByTipoVehiculo(any(TipoVehiculo.class))).thenReturn(Optional.of(tarifa));
        when(tarifaRepository.save(any(Tarifa.class))).thenReturn(tarifa);


        mockMvc.perform(post("/api/tarifas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarifa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(100.0));
    }
}
