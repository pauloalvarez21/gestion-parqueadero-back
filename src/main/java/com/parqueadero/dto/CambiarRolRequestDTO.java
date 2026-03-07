package com.parqueadero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CambiarRolRequestDTO {
    @NotBlank(message = "El nuevo rol no puede estar vacío.")
    private String newRole;
}