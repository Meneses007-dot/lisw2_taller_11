package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de invariantes del Objeto de Valor {@link Enunciado}.
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 */
@DisplayName("Pruebas del Value Object Enunciado")
class EnunciadoTest {

    @Test
    @DisplayName("No debe permitir enunciado con referencia nula")
    void noDebePermitirTextoNulo() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new Enunciado(null)
        );
        assertEquals("El enunciado de la pregunta no puede ser nulo ni vacío.", ex.getMessage());
    }

    @Test
    @DisplayName("No debe permitir enunciado vacío o compuesto únicamente de espacios")
    void noDebePermitirTextoVacio() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new Enunciado("     ")
        );
        assertEquals("El enunciado de la pregunta no puede ser nulo ni vacío.", ex.getMessage());
    }

    @Test
    @DisplayName("No debe permitir enunciado con menos de 5 caracteres")
    void noDebePermitirMenosDeCincoCaracteres() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new Enunciado("¿A?")
        );
        assertEquals("El enunciado debe contener al menos 5 caracteres descriptivos.", ex.getMessage());
    }

    @Test
    @DisplayName("Debe construir un Enunciado válido preservando el texto sin espacios periféricos")
    void debeConstruirEnunciadoValido() {
        String texto = "¿Cuál es el patrón que define las raíces de agregado?";
        Enunciado enunciado = new Enunciado("   " + texto + "   ");

        assertNotNull(enunciado);
        assertEquals(texto, enunciado.getTexto());
    }

    @Test
    @DisplayName("Invariante Value Object: Igualdad estructural por valor")
    void dosInstanciasConMismoTextoSonIguales() {
        Enunciado e1 = new Enunciado("Contexto situacional del problema planteado.");
        Enunciado e2 = new Enunciado("Contexto situacional del problema planteado.");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    @DisplayName("Invariante Value Object: Textos distintos generan objetos no equivalentes")
    void dosInstanciasConDistintoTextoNoSonIguales() {
        Enunciado e1 = new Enunciado("Contexto A de la pregunta.");
        Enunciado e2 = new Enunciado("Contexto B de la pregunta.");

        assertNotEquals(e1, e2);
    }
}
