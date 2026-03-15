package com.parqueadero.config;

import com.parqueadero.entity.*;
import com.parqueadero.enums.*;
import com.parqueadero.enums.Role;
import com.parqueadero.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Profile("dev") // IMPORTANTE: Solo corre en entorno de desarrollo
@Order(2) // Se ejecuta después de DataInitializer
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
        // Verificamos si ya hay datos de prueba para no duplicarlos
        if (usuarioRepository.findByUsername("operador").isPresent()) {
            log.info("El usuario 'operador' ya existe. Omitiendo DataSeeder de desarrollo.");
            return;
        }

        log.info("Iniciando carga de datos de prueba para DEV...");

        // 1. Asegurar usuario de prueba 'operador' (admin lo crea DataInitializer)
        crearUsuarioOperador();

        // 3. Obtener Espacios existentes (creados por DataInitializer)
        List<Espacio> todosEspacios = espacioRepository.findAll();
        
        List<Espacio> espaciosCarro = todosEspacios.stream()
                .filter(e -> e.getTipoVehiculoPermitido() == TipoVehiculo.CARRO)
                .toList();
        
        List<Espacio> espaciosMoto = todosEspacios.stream()
                .filter(e -> e.getTipoVehiculoPermitido() == TipoVehiculo.MOTO)
                .toList();

        if (espaciosCarro.isEmpty() || espaciosMoto.isEmpty()) {
            log.warn("No se encontraron espacios para CARRO o MOTO. Saltando la creación de tickets de prueba. Verifica que DataInitializer se haya ejecutado.");
            return;
        }

        // 4. Crear Escenarios de Tickets
        // Se asume que DataInitializer ya creó espacios, por lo que las listas no estarán vacías.
        // Si lo estuvieran, se lanzará una excepción, lo cual es correcto en este contexto.
        
        // Escenario A: Carro que entró hace 2.5 horas (Debe cobrar 3 horas)
        crearTicketActivo(
                "AAA-111", 
                TipoVehiculo.CARRO, 
                espaciosCarro.get(0), 
                LocalDateTime.now().minusHours(2).minusMinutes(30)
        );

        // Escenario B: Moto que entró hace 15 minutos (Debe cobrar 1 hora o fracción según config)
        crearTicketActivo(
                "MMM-222", 
                TipoVehiculo.MOTO, 
                espaciosMoto.get(0), 
                LocalDateTime.now().minusMinutes(15)
        );

        // Escenario C: Camión que entró hace 5 horas
        crearTicketActivo(
                "KKK-555",
                TipoVehiculo.CAMION,
                todosEspacios.stream().filter(e -> e.getTipoVehiculoPermitido() == TipoVehiculo.CAMION).findFirst().get(),
                LocalDateTime.now().minusHours(5)
        );

        // Escenario D: Carro que ya salió (Historial)
        crearHistorial("OLD-999", TipoVehiculo.CARRO);

        log.info("¡Datos de prueba cargados exitosamente!");
    }

    private void crearUsuarioOperador() {
        if (usuarioRepository.findByUsername("operador").isEmpty()) {
            Usuario operador = Usuario.builder()
                    .username("operador")
                    .password(passwordEncoder.encode("operador123"))
                    .role(Role.OPERADOR)
                    .build();
            usuarioRepository.save(operador);
            log.info("Usuario 'operador' de prueba creado.");
        }
    }

    private void crearTicketActivo(String placa, TipoVehiculo tipo, Espacio espacio, LocalDateTime entrada) {
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
                .tipoTarifa(TipoTarifa.POR_HORA)
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
