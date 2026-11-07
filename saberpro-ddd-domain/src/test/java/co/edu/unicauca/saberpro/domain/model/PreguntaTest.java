package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de invariantes del Agregado {@link Pregunta} en JUnit 5.
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 */
@DisplayName("Pruebas del Agregado Pregunta (Invariantes de Dominio)")
class PreguntaTest {

    private Pregunta pregunta;
    private Enunciado enunciadoValido;
    private Justificacion justificacionValida;

    @BeforeEach
    void setUp() {
        pregunta = new Pregunta();
        enunciadoValido = new Enunciado("¿Cuál es el propósito principal del patrón Aggregate en DDD?");
        justificacionValida = new Justificacion("El patrón Aggregate garantiza la consistencia transaccional y encapsula las invariantes.");
    }

    @Nested
    @DisplayName("Invariante: Opciones de Respuesta y Estado Borrador")
    class InvariantesOpciones {

        @Test
        @DisplayName("Debe permitir agregar hasta 4 opciones en estado BORRADOR")
        void debePermitirAgregarHastaCuatroOpciones() {
            pregunta.agregarOpcion("Opción A - Distractor 1", false);
            pregunta.agregarOpcion("Opción B - Clave correcta", true);
            pregunta.agregarOpcion("Opción C - Distractor 2", false);
            pregunta.agregarOpcion("Opción D - Distractor 3", false);

            assertEquals(4, pregunta.getOpciones().size());
            assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
        }

        @Test
        @DisplayName("No debe permitir agregar más de 4 opciones a una pregunta")
        void noDebePermitirAgregarMasDeCuatroOpciones() {
            pregunta.agregarOpcion("Opción A", false);
            pregunta.agregarOpcion("Opción B", true);
            pregunta.agregarOpcion("Opción C", false);
            pregunta.agregarOpcion("Opción D", false);

            ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                    pregunta.agregarOpcion("Opción E excedente", false)
            );
            assertEquals("Una pregunta no puede tener más de 4 opciones.", ex.getMessage());
        }

