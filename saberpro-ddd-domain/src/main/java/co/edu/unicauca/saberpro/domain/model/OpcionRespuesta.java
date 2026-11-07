package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Objects;

/**
 * Objeto de Valor (Value Object) inmutable que modela una opción de respuesta.
 *
 * <p>Representa una alternativa dentro de una pregunta de selección múltiple.
 * Contiene el texto de la opción y un indicador booleano que determina si corresponde
 * a la clave (respuesta correcta) o a un distractor.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public final class OpcionRespuesta {

    private final String texto;
    private final boolean esCorrecta;

    /**
     * Construye una opción de respuesta validando sus invariantes.
     *
     * @param texto Texto de la opción de respuesta (no nulo ni en blanco).
     * @param esCorrecta {@code true} si esta opción es la clave correcta, {@code false} si es distractor.
     * @throws ReglaNegocioException Si el texto es nulo o vacío.
     */
    public OpcionRespuesta(String texto, boolean esCorrecta) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ReglaNegocioException("El texto de la opción de respuesta no puede ser nulo ni vacío.");
        }
        this.texto = texto.trim();
        this.esCorrecta = esCorrecta;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpcionRespuesta that = (OpcionRespuesta) o;
        return esCorrecta == that.esCorrecta && Objects.equals(texto, that.texto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texto, esCorrecta);
    }

    @Override
    public String toString() {
        return "OpcionRespuesta{" +
                "texto='" + texto + '\'' +
                ", esCorrecta=" + esCorrecta +
                '}';
    }
}
