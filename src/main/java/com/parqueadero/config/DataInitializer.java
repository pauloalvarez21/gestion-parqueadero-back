package com.parqueadero.config;

import com.parqueadero.entity.Espacio;
import com.parqueadero.entity.Tarifa;
import com.parqueadero.entity.ResolucionFactura;
import com.parqueadero.entity.Usuario;
import com.parqueadero.enums.EstadoEspacio;
import com.parqueadero.enums.Role;

import com.parqueadero.enums.TipoTarifa;
import com.parqueadero.enums.TipoVehiculo;
import com.parqueadero.entity.Vehiculo;
import com.parqueadero.repository.EspacioRepository;
import com.parqueadero.repository.ResolucionFacturaRepository;
import com.parqueadero.repository.TarifaRepository;
import com.parqueadero.repository.UsuarioRepository;
import com.parqueadero.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final ResolucionFacturaRepository resolucionFacturaRepository;
    private final VehiculoRepository vehiculoRepository;
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

    @Value("${parqueadero.cupos.camion:5}")
    private int cuposCamion;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("--- INICIALIZADOR DE DATOS ESENCIALES ---");
        crearTarifasGlobalesSiNoExisten();
        crearAdminSiNoExiste();
        crearEspaciosSiNoExisten();
        crearResolucionFacturaSiNoExiste();
        crearVehiculosPruebaSiNoExisten();
        log.info("--- INICIALIZACIÓN DE DATOS ESENCIALES COMPLETADA ---");
    }

    private void crearTarifasGlobalesSiNoExisten() {
        log.info("Verificando tarifas globales...");
        
        // Tarifas para CARRO
        verificarYCrearTarifa(TipoVehiculo.CARRO, TipoTarifa.POR_MINUTO, new BigDecimal("50.00"));
        verificarYCrearTarifa(TipoVehiculo.CARRO, TipoTarifa.POR_HORA, new BigDecimal("3000.00"));
        verificarYCrearTarifa(TipoVehiculo.CARRO, TipoTarifa.POR_DIA, new BigDecimal("25000.00"));
        verificarYCrearTarifa(TipoVehiculo.CARRO, TipoTarifa.POR_MES, new BigDecimal("150000.00"));
        verificarYCrearTarifa(TipoVehiculo.CARRO, TipoTarifa.FRACCION, new BigDecimal("1000.00"));

        // Tarifas para MOTO
        verificarYCrearTarifa(TipoVehiculo.MOTO, TipoTarifa.POR_MINUTO, new BigDecimal("20.00"));
        verificarYCrearTarifa(TipoVehiculo.MOTO, TipoTarifa.POR_HORA, new BigDecimal("1000.00"));
        verificarYCrearTarifa(TipoVehiculo.MOTO, TipoTarifa.POR_DIA, new BigDecimal("8000.00"));
        verificarYCrearTarifa(TipoVehiculo.MOTO, TipoTarifa.POR_MES, new BigDecimal("60000.00"));
        verificarYCrearTarifa(TipoVehiculo.MOTO, TipoTarifa.FRACCION, new BigDecimal("500.00"));

        // Tarifas para BICICLETA
        verificarYCrearTarifa(TipoVehiculo.BICICLETA, TipoTarifa.POR_MINUTO, new BigDecimal("5.00"));
        verificarYCrearTarifa(TipoVehiculo.BICICLETA, TipoTarifa.POR_HORA, new BigDecimal("500.00"));
        verificarYCrearTarifa(TipoVehiculo.BICICLETA, TipoTarifa.POR_DIA, new BigDecimal("3000.00"));
        verificarYCrearTarifa(TipoVehiculo.BICICLETA, TipoTarifa.POR_MES, new BigDecimal("20000.00"));
        verificarYCrearTarifa(TipoVehiculo.BICICLETA, TipoTarifa.FRACCION, new BigDecimal("200.00"));

        // Tarifas para CAMION
        verificarYCrearTarifa(TipoVehiculo.CAMION, TipoTarifa.POR_MINUTO, new BigDecimal("100.00"));
        verificarYCrearTarifa(TipoVehiculo.CAMION, TipoTarifa.POR_HORA, new BigDecimal("6000.00"));
        verificarYCrearTarifa(TipoVehiculo.CAMION, TipoTarifa.POR_DIA, new BigDecimal("50000.00"));
        verificarYCrearTarifa(TipoVehiculo.CAMION, TipoTarifa.POR_MES, new BigDecimal("300000.00"));
        verificarYCrearTarifa(TipoVehiculo.CAMION, TipoTarifa.FRACCION, new BigDecimal("2000.00"));

        log.info("Verificación de tarifas globales completada.");
    }

    private void verificarYCrearTarifa(TipoVehiculo vehiculo, TipoTarifa tipo, BigDecimal valor) {
        if (tarifaRepository.findByTipoVehiculoAndTipoTarifa(vehiculo, tipo).isEmpty()) {
            tarifaRepository.save(Tarifa.builder()
                    .tipoVehiculo(vehiculo)
                    .tipoTarifa(tipo)
                    .valor(valor)
                    .build());
            log.info("Tarifa creada: {} - {} - {}", vehiculo, tipo, valor);
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
            log.info("Usuario administrador '{}' creado exitosamente.", adminUsername);
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

            // Crear espacios para CAMION
            for (int i = 1; i <= cuposCamion; i++) {
                Espacio espacio = new Espacio();
                espacio.setCodigo("K-" + i); // K de Camión
                espacio.setTipoVehiculoPermitido(TipoVehiculo.CAMION);
                espacio.setEstado(EstadoEspacio.DISPONIBLE);
                espacio.setTarifaBase(new BigDecimal("6000.00"));
                nuevosEspacios.add(espacio);
            }
            
            if (!nuevosEspacios.isEmpty()) {
                espacioRepository.saveAll(nuevosEspacios);
                log.info("{} espacios creados exitosamente ({} Carro, {} Moto, {} Bicicleta, {} Camion).", 
                    nuevosEspacios.size(), cuposCarro, cuposMoto, cuposBicicleta, cuposCamion);
            }
        }
    }

    private void crearResolucionFacturaSiNoExiste() {
        if (resolucionFacturaRepository.count() == 0) {
            log.info("Creando resolución de facturación DIAN inicial...");
            ResolucionFactura resolucion = ResolucionFactura.builder()
                    .numeroResolucion("187640000001")
                    .fechaResolucion(LocalDate.now())
                    .prefijo("SETT")
                    .numeroDesde(1L)
                    .numeroHasta(5000L)
                    .numeroActual(0L)
                    .fechaInicio(LocalDate.now())
                    .fechaFin(LocalDate.now().plusYears(1))
                    .activa(true)
                    .mensajePiePagina("Factura de prueba autorizada por la DIAN")
                    .nitEmpresa("900123456-1")
                    .build();
            resolucionFacturaRepository.save(resolucion);
            log.info("Resolución de facturación DIAN creada exitosamente: {} {}-{}", 
                resolucion.getPrefijo(), resolucion.getNumeroDesde(), resolucion.getNumeroHasta());
        }
    }

    private void crearVehiculosPruebaSiNoExisten() {
        log.info("Verificando existencia de vehículos de prueba...");
        
        if (vehiculoRepository.findByPlaca("ABC123").isEmpty()) {
            log.info("Creando carro de prueba ABC123...");
            Vehiculo carro = Vehiculo.builder()
                .placa("ABC123")
                .tipo(TipoVehiculo.CARRO)
                .marca("Toyota")
                .modelo("Corolla 2024")
                .color("Blanco Perlado")
                .nombrePropietario("Juan Perez")
                .telefonoPropietario("3001234567")
                .build();
            vehiculoRepository.save(carro);
        }

        if (vehiculoRepository.findByPlaca("XYZ789").isEmpty()) {
            log.info("Creando moto de prueba XYZ789...");
            Vehiculo moto = Vehiculo.builder()
                .placa("XYZ789")
                .tipo(TipoVehiculo.MOTO)
                .marca("Yamaha")
                .modelo("MT-03")
                .color("Negro Mate")
                .nombrePropietario("Maria Lopez")
                .telefonoPropietario("3119876543")
                .build();
            vehiculoRepository.save(moto);
        }
    }
}
