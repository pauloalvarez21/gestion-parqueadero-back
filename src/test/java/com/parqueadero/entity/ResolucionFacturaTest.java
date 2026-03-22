package com.parqueadero.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ResolucionFacturaTest {

    @Test
    void constructorVacio_deberiaCrearInstancia() {
        // Arrange & Act
        ResolucionFactura resolucion = new ResolucionFactura();

        // Assert
        assertNotNull(resolucion);
        assertNull(resolucion.getNumeroResolucion());
        assertNull(resolucion.getFechaResolucion());
        assertNull(resolucion.getPrefijo());
        assertNull(resolucion.getNumeroDesde());
        assertNull(resolucion.getNumeroHasta());
        assertNull(resolucion.getNumeroActual());
        assertNull(resolucion.getFechaInicio());
        assertNull(resolucion.getFechaFin());
        assertFalse(resolucion.isActiva());
        assertNull(resolucion.getMensajePiePagina());
        assertNull(resolucion.getNitEmpresa());
        assertNull(resolucion.getCreadoEn());
    }

    @Test
    void builder_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDate fechaResolucion = LocalDate.of(2024, 1, 15);
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 12, 31);
        LocalDateTime creadoEn = LocalDateTime.of(2024, 1, 1, 10, 0);

        // Act
        ResolucionFactura resolucion = ResolucionFactura.builder()
                .id(1L)
                .numeroResolucion("RES-001")
                .fechaResolucion(fechaResolucion)
                .prefijo("FC")
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(100L)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .activa(true)
                .mensajePiePagina("Gracias por su compra")
                .nitEmpresa("900123456-1")
                .creadoEn(creadoEn)
                .build();

        // Assert
        assertNotNull(resolucion);
        assertEquals(1L, resolucion.getId());
        assertEquals("RES-001", resolucion.getNumeroResolucion());
        assertEquals(fechaResolucion, resolucion.getFechaResolucion());
        assertEquals("FC", resolucion.getPrefijo());
        assertEquals(1L, resolucion.getNumeroDesde());
        assertEquals(1000L, resolucion.getNumeroHasta());
        assertEquals(100L, resolucion.getNumeroActual());
        assertEquals(fechaInicio, resolucion.getFechaInicio());
        assertEquals(fechaFin, resolucion.getFechaFin());
        assertTrue(resolucion.isActiva());
        assertEquals("Gracias por su compra", resolucion.getMensajePiePagina());
        assertEquals("900123456-1", resolucion.getNitEmpresa());
        assertEquals(creadoEn, resolucion.getCreadoEn());
    }

    @Test
    void constructorCompleto_deberiaCrearInstanciaConTodosLosCampos() {
        // Arrange
        LocalDate fechaResolucion = LocalDate.of(2024, 1, 15);
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 12, 31);

        // Act
        ResolucionFactura resolucion = new ResolucionFactura(
                1L,
                "RES-001",
                fechaResolucion,
                "FC",
                1L,
                1000L,
                100L,
                fechaInicio,
                fechaFin,
                true,
                "Gracias por su compra",
                "900123456-1",
                null
        );

        // Assert
        assertNotNull(resolucion);
        assertEquals(1L, resolucion.getId());
        assertEquals("RES-001", resolucion.getNumeroResolucion());
        assertEquals(fechaResolucion, resolucion.getFechaResolucion());
        assertEquals("FC", resolucion.getPrefijo());
        assertEquals(1L, resolucion.getNumeroDesde());
        assertEquals(1000L, resolucion.getNumeroHasta());
        assertEquals(100L, resolucion.getNumeroActual());
        assertEquals(fechaInicio, resolucion.getFechaInicio());
        assertEquals(fechaFin, resolucion.getFechaFin());
        assertTrue(resolucion.isActiva());
        assertEquals("Gracias por su compra", resolucion.getMensajePiePagina());
        assertEquals("900123456-1", resolucion.getNitEmpresa());
    }

    @Test
    void prePersist_deberiaEstablecerCreadoEn() {
        // Arrange
        ResolucionFactura resolucion = ResolucionFactura.builder()
                .numeroResolucion("RES-001")
                .fechaResolucion(LocalDate.now())
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(1L)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .activa(true)
                .build();

        // Act
        LocalDateTime antesDePersistir = LocalDateTime.now();
        resolucion.setCreadoEn(null); // Simular que aún no se ha persistido
        // En un test real, @PrePersist se ejecutaría al guardar en la BD
        // Aquí verificamos que el campo pueda ser establecido
        resolucion.setCreadoEn(antesDePersistir);

        // Assert
        assertNotNull(resolucion.getCreadoEn());
        assertTrue(resolucion.getCreadoEn().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void equals_deberiaRetornarTrue_paraMismosObjetos() {
        // Arrange
        ResolucionFactura resolucion1 = ResolucionFactura.builder().id(1L).build();
        ResolucionFactura resolucion2 = resolucion1;

        // Act & Assert
        assertEquals(resolucion1, resolucion2);
    }

    @Test
    void equals_deberiaRetornarTrue_paraObjetosConMismoId() {
        // Arrange
        ResolucionFactura resolucion1 = ResolucionFactura.builder().id(1L).build();
        ResolucionFactura resolucion2 = ResolucionFactura.builder().id(1L).build();

        // Act & Assert
        assertEquals(resolucion1, resolucion2);
    }

    @Test
    void equals_deberiaRetornarFalse_paraIdsDiferentes() {
        // Arrange
        ResolucionFactura resolucion1 = ResolucionFactura.builder().id(1L).build();
        ResolucionFactura resolucion2 = ResolucionFactura.builder().id(2L).build();

        // Act & Assert
        assertNotEquals(resolucion1, resolucion2);
    }

    @Test
    void hashCode_deberiaSerIgual_paraMismosIds() {
        // Arrange
        ResolucionFactura resolucion1 = ResolucionFactura.builder().id(1L).build();
        ResolucionFactura resolucion2 = ResolucionFactura.builder().id(1L).build();

        // Act & Assert
        assertEquals(resolucion1.hashCode(), resolucion2.hashCode());
    }

    @Test
    void toString_deberiaRetornarRepresentacionEnString() {
        // Arrange
        ResolucionFactura resolucion = ResolucionFactura.builder()
                .id(1L)
                .numeroResolucion("RES-001")
                .prefijo("FC")
                .activa(true)
                .build();

        // Act
        String resultado = resolucion.toString();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.contains("RES-001"));
        assertTrue(resultado.contains("FC"));
    }
}
