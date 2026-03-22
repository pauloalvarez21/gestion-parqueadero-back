package com.parqueadero.service.impl;

import com.parqueadero.dto.CambiarRolRequestDTO;
import com.parqueadero.dto.UsuarioDTO;
import com.parqueadero.entity.Usuario;
import com.parqueadero.enums.Role;
import com.parqueadero.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .username("admin")
                .role(Role.ADMIN)
                .build();

        usuarioDTO = UsuarioDTO.builder()
                .id(1L)
                .username("admin")
                .role("ADMIN")
                .build();
        
        ReflectionTestUtils.setField(usuarioService, "adminUsername", "admin");
    }

    @Test
    void getAllUsers_deberiaRetornarListaDeUsuarios() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        // Act
        List<UsuarioDTO> result = usuarioService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getUsername());
        assertEquals("ADMIN", result.get(0).getRole());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void cambiarRol_cuandoExito_deberiaActualizarYRetornarDTO() {
        // Arrange
        String username = "admin";
        CambiarRolRequestDTO request = new CambiarRolRequestDTO();
        request.setNewRole("OPERADOR");
        
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDTO result = usuarioService.cambiarRol(username, request);

        // Assert
        assertNotNull(result);
        assertEquals("OPERADOR", result.getRole());
        assertEquals(Role.OPERADOR, usuario.getRole());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void cambiarRol_cuandoUsuarioNoExiste_deberiaLanzarUsernameNotFoundException() {
        // Arrange
        String username = "inexistente";
        CambiarRolRequestDTO request = new CambiarRolRequestDTO();
        request.setNewRole("OPERADOR");

        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioService.cambiarRol(username, request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void cambiarRol_cuandoRolInvalido_deberiaLanzarIllegalArgumentException() {
        // Arrange
        String username = "admin";
        CambiarRolRequestDTO request = new CambiarRolRequestDTO();
        request.setNewRole("ROLE_INVALIDO");

        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> usuarioService.cambiarRol(username, request));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
    @Test
    void eliminarUsuario_cuandoExito_deberiaDesactivarUsuario() {
        // Arrange
        String username = "otroUser";
        Usuario otroUsuario = Usuario.builder().id(2L).username(username).activo(true).build();
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(otroUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(otroUsuario);

        // Act
        usuarioService.eliminarUsuario(username);

        // Assert
        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        assertFalse(otroUsuario.isActivo());
    }

    @Test
    void eliminarUsuario_cuandoUsuarioNoExiste_deberiaLanzarUsernameNotFoundException() {
        // Arrange
        String username = "inexistente";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioService.eliminarUsuario(username));
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
    @Test
    void eliminarUsuario_cuandoAdmin_deberiaLanzarException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuarioService.eliminarUsuario("admin"));
        assertEquals("No se puede eliminar el usuario administrador principal.", exception.getMessage());
        verify(usuarioRepository, never()).delete(any());
    }
}
