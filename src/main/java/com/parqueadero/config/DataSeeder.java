package com.parqueadero.config;

import com.parqueadero.entity.*;
import com.parqueadero.enums.*;
import com.parqueadero.enums.Role;
import com.parqueadero.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Profile("dev") // IMPORTANTE: Solo corre en entorno de desarrollo
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EspacioRepository espacioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TicketRepository ticketRepository;
    private final HistorialRepository historialRepository;
    private final TarifaRepository tarifaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verificamos si ya hay tickets para no duplicar la data de prueba
        if (ticketRepository.count() > 0) {
            log.info("Ya existen tickets en la base de datos. Omitiendo DataSeeder de desarrollo.");
            return;
        }

        log.info("Iniciando carga de datos de prueba para DEV...");

        // 1. Asegurar usuario 'user' (admin ya lo crea DataInitializer)
        crearUsuarioUser();

        // 2. Asegurar Tarifas (por si no existen)
        crearTarifasSiNoExisten();

        // 3. Obtener Espacios existentes (creados por DataInitializer: C-1, M-1...)
        // Nota: DataInitializer usa formato "C-1", no "C-01"
        List<Espacio> todosEspacios = espacioRepository.findAll();
        
        List<Espacio> espaciosCarro = todosEspacios.stream()
                .filter(e -> e.getTipoVehiculoPermitido() == TipoVehiculo.CARRO)
                .toList();
        
        List<Espacio> espaciosMoto = todosEspacios.stream()
                .filter(e -> e.getTipoVehiculoPermitido() == TipoVehiculo.MOTO)
                .toList();

        if (espaciosCarro.isEmpty()) {
            log.info("No se encontraron espacios de CARRO. Creando 10 espacios...");
            espaciosCarro = crearEspacios(TipoVehiculo.CARRO, 10, new BigDecimal("3000"));
        }
        if (espaciosMoto.isEmpty()) {
            log.info("No se encontraron espacios de MOTO. Creando 10 espacios...");
            espaciosMoto = crearEspacios(TipoVehiculo.MOTO, 10, new BigDecimal("1000"));
        }

        // 4. Crear Escenarios de Tickets
        
        // Escenario A: Carro que entró hace 2.5 horas (Debe cobrar 3 horas)
        crearTicketActivo(
                "AAA-111", 
                TipoVehiculo.CARRO, 
                espaciosCarro.get(0), 
                LocalDateTime.now().minusHours(2).minusMinutes(30),
                TipoTarifa.POR_HORA
        );

        // Escenario B: Moto que entró hace 15 minutos (Debe cobrar 1 hora o fracción según config)
        crearTicketActivo(
                "MMM-222", 
                TipoVehiculo.MOTO, 
                espaciosMoto.get(0), 
                LocalDateTime.now().minusMinutes(15),
                TipoTarifa.POR_HORA
        );

        // Escenario C: Carro que ya salió (Historial)
        crearHistorial("OLD-999", TipoVehiculo.CARRO);

        log.info("¡Datos de prueba cargados exitosamente!");
    }

    private void crearUsuarioUser() {
        if (usuarioRepository.findByUsername("user").isEmpty()) {
            Usuario user = Usuario.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build();
            usuarioRepository.save(user);
        }
    }

    private void crearTarifasSiNoExisten() {
        if (tarifaRepository.count() == 0) {
            List<Tarifa> tarifas = new ArrayList<>();
            tarifas.add(new Tarifa(null, TipoTarifa.POR_MINUTO, new BigDecimal("50")));
            tarifas.add(new Tarifa(null, TipoTarifa.POR_HORA, new BigDecimal("3000")));
            tarifas.add(new Tarifa(null, TipoTarifa.POR_DIA, new BigDecimal("30000")));
            tarifas.add(new Tarifa(null, TipoTarifa.POR_MES, new BigDecimal("150000")));
            tarifaRepository.saveAll(tarifas);
        }
    }

    private List<Espacio> crearEspacios(TipoVehiculo tipo, int cantidad, BigDecimal tarifaBase) {
        List<Espacio> espacios = new ArrayList<>();
        String prefijo = tipo == TipoVehiculo.CARRO ? "C-" : "M-";
        
        for (int i = 1; i <= cantidad; i++) {
            Espacio e = new Espacio();
            e.setCodigo(prefijo + i); 
            e.setTipoVehiculoPermitido(tipo);
            e.setEstado(EstadoEspacio.DISPONIBLE);
            e.setTarifaBase(tarifaBase);
            espacios.add(e);
        }
        return espacioRepository.saveAll(espacios);
    }

    private void crearTicketActivo(String placa, TipoVehiculo tipo, Espacio espacio, LocalDateTime entrada, TipoTarifa tarifa) {
        // 1. Crear o buscar vehículo
        Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa)
                .orElseGet(() -> vehiculoRepository.save(
                        Vehiculo.builder().placa(placa).tipo(tipo).build()
                ));

        // 2. Ocupar el espacio
        espacio.setEstado(EstadoEspacio.OCUPADO);
        espacioRepository.save(espacio);

        // 3. Crear Ticket
        Ticket ticket = Ticket.builder()
                .codigo("TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .vehiculo(vehiculo)
                .espacio(espacio)
                .horaEntrada(entrada)
                .tipoTarifa(tarifa)
                .estado(EstadoTicket.ACTIVO)
                .build();
        
        ticketRepository.save(ticket);
        log.info("Ticket creado: {} - Placa: {} - Entrada: {}", ticket.getCodigo(), placa, entrada);
    }

    private void crearHistorial(String placa, TipoVehiculo tipo) {
        // Simular un ticket que ya se pagó y cerró
        // Aseguramos que el vehículo exista en la BD (opcional para historial, pero consistente)
        vehiculoRepository.findByPlaca(placa).orElseGet(() -> 
            vehiculoRepository.save(Vehiculo.builder().placa(placa).tipo(tipo).build())
        );
        
        LocalDateTime entrada = LocalDateTime.now().minusDays(1).minusHours(2);
        LocalDateTime salida = LocalDateTime.now().minusDays(1);
        
        Historial h = Historial.builder()
                .placaVehiculo(placa)
                .codigoEspacio("C-1") // Usamos un código válido referencial
                .horaEntrada(entrada)
                .horaSalida(salida)
                .duracionMinutos(120L)
                .valorTotal(new BigDecimal("6000"))
                .build();
        
        historialRepository.save(h);
    }
}
