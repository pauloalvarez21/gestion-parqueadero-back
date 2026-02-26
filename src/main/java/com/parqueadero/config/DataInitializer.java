// ============ DATA INITIALIZER ============
package com.parqueadero.config;

import com.parqueadero.entity.Espacio;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.repository.EspacioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initEspacios(EspacioRepository espacioRepository) {
        return args -> {
            if (espacioRepository.count() == 0) {
                List<Espacio> espacios = new ArrayList<>();
                
                // Espacios para carros (20)
                for (int i = 1; i <= 20; i++) {
                    espacios.add(Espacio.builder()
                        .codigo("C-" + String.format("%02d", i))
                        .tipoVehiculoPermitido(TipoVehiculo.CARRO)
                        .estado(EstadoEspacio.DISPONIBLE)
                        .tarifaBase(new BigDecimal("2000"))
                        .build());
                }
                
                // Espacios para motos (10)
                for (int i = 1; i <= 10; i++) {
                    espacios.add(Espacio.builder()
                        .codigo("M-" + String.format("%02d", i))
                        .tipoVehiculoPermitido(TipoVehiculo.MOTO)
                        .estado(EstadoEspacio.DISPONIBLE)
                        .tarifaBase(new BigDecimal("1000"))
                        .build());
                }
                
                // Espacios para camiones (5)
                for (int i = 1; i <= 5; i++) {
                    espacios.add(Espacio.builder()
                        .codigo("T-" + String.format("%02d", i))
                        .tipoVehiculoPermitido(TipoVehiculo.CAMION)
                        .estado(EstadoEspacio.DISPONIBLE)
                        .tarifaBase(new BigDecimal("5000"))
                        .build());
                }
                
                // Espacios para bicicletas (5)
                for (int i = 1; i <= 5; i++) {
                    espacios.add(Espacio.builder()
                        .codigo("B-" + String.format("%02d", i))
                        .tipoVehiculoPermitido(TipoVehiculo.BICICLETA)
                        .estado(EstadoEspacio.DISPONIBLE)
                        .tarifaBase(new BigDecimal("500"))
                        .build());
                }
                
                espacioRepository.saveAll(espacios);
                System.out.println("✅ Espacios inicializados: " + espacios.size());
            }
        };
    }
}
