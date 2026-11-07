package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de las entidades del Contexto de Revisión de Pares ({@link Revisor} y {@link Revision}).
 * Demuestra la semántica de Entidad (identidad única en el tiempo).
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 */
@DisplayName("Pruebas de Entidades de Revisión de Pares")
class RevisionTest {

    private Revisor revisor;

    @BeforeEach
    void setUp() {
        revisor = new Revisor("DOC-123", "Ing. Paola Bedoya", "pbedoya@unicauca.edu.co", "Ingeniería de Software");
    }

    @Test
    @DisplayName("No debe permitir crear revisor con ID nulo o vacío")
    void noDebePermitirRevisorSinId() {
        assertThrows(ReglaNegocioException.class, () ->
                new Revisor(null, "Docente", "email@unicauca.edu.co", "Área")
        );
        assertThrows(ReglaNegocioException.class, () ->
                new Revisor("   ", "Docente", "email@unicauca.edu.co", "Área")
        );
    }

    @Test
    @DisplayName("No debe permitir crear revisor con nombre vacío")
    void noDebePermitirRevisorSinNombre() {
        assertThrows(ReglaNegocioException.class, () ->
                new Revisor("DOC-001", "   ", "email@unicauca.edu.co", "Área")
        );
    }

    @Test
    @DisplayName("Semántica de Entidad: Dos revisores con el mismo ID docente son la misma entidad")
    void dosRevisoresConMismoIdSonLaMismaEntidad() {
        Revisor r1 = new Revisor("DOC-100", "Carlos Perez", "cperez@unicauca.edu.co", "Sistemas");
        Revisor r2 = new Revisor("DOC-100", "Carlos Perez Actualizado", "nuevo@unicauca.edu.co", "Ciencias");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    @DisplayName("No debe permitir crear una revisión sin ID o sin revisor")
    void noDebePermitirRevisionSinIdOSinRevisor() {
        assertThrows(ReglaNegocioException.class, () ->
                new Revision(null, revisor, Dictamen.APROBADO, "Ok", LocalDate.now())
        );
        assertThrows(ReglaNegocioException.class, () ->
                new Revision(1L, null, Dictamen.APROBADO, "Ok", LocalDate.now())
        );
    }

    @Test
    @DisplayName("Invariante de Revisión: No se permite rechazar sin observaciones pedagógicas detalladas")
    void noDebePermitirRechazoSinObservacionesDetalladas() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new Revision(2L, revisor, Dictamen.RECHAZADO, "Mal", LocalDate.now())
        );
        assertEquals("Un dictamen de rechazo o ajuste requiere observaciones detalladas (mínimo 10 caracteres).", ex.getMessage());
    }

    @Test
    @DisplayName("Debe permitir crear revisión aprobada con fecha por defecto")
    void debeCrearRevisionAprobada() {
        Revision rev = new Revision(3L, revisor, Dictamen.APROBADO, "Excelente reactivo", null);

        assertNotNull(rev);
        assertEquals(Dictamen.APROBADO, rev.getDictamen());
        assertNotNull(rev.getFechaRevision());
    }

    @Test
    @DisplayName("Semántica de Entidad: Dos revisiones con el mismo ID representan la misma evaluación")
    void dosRevisionesConMismoIdSonEquivalentes() {
        Revision rev1 = new Revision(50L, revisor, Dictamen.APROBADO, "Obs 1", LocalDate.now());
        Revision rev2 = new Revision(50L, revisor, Dictamen.RECHAZADO, "Observaciones válidas para rechazo.", LocalDate.now());

        assertEquals(rev1, rev2);
    }
}
