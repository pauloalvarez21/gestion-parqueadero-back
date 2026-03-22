package com.parqueadero.mapper;

import com.parqueadero.dto.ResolucionFacturaDTO;
import com.parqueadero.entity.ResolucionFactura;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResolucionFacturaMapperTest {

    @Autowired
    private ParqueaderoMapper mapper;

    @Test
    void toResolucionDTO_deberiaRetornarNull_cuandoEntidadEsNull() {
        // Act
        ResolucionFacturaDTO resultado = mapper.toResolucionDTO(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    void toResolucionDTO_deberiaMapearTodosLosCampos() {
        // Arrange
        LocalDate fechaResolucion = LocalDate.of(2024, 1, 15);
        LocalDate fechaInicio = LocalDate.of(2024, 1, 1);
        LocalDate fechaFin = LocalDate.of(2024, 12, 31);

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
                .build();

        // Act
        ResolucionFacturaDTO resultado = mapper.toResolucionDTO(resolucion);

        // Assert
        assertNotNull(resultado);
        // El DTO no tiene campo id
        assertEquals("RES-001", resultado.getNumeroResolucion());
        assertEquals(fechaResolucion, resultado.getFechaResolucion());
        assertEquals("FC", resultado.getPrefijo());
        assertEquals(1L, resultado.getNumeroDesde());
        assertEquals(1000L, resultado.getNumeroHasta());
        assertEquals(100L, resultado.getNumeroActual());
        assertEquals(fechaInicio, resultado.getFechaInicio());
        assertEquals(fechaFin, resultado.getFechaFin());
        assertTrue(resultado.isActiva());
        assertEquals("Gracias por su compra", resultado.getMensajePiePagina());
        assertEquals("900123456-1", resultado.getNitEmpresa());
    }

    @Test
    void toResolucionDTO_deberiaMapearCamposNull() {
        // Arrange
        ResolucionFactura resolucion = ResolucionFactura.builder()
                .numeroResolucion("RES-002")
                .fechaResolucion(LocalDate.now())
                .numeroDesde(1L)
                .numeroHasta(1000L)
                .numeroActual(1L)
                .fechaInicio(LocalDate.now())
                .fechaFin(LocalDate.now().plusYears(1))
                .activa(true)
                .build();

        // Act
        ResolucionFacturaDTO resultado = mapper.toResolucionDTO(resolucion);

        // Assert
        assertNotNull(resultado);
        assertEquals("RES-002", resultado.getNumeroResolucion());
        assertNull(resultado.getPrefijo());
        assertNull(resultado.getMensajePiePagina());
        assertNull(resultado.getNitEmpresa());
    }

    @Test
    void toResolucionDTO_deberiaManejarValoresExtremos() {
        // Arrange
        ResolucionFactura resolucion = ResolucionFactura.builder()
                .numeroResolucion("RES-003")
                .fechaResolucion(LocalDate.of(2000, 1, 1))
                .prefijo("A")
                .numeroDesde(0L)
                .numeroHasta(Long.MAX_VALUE)
                .numeroActual(999999L)
                .fechaInicio(LocalDate.of(2000, 1, 1))
                .fechaFin(LocalDate.of(2099, 12, 31))
                .activa(false)
                .mensajePiePagina("")
                .nitEmpresa("")
                .build();

        // Act
        ResolucionFacturaDTO resultado = mapper.toResolucionDTO(resolucion);

        // Assert
        assertNotNull(resultado);
        assertEquals("RES-003", resultado.getNumeroResolucion());
        assertEquals("A", resultado.getPrefijo());
        assertEquals(0L, resultado.getNumeroDesde());
        assertEquals(Long.MAX_VALUE, resultado.getNumeroHasta());
        assertFalse(resultado.isActiva());
        assertEquals("", resultado.getMensajePiePagina());
        assertEquals("", resultado.getNitEmpresa());
    }
}
