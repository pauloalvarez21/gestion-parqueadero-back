package com.parqueadero.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void should_HaveThreeValues() {
        // Arrange & Act
        Role[] values = Role.values();

        // Assert
        assertNotNull(values);
        assertEquals(3, values.length);
    }

    @Test
    void should_ReturnAdmin() {
        // Arrange & Act
        Role role = Role.ADMIN;

        // Assert
        assertNotNull(role);
        assertEquals("ADMIN", role.name());
        assertEquals(0, role.ordinal());
    }

    @Test
    void should_ReturnOperador() {
        // Arrange & Act
        Role role = Role.OPERADOR;

        // Assert
        assertNotNull(role);
        assertEquals("OPERADOR", role.name());
        assertEquals(1, role.ordinal());
    }

    @Test
    void should_ReturnUser() {
        // Arrange & Act
        Role role = Role.USER;

        // Assert
        assertNotNull(role);
        assertEquals("USER", role.name());
        assertEquals(2, role.ordinal());
    }

    @Test
    void valueOf_deberiaRetornarRoleValido() {
        // Arrange & Act
        Role role1 = Role.valueOf("ADMIN");
        Role role2 = Role.valueOf("OPERADOR");
        Role role3 = Role.valueOf("USER");

        // Assert
        assertEquals(Role.ADMIN, role1);
        assertEquals(Role.OPERADOR, role2);
        assertEquals(Role.USER, role3);
    }

    @Test
    void values_deberiaRetornarArrayConTodosLosRoles() {
        // Arrange & Act
        Role[] values = Role.values();

        // Assert
        assertArrayEquals(
            new Role[]{Role.ADMIN, Role.OPERADOR, Role.USER},
            values
        );
    }

    @Test
    void should_AllValuesAreUnique() {
        // Arrange
        Role[] values = Role.values();

        // Act & Assert
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i], values[j]);
            }
        }
    }

    @Test
    void toString_deberiaRetornarNombreDelRole() {
        // Arrange & Act
        String admin = Role.ADMIN.toString();
        String operador = Role.OPERADOR.toString();

        // Assert
        assertEquals("ADMIN", admin);
        assertEquals("OPERADOR", operador);
    }

    @Test
    void should_OrdinalValuesAreSequential() {
        // Arrange & Act
        Role[] values = Role.values();

        // Assert
        for (int i = 0; i < values.length; i++) {
            assertEquals(i, values[i].ordinal());
        }
    }

    @Test
    void valueOf_deberiaLanzarExcepcionParaValorInvalido() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Role.valueOf("INVALIDO");
        });
    }

    @Test
    void valueOf_deberiaSerCaseSensitive() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Role.valueOf("admin");
        });
    }

    @Test
    void should_AdminHasHighestPriority() {
        // Arrange
        Role admin = Role.ADMIN;

        // Assert
        assertEquals(0, admin.ordinal());
    }

    @Test
    void should_UserHasLowestPriority() {
        // Arrange
        Role user = Role.USER;

        // Assert
        assertEquals(2, user.ordinal());
    }
}
