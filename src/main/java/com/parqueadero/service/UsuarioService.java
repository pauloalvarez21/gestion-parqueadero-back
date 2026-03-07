package com.parqueadero.service;

import com.parqueadero.dto.CambiarRolRequestDTO;
import com.parqueadero.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> getAllUsers();

    UsuarioDTO cambiarRol(String username, CambiarRolRequestDTO request);
}