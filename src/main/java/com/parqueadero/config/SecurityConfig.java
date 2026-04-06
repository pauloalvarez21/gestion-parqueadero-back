package com.parqueadero.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @org.springframework.beans.factory.annotation.Value("${cors.allowed-origins:http://localhost:4200}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 1. Desactiva CSRF (Soluciona el 403 en POST)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 2. Habilita y configura CORS
                .authorizeHttpRequests(authorize -> authorize
                        // 0. Permitir explícitamente peticiones OPTIONS (Preflight)
                        .requestMatchers(org.springframework.web.cors.CorsUtils::isPreFlightRequest).permitAll()
                        // 1. Endpoints públicos (autenticación y documentación)
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**"
                        ).permitAll()

                        // 2. Endpoints de Administración (solo ADMIN)
                        .requestMatchers(
                                "/api/parqueadero/estadisticas",
                                "/api/usuarios/**" // Gestión completa de usuarios
                        ).hasAuthority("ADMIN")
                        // Endpoints específicos de ADMIN para gestionar el parqueadero
                        .requestMatchers(HttpMethod.POST, "/api/parqueadero/espacios/agregar", "/api/parqueadero/tarifas").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/parqueadero/tarifas/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/parqueadero/espacios/eliminar", "/api/parqueadero/tarifas/**").hasAuthority("ADMIN")

                        // 3. Endpoints de Tarifas directos (TarifaController)
                        .requestMatchers(HttpMethod.POST, "/api/tarifas/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/tarifas/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/tarifas/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/tarifas/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // 4. Endpoints Operativos (ADMIN y OPERADOR)
                        // Corregido: los paths del controlador son /entrada y /salida
                        .requestMatchers("/api/parqueadero/entrada", "/api/parqueadero/salida").hasAnyAuthority("ADMIN", "OPERADOR")
                        .requestMatchers(HttpMethod.GET, "/api/parqueadero/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // 5. Endpoints de Vehículos (ADMIN y OPERADOR)
                        .requestMatchers("/api/vehiculos/**").hasAnyAuthority("ADMIN", "OPERADOR")

                        // 6. Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(allowedOrigins); // Usa patrones para mayor flexibilidad
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type")); // Expone headers al frontend
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica la configuración a todas las rutas
        return source;
    }
}