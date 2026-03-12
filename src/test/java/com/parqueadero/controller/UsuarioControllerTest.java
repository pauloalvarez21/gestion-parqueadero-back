package com.parqueadero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.dto.CambiarRolRequestDTO;
import com.parqueadero.dto.UsuarioDTO;
import com.parqueadero.service.UsuarioService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .username("testuser")
                .role("OPERADOR")
                .build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllUsers_cuandoAdmin_deberiaRetornar200() throws Exception {
        when(usuarioService.getAllUsers()).thenReturn(List.of(usuarioDTO));

        mockMvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(authorities = "OPERADOR")
    void getAllUsers_cuandoNoEsAdmin_deberiaRetornar403() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void cambiarRol_cuandoAdmin_deberiaRetornar200() throws Exception {
        String username = "testuser";
        CambiarRolRequestDTO request = new CambiarRolRequestDTO();
        request.setNewRole("ADMIN");

        UsuarioDTO usuarioActualizado = UsuarioDTO.builder()
                .id(1L)
                .username("testuser")
                .role("ADMIN")
                .build();

        when(usuarioService.cambiarRol(eq(username), any(CambiarRolRequestDTO.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/api/usuarios/{username}/rol", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}
