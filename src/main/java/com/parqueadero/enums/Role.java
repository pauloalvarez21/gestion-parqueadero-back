package com.parqueadero.enums;

public enum Role {
    ADMIN,      // Acceso total: Configuración, Tarifas, Usuarios, Reportes
    OPERADOR,    // Acceso operativo: Registrar entradas/salidas, ver disponibilidad
    USER        // Acceso normal: Consultar estadísticas, ver historial
}
