package com.parqueadero.controller;

import com.parqueadero.dto.CambiarRolRequestDTO;
import com.parqueadero.dto.UsuarioDTO;
import com.parqueadero.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Para la documentación de Swagger
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Lista todos los usuarios y sus roles (Solo para Admin)",
               description = "Devuelve una lista de todos los usuarios registrados en el sistema. Requiere rol de ADMIN.")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsers() {
        return ResponseEntity.ok(usuarioService.getAllUsers());
    }

    @Operation(summary = "Cambia el rol de un usuario existente (Solo para Admin)",
               description = "Actualiza el rol de un usuario específico. Requiere rol de ADMIN.")
    @PutMapping("/{username}/rol")
    public ResponseEntity<UsuarioDTO> cambiarRol(@PathVariable String username, @Valid @RequestBody CambiarRolRequestDTO request) {
        UsuarioDTO usuarioActualizado = usuarioService.cambiarRol(username, request);
        return ResponseEntity.ok(usuarioActualizado);
    }
}