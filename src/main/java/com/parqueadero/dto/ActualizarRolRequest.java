package com.parqueadero.dto;

import com.parqueadero.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarRolRequest {
    @NotNull(message = "El rol no puede ser nulo")
    private Role role;
}