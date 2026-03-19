package com.parqueadero.service.impl;

import com.parqueadero.dto.CambiarRolRequestDTO;
import com.parqueadero.dto.UsuarioDTO;
import com.parqueadero.entity.Usuario;
import com.parqueadero.enums.Role;
import com.parqueadero.repository.UsuarioRepository;
import com.parqueadero.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Value("${admin.user.username:admin}")
    private String adminUsername;

    @Override
    public List<UsuarioDTO> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> UsuarioDTO.builder()
                        .id(usuario.getId())
                        .username(usuario.getUsername())
                        .role(usuario.getRole().name())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioDTO cambiarRol(String username, CambiarRolRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el username: " + username));

        Role newRole;
        try {
            // Convierte el string del rol a mayúsculas para que coincida con el Enum
            newRole = Role.valueOf(request.getNewRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: '" + request.getNewRole() + "'. Roles válidos son: " + Arrays.toString(Role.values()));
        }

        usuario.setRole(newRole);
        usuarioRepository.save(usuario);

        return UsuarioDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .role(usuario.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public void eliminarUsuario(String username) {
        if (username.equalsIgnoreCase(adminUsername)) {
            throw new IllegalArgumentException("No se puede eliminar el usuario administrador principal.");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el username: " + username));
        usuarioRepository.delete(usuario);
    }
}