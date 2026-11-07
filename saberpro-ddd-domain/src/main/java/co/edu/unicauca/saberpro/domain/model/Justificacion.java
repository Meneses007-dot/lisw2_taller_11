package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.List;
import java.util.Objects;

/**
 * Objeto de Valor (Value Object) que encapsula la justificación pedagógica
 * de un reactivo de Saber PRO.
 *
 * <p>Carece de identidad conceptual propia y se define exclusivamente por su contenido textual.
 * Es inmutable y garantiza las siguientes invariantes en su creación:
 * <ul>
 *   <li>No puede ser nula ni vacía.</li>
 *   <li>Debe contener al menos 15 caracteres no triviales.</li>
 *   <li>No puede contener expresiones informales o circulares prohibidas
 *       (e.g., "porque sí", "es obvio", "es la correcta").</li>
 * </ul>
 * </p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public final class Justificacion {

    private static final List<String> PALABRAS_PROHIBIDAS = List.of(
            "porque sí",
            "porque si",
            "es obvio",
            "obvio",
            "es la correcta",
            "sin justificar",
            "por descarte"
    );

    private final String texto;

    /**
     * Construye un objeto de valor Justificacion aplicando validaciones de invariantes.
     *
     * @param texto Cadena con el texto explicativo de la justificación pedagógica.
     * @throws ReglaNegocioException Si el texto es nulo, menor a 15 caracteres o contiene expresiones prohibidas.
     */
    public Justificacion(String texto) {
        if (texto == null || texto.trim().length() < 15) {
            throw new ReglaNegocioException("La justificación debe contener al menos 15 caracteres.");
        }

        String textoLimpio = texto.trim();
        String textoMin = textoLimpio.toLowerCase();

        for (String prohibida : PALABRAS_PROHIBIDAS) {
            if (textoMin.contains(prohibida)) {
                throw new ReglaNegocioException("La justificación contiene expresiones no permitidas: '" + prohibida + "'.");
            }
        }

        this.texto = textoLimpio;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Justificacion that = (Justificacion) o;
        return Objects.equals(texto, that.texto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texto);
    }

    @Override
    public String toString() {
        return "Justificacion{" +
                "texto='" + texto + '\'' +
                '}';
    }
}