        @Test
        @DisplayName("No debe permitir modificar opciones en un estado diferente de Borrador")
        void noDebePermitirModificarOpcionesEnEstadoDiferenteDeBorrador() {
            pregunta.asignarEnunciado(enunciadoValido);
            pregunta.asignarJustificacion(justificacionValida);
            pregunta.agregarOpcion("Opción A", true);
            pregunta.agregarOpcion("Opción B", false);
            pregunta.agregarOpcion("Opción C", false);
            pregunta.agregarOpcion("Opción D", false);

            pregunta.enviarARevision();
            assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());

            ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                    pregunta.agregarOpcion("Nueva opción en revisión", false)
            );
            assertEquals("Solo se pueden modificar opciones en estado Borrador.", ex.getMessage());
        }

        @Test
        @DisplayName("Debe proteger el encapsulamiento de la lista de opciones (Inmodificabilidad)")
        void debeProtegerEncapsulamientoDeOpcionesContraModificacionesExternas() {
            pregunta.agregarOpcion("Opción A", true);

            assertThrows(UnsupportedOperationException.class, () ->
                    pregunta.getOpciones().add(new OpcionRespuesta("Opción externa maliciosa", false))
            );
        }

        @Test
        @DisplayName("Debe permitir limpiar opciones únicamente en estado Borrador")
        void debePermitirLimpiarOpcionesEnBorrador() {
            pregunta.agregarOpcion("Opción A", true);
            pregunta.agregarOpcion("Opción B", false);
            assertEquals(2, pregunta.getOpciones().size());

            pregunta.limpiarOpciones();
            assertEquals(0, pregunta.getOpciones().size());
        }
    }

    @Nested
    @DisplayName("Invariante: Envío a Revisión")
    class InvariantesEnvioRevision {

        @Test
        @DisplayName("Paso 3 del Taller: No debe permitir enviar a revisión sin 4 opciones")
        void noDebePermitirEnviarARevisionSinCuatroOpciones() {
            Pregunta p = new Pregunta();
            p.agregarOpcion("Opción A", true);
            assertThrows(ReglaNegocioException.class, p::enviarARevision);
        }

        @Test
        @DisplayName("No debe permitir enviar a revisión con menos de 4 opciones (mensaje exacto)")
        void noDebePermitirEnviarARevisionConMenosDeCuatroOpciones() {
            pregunta.agregarOpcion("Opción 1", true);
            pregunta.agregarOpcion("Opción 2", false);
            pregunta.agregarOpcion("Opción 3", false);

            ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
            assertEquals("Debe tener exactamente 4 opciones de respuesta.", ex.getMessage());
        }

        @Test
        @DisplayName("No debe permitir enviar a revisión sin ninguna opción correcta (0 claves)")
        void noDebePermitirEnviarARevisionSinOpcionCorrecta() {
            pregunta.agregarOpcion("Opción 1", false);
            pregunta.agregarOpcion("Opción 2", false);
            pregunta.agregarOpcion("Opción 3", false);
            pregunta.agregarOpcion("Opción 4", false);

            ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
            assertEquals("Debe existir exactamente 1 opción correcta.", ex.getMessage());
        }

        @Test
        @DisplayName("No debe permitir enviar a revisión con más de 1 opción correcta (múltiples claves)")
        void noDebePermitirEnviarARevisionConMasDeUnaOpcionCorrecta() {
            pregunta.agregarOpcion("Opción 1", true);
            pregunta.agregarOpcion("Opción 2", true);
            pregunta.agregarOpcion("Opción 3", false);
            pregunta.agregarOpcion("Opción 4", false);

            ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
            assertEquals("Debe existir exactamente 1 opción correcta.", ex.getMessage());
        }

        @Test
        @DisplayName("No debe permitir enviar a revisión sin enunciado")
        void noDebePermitirEnviarARevisionSinEnunciado() {
            pregunta.agregarOpcion("Opción 1", true);
            pregunta.agregarOpcion("Opción 2", false);
            pregunta.agregarOpcion("Opción 3", false);
            pregunta.agregarOpcion("Opción 4", false);

            ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
            assertEquals("La pregunta debe contar con un enunciado válido antes de enviarse a revisión.", ex.getMessage());
        }

        @Test
        @DisplayName("No debe permitir enviar a revisión sin justificación pedagógica")
        void noDebePermitirEnviarARevisionSinJustificacion() {
            pregunta.asignarEnunciado(enunciadoValido);
            pregunta.agregarOpcion("Opción 1", true);
            pregunta.agregarOpcion("Opción 2", false);
            pregunta.agregarOpcion("Opción 3", false);
            pregunta.agregarOpcion("Opción 4", false);

            ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
            assertEquals("La pregunta debe contar con una justificación pedagógica antes de enviarse a revisión.", ex.getMessage());
        }

        @Test
        @DisplayName("Debe permitir enviar a revisión cuando cumple todas las invariantes y cambiar estado")
        void debePermitirEnviarARevisionCuandoCumpleInvariantes() {
            pregunta.asignarEnunciado(enunciadoValido);
            pregunta.asignarJustificacion(justificacionValida);
            pregunta.agregarOpcion("Opción 1 (Clave)", true);
            pregunta.agregarOpcion("Opción 2 (Distractor)", false);
            pregunta.agregarOpcion("Opción 3 (Distractor)", false);
            pregunta.agregarOpcion("Opción 4 (Distractor)", false);

            assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
            pregunta.enviarARevision();
            assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());
        }
    }

    @Nested
    @DisplayName("Contexto de Revisión de Pares y Ciclo de Vida")
    class RevisionParesYCicloDeVida {

        private Revisor revisor;

        @BeforeEach
        void prepararPreguntaParaRevision() {
            pregunta.asignarEnunciado(enunciadoValido);
            pregunta.asignarJustificacion(justificacionValida);
            pregunta.agregarOpcion("Opción 1 (Clave)", true);
            pregunta.agregarOpcion("Opción 2 (Distractor)", false);
            pregunta.agregarOpcion("Opción 3 (Distractor)", false);
            pregunta.agregarOpcion("Opción 4 (Distractor)", false);
            pregunta.enviarARevision();

            revisor = new Revisor("DOC-001", "Dra. Paola Andrea Bedoya", "pbedoya@unicauca.edu.co", "Ingeniería de Software");
        }

        @Test
        @DisplayName("Debe permitir iniciar revisión cuando está pendiente")
        void debePermitirIniciarRevision() {
            pregunta.iniciarRevision(revisor);
            assertEquals(EstadoPregunta.EN_REVISION, pregunta.getEstado());
        }

        @Test
        @DisplayName("No debe permitir iniciar revisión si el revisor es nulo")
        void noDebePermitirIniciarRevisionConRevisorNulo() {
            assertThrows(ReglaNegocioException.class, () -> pregunta.iniciarRevision(null));
        }

        @Test
        @DisplayName("Al aprobar una revisión, la pregunta transiciona a APROBADA")
        void debeTransicionarAAprobadaAlRecibirDictamenAprobado() {
            pregunta.iniciarRevision(revisor);

            Revision rev = new Revision(101L, revisor, Dictamen.APROBADO, "Reactivo de excelente calidad pedagógica", LocalDate.now());
            pregunta.registrarRevision(rev);

            assertEquals(EstadoPregunta.APROBADA, pregunta.getEstado());
            assertEquals(1, pregunta.getRevisiones().size());
        }

        @Test
        @DisplayName("Al rechazar una revisión, la pregunta regresa a BORRADOR para correcciones del autor")
        void debeRegresarABorradorAlRecibirDictamenRechazado() {
            pregunta.iniciarRevision(revisor);

            Revision rev = new Revision(102L, revisor, Dictamen.RECHAZADO, "El distractor 3 induce a confusión innecesaria.", LocalDate.now());
            pregunta.registrarRevision(rev);

            assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
            assertEquals(1, pregunta.getRevisiones().size());

            // Al regresar a borrador, el autor puede volver a modificar opciones
            assertDoesNotThrow(() -> pregunta.limpiarOpciones());
        }

        @Test
        @DisplayName("Al solicitar ajustes, la pregunta regresa a BORRADOR")
        void debeRegresarABorradorAlSolicitarAjustes() {
            pregunta.iniciarRevision(revisor);

            Revision rev = new Revision(103L, revisor, Dictamen.REQUIERE_AJUSTES, "Mejorar la redacción del enunciado para mayor claridad.", LocalDate.now());
            pregunta.registrarRevision(rev);

            assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
        }
    }
}
