package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de invariantes del Objeto de Valor {@link Justificacion}.
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 */
@DisplayName("Pruebas del Value Object Justificacion")
class JustificacionTest {

    @Test
    @DisplayName("No debe permitir crear una justificación con texto nulo")
    void noDebePermitirTextoNulo() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new Justificacion(null)
        );
        assertEquals("La justificación debe contener al menos 15 caracteres.", ex.getMessage());
    }

    @Test
    @DisplayName("No debe permitir crear una justificación vacía o de solo espacios")
    void noDebePermitirTextoVacio() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new Justificacion("               ")
        );
        assertEquals("La justificación debe contener al menos 15 caracteres.", ex.getMessage());
    }

    @Test
    @DisplayName("No debe permitir crear una justificación con menos de 15 caracteres")
    void noDebePermitirMenosDe15Caracteres() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new Justificacion("Texto corto")
        );
        assertEquals("La justificación debe contener al menos 15 caracteres.", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Esta opción es la elegida porque sí y no requiere más análisis.",
            "La respuesta correcta es esta porque si se analiza bien salta a la vista.",
            "El resultado es obvio según la teoría expuesta en el enunciado.",
            "Esta alternativa es la correcta indiscutiblemente.",
            "Se descarta la opción sin justificar adecuadamente el procedimiento.",
            "Se elige por descarte ya que las otras opciones parecen incorrectas."
    })
    @DisplayName("No debe permitir crear justificaciones con expresiones prohibidas o ambiguas")
    void noDebePermitirExpresionesProhibidas(String textoInvalido) {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion(textoInvalido));
    }

    @Test
    @DisplayName("Debe crear exitosamente una justificación pedagógica válida")
    void debeCrearJustificacionValida() {
        String texto = "La opción B satisface el criterio técnico aplicando el teorema de Bayes con probabilidades a priori.";
        Justificacion justificacion = new Justificacion(texto);

        assertNotNull(justificacion);
        assertEquals(texto, justificacion.getTexto());
    }

    @Test
    @DisplayName("Invariante Value Object: Igualdad por valor entre dos instancias")
    void dosInstanciasConMismoTextoDebenSerIgualesPorValor() {
        String texto = "Sustentación pedagógica rigurosa y válida para la prueba.";
        Justificacion j1 = new Justificacion(texto);
        Justificacion j2 = new Justificacion(texto);

        assertEquals(j1, j2);
        assertEquals(j1.hashCode(), j2.hashCode());
    }

    @Test
    @DisplayName("Invariante Value Object: Desigualdad por valor entre textos diferentes")
    void dosInstanciasConTextoDiferenteNoSonIguales() {
        Justificacion j1 = new Justificacion("Sustentación pedagógica número uno válida.");
        Justificacion j2 = new Justificacion("Sustentación pedagógica número dos válida.");

        assertNotEquals(j1, j2);
    }
}
