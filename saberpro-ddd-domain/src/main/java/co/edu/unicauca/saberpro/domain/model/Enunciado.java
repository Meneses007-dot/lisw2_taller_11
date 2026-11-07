package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Objects;

/**
 * Objeto de Valor (Value Object) inmutable que encapsula el enunciado de un reactivo.
 *
 * <p>Representa el contexto situacional, problema o requerimiento planteado en una prueba Saber PRO.
 * Garantiza como invariante de construcción que no puede ser nulo ni estar compuesto únicamente
 * por espacios en blanco, y debe poseer una extensión mínima significativa.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public final class Enunciado {

    private final String texto;

    /**
     * Construye un Enunciado validando su integridad estructural.
     *
     * @param texto Cadena con el texto del enunciado.
     * @throws ReglaNegocioException Si el texto es nulo, vacío o tiene menos de 5 caracteres.
     */
    public Enunciado(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ReglaNegocioException("El enunciado de la pregunta no puede ser nulo ni vacío.");
        }
        if (texto.trim().length() < 5) {
            throw new ReglaNegocioException("El enunciado debe contener al menos 5 caracteres descriptivos.");
        }
        this.texto = texto.trim();
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enunciado enunciado = (Enunciado) o;
        return Objects.equals(texto, enunciado.texto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texto);
    }

    @Override
    public String toString() {
        return "Enunciado{" +
                "texto='" + texto + '\'' +
                '}';
    }
}
