package com.parqueadero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalidaRequest {
    private String codigoTicket;
    private String placa;
    private String observaciones;
}  
