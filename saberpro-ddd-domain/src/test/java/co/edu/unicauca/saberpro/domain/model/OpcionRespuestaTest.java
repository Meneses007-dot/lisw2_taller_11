package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de invariantes del Objeto de Valor {@link OpcionRespuesta}.
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 */
@DisplayName("Pruebas del Value Object OpcionRespuesta")
class OpcionRespuestaTest {

    @Test
    @DisplayName("No debe permitir opción de respuesta con texto nulo")
    void noDebePermitirTextoNulo() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new OpcionRespuesta(null, true)
        );
        assertEquals("El texto de la opción de respuesta no puede ser nulo ni vacío.", ex.getMessage());
    }

    @Test
    @DisplayName("No debe permitir opción de respuesta con texto en blanco")
    void noDebePermitirTextoEnBlanco() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () ->
                new OpcionRespuesta("    ", false)
        );
        assertEquals("El texto de la opción de respuesta no puede ser nulo ni vacío.", ex.getMessage());
    }

    @Test
    @DisplayName("Debe construir una opción válida como clave correcta")
    void debeConstruirOpcionComoClaveCorrecta() {
        OpcionRespuesta opcion = new OpcionRespuesta("Encapsular las reglas del dominio", true);

        assertEquals("Encapsular las reglas del dominio", opcion.getTexto());
        assertTrue(opcion.isEsCorrecta());
    }

    @Test
    @DisplayName("Debe construir una opción válida como distractor")
    void debeConstruirOpcionComoDistractor() {
        OpcionRespuesta opcion = new OpcionRespuesta("Exponer la base de datos directamente al controlador", false);

        assertEquals("Exponer la base de datos directamente al controlador", opcion.getTexto());
        assertFalse(opcion.isEsCorrecta());
    }

    @Test
    @DisplayName("Invariante Value Object: Igualdad estructural por valor (texto y clave)")
    void dosInstanciasConMismoTextoYClaveSonIguales() {
        OpcionRespuesta o1 = new OpcionRespuesta("Opción de respuesta idéntica", true);
        OpcionRespuesta o2 = new OpcionRespuesta("Opción de respuesta idéntica", true);

        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    @DisplayName("Invariante Value Object: Desigualdad si difiere el texto o el indicador de clave")
    void dosInstanciasConDiferenteClaveNoSonIguales() {
        OpcionRespuesta o1 = new OpcionRespuesta("Mismo texto", true);
        OpcionRespuesta o2 = new OpcionRespuesta("Mismo texto", false);

        assertNotEquals(o1, o2);
    }
}
