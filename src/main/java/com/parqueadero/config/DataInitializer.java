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
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(1) // Se ejecuta primero para asegurar la estructura base
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
        log.info("--- INICIALIZADOR DE DATOS ESENCIALES ---");
        crearTarifasGlobalesSiNoExisten();
        crearAdminSiNoExiste();
        crearEspaciosSiNoExisten();
        log.info("--- INICIALIZACIÓN DE DATOS ESENCIALES COMPLETADA ---");
    }

    private void crearTarifasGlobalesSiNoExisten() {
        if (tarifaRepository.count() == 0) {
            log.info("Creando tarifas globales por defecto...");
            List<Tarifa> tarifas = List.of(
                    Tarifa.builder().tipoTarifa(TipoTarifa.POR_MINUTO).valor(new BigDecimal("50.00")).build(),
                    Tarifa.builder().tipoTarifa(TipoTarifa.POR_DIA).valor(new BigDecimal("15000.00")).build(),
                    Tarifa.builder().tipoTarifa(TipoTarifa.POR_MES).valor(new BigDecimal("200000.00")).build()
            );
            tarifaRepository.saveAll(tarifas);
            log.info("Tarifas globales creadas.");
        }
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
            log.info("Usuario administrador '{}' creado con contraseña '{}'.", adminUsername, adminPassword);
        }
    }

    private void crearEspaciosSiNoExisten() {
        if (espacioRepository.count() == 0) {
            log.info("Creando espacios iniciales de parqueadero...");
            List<Espacio> nuevosEspacios = new ArrayList<>();
            
            // Crear espacios para CARRO
            for (int i = 1; i <= cuposCarro; i++) {
                Espacio espacio = new Espacio();
                espacio.setCodigo("C-" + i);
                espacio.setTipoVehiculoPermitido(TipoVehiculo.CARRO);
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setTarifaBase(new BigDecimal("3000.00"));
                nuevosEspacios.add(espacio);
            }

            // Crear espacios para MOTO
            for (int i = 1; i <= cuposMoto; i++) {
                Espacio espacio = new Espacio();
                espacio.setCodigo("M-" + i);
                espacio.setTipoVehiculoPermitido(TipoVehiculo.MOTO);
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setTarifaBase(new BigDecimal("1000.00"));
                nuevosEspacios.add(espacio);
            }

            // Crear espacios para BICICLETA
            for (int i = 1; i <= cuposBicicleta; i++) {
                Espacio espacio = new Espacio();
                espacio.setCodigo("B-" + i);
                espacio.setTipoVehiculoPermitido(TipoVehiculo.BICICLETA);
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setTarifaBase(new BigDecimal("500.00"));
                nuevosEspacios.add(espacio);
            }
            
            if (!nuevosEspacios.isEmpty()) {
                espacioRepository.saveAll(nuevosEspacios);
                log.info("{} espacios creados exitosamente ({} Carro, {} Moto, {} Bicicleta).", nuevosEspacios.size(), cuposCarro, cuposMoto, cuposBicicleta);
            }
        }
    }
}