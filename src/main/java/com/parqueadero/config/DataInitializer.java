package com.parqueadero.config;

import com.parqueadero.entity.Espacio;
import com.parqueadero.entity.Tarifa;
import com.parqueadero.entity.Usuario;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.Role;
import com.parqueadero.enums.TipoTarifa;
import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.repository.EspacioRepository;
import com.parqueadero.repository.TarifaRepository;
import com.parqueadero.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final TarifaRepository tarifaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EspacioRepository espacioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.user.username:admin}")
    private String adminUsername;

    @Value("${admin.user.password:admin123}")
    private String adminPassword;

    @Value("${parqueadero.cupos.carro:10}")
    private int cuposCarro;

    @Value("${parqueadero.cupos.moto:10}")
    private int cuposMoto;

    @Value("${parqueadero.cupos.bicicleta:10}")
    private int cuposBicicleta;

    @Override
    public void run(String... args) throws Exception {
        log.info("Inicializando tarifas por defecto...");
        crearTarifaSiNoExiste(TipoTarifa.POR_MINUTO, new BigDecimal("50.00"));
        crearTarifaSiNoExiste(TipoTarifa.POR_DIA, new BigDecimal("15000.00"));
        crearTarifaSiNoExiste(TipoTarifa.POR_MES, new BigDecimal("200000.00"));
        log.info("Tarifas inicializadas correctamente.");

        log.info("Verificando usuario administrador...");
        crearAdminSiNoExiste();

        log.info("Verificando espacios de parqueadero...");
        crearEspaciosSiNoExisten();
    }

    private void crearTarifaSiNoExiste(TipoTarifa tipo, BigDecimal valor) {
        tarifaRepository.findByTipoTarifa(tipo).orElseGet(() ->
            tarifaRepository.save(Tarifa.builder().tipoTarifa(tipo).valor(valor).build()));
    }

    private void crearAdminSiNoExiste() {
        if (usuarioRepository.findByUsername(adminUsername).isEmpty()) {
            log.info("Creando usuario administrador por defecto: {}", adminUsername);
            Usuario admin = Usuario.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario administrador creado exitosamente.");
        }
    }

    private void crearEspaciosSiNoExisten() {
        if (espacioRepository.count() == 0) {
            log.info("Creando espacios iniciales...");
            
            // Crear espacios para CARRO
            for (int i = 1; i <= cuposCarro; i++) {
                Espacio espacio = new Espacio();
                espacio.setCodigo("C-" + i);
                espacio.setTipoVehiculoPermitido(TipoVehiculo.CARRO);
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setTarifaBase(new BigDecimal("3000.00"));
                espacioRepository.save(espacio);
            }

            // Crear espacios para MOTO
            for (int i = 1; i <= cuposMoto; i++) {
                Espacio espacio = new Espacio();
                espacio.setCodigo("M-" + i);
                espacio.setTipoVehiculoPermitido(TipoVehiculo.MOTO);
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setTarifaBase(new BigDecimal("1000.00"));
                espacioRepository.save(espacio);
            }

            // Crear espacios para BICICLETA
            for (int i = 1; i <= cuposBicicleta; i++) {
                Espacio espacio = new Espacio();
                espacio.setCodigo("B-" + i);
                espacio.setTipoVehiculoPermitido(TipoVehiculo.BICICLETA);
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setTarifaBase(new BigDecimal("500.00"));
                espacioRepository.save(espacio);
            }
            log.info("Espacios creados exitosamente.");
        }
    }
}