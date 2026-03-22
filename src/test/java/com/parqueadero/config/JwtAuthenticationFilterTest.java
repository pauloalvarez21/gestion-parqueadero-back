package com.parqueadero.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void doFilterInternal_deberiaPasarSinAutenticar_cuandoNoHayHeaderAuthorization() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaPasarSinAutenticar_cuandoHeaderNoEmpiezaConBearer() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic token123");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaAutenticar_cuandoTokenEsValido() throws Exception {
        // Arrange
        String jwt = "valid-jwt-token";
        String username = "testuser";
        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(true);

        SecurityContextHolder.setContext(securityContext);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        ArgumentCaptor<org.springframework.security.core.Authentication> authCaptor = 
            ArgumentCaptor.forClass(org.springframework.security.core.Authentication.class);
        verify(securityContext).setAuthentication(authCaptor.capture());
        
        var auth = authCaptor.getValue();
        assertNotNull(auth);
        assertEquals(username, auth.getName());
        assertTrue(auth.isAuthenticated());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_noDeberiaAutenticar_cuandoUsernameEsNull() throws Exception {
        // Arrange
        String jwt = "valid-jwt-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_noDeberiaAutenticar_cuandoYaHayAutenticacion() throws Exception {
        // Arrange
        String jwt = "valid-jwt-token";
        String username = "testuser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(mock(org.springframework.security.core.Authentication.class));

        SecurityContextHolder.setContext(securityContext);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_noDeberiaAutenticar_cuandoTokenEsInvalido() throws Exception {
        // Arrange
        String jwt = "invalid-jwt-token";
        String username = "testuser";
        UserDetails mockUserDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(mockUserDetails);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(false);

        SecurityContextHolder.setContext(securityContext);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaManejarExpiredJwtException() throws Exception {
        // Arrange
        String jwt = "expired-jwt-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenThrow(new ExpiredJwtException(null, null, "Token expirado"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaManejarMalformedJwtException() throws Exception {
        // Arrange
        String jwt = "malformed-jwt-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenThrow(new MalformedJwtException("Token malformado"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaManejarSignatureException() throws Exception {
        // Arrange
        String jwt = "invalid-signature-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenThrow(new SignatureException("Firma inválida"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaManejarUsernameNotFoundException() throws Exception {
        // Arrange
        String jwt = "valid-jwt-token";
        String username = "nonexistent";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(
            new org.springframework.security.core.userdetails.UsernameNotFoundException("Usuario no encontrado")
        );

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaManejarIllegalArgumentException() throws Exception {
        // Arrange
        String jwt = "invalid-jwt-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenThrow(new IllegalArgumentException("Token inválido"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    void doFilterInternal_deberiaEstablecerDetailsEnAuthentication() throws Exception {
        // Arrange
        String jwt = "valid-jwt-token";
        String username = "testuser";
        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(true);

        SecurityContextHolder.setContext(securityContext);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        ArgumentCaptor<org.springframework.security.core.Authentication> authCaptor = 
            ArgumentCaptor.forClass(org.springframework.security.core.Authentication.class);
        verify(securityContext).setAuthentication(authCaptor.capture());
        
        var auth = authCaptor.getValue();
        assertNotNull(auth.getDetails());
    }
}
