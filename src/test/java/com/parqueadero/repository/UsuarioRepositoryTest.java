package com.parqueadero.repository;

import com.parqueadero.entity.Usuario;
import com.parqueadero.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void findByUsername_deberiaRetornarUsuario_cuandoExiste() {
        // Arrange
        Usuario usuario = Usuario.builder()
                .username("testuser")
                .password("password")
                .role(Role.OPERADOR)
                .build();
        entityManager.persist(usuario);
        entityManager.flush();

        // Act
        Optional<Usuario> encontrado = usuarioRepository.findByUsername("testuser");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getUsername()).isEqualTo("testuser");
        assertThat(encontrado.get().getRole()).isEqualTo(Role.OPERADOR);
    }

    @Test
    void findByUsername_deberiaRetornarEmpty_cuandoNoExiste() {
        // Act
        Optional<Usuario> encontrado = usuarioRepository.findByUsername("user_inexistente");

        // Assert
        assertThat(encontrado).isEmpty();
    }
}
