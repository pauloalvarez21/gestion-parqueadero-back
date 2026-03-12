package com.parqueadero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.config.JwtService;
import com.parqueadero.dto.AuthResponse;
import com.parqueadero.dto.LoginRequest;
import com.parqueadero.dto.RegisterRequest;
import com.parqueadero.entity.Usuario;
import com.parqueadero.enums.Role;
import com.parqueadero.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .username("testuser")
                .password("encoded_password")
                .role(Role.USER)
                .build();
    }

    @Test
    void register_deberiaRetornar200_cuandoEsValido() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setRole(Role.USER);

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("fake_token");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake_token"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void register_deberiaRetornar409_cuandoUsuarioYaExiste() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_deberiaRetornar200_cuandoEsValido() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("fake_token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake_token"));
    }

    @Test
    void eliminarUsuario_deberiaRetornar204_cuandoExiste() throws Exception {
        String username = "testuser";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        mockMvc.perform(delete("/api/auth/eliminar/{username}", username))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarUsuario_deberiaRetornar404_cuandoNoExiste() throws Exception {
        String username = "inexistente";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/auth/eliminar/{username}", username))
                .andExpect(status().isNotFound());
    }
}
