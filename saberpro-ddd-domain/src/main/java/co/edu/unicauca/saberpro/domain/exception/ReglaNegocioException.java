package co.edu.unicauca.saberpro.domain.exception;

/**
 * Excepción lanzada cuando una operación solicitada sobre una entidad,
 * objeto de valor o agregado transgrede una regla o invariante de negocio.
 *
 * <p>Ejemplos de transgresión: intentar agregar más de 4 opciones de respuesta,
 * enviar a revisión una pregunta sin exactamente 4 opciones o sin 1 clave correcta,
 * o redactar una justificación con menos de 15 caracteres o con frases prohibidas.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public class ReglaNegocioException extends DomainException {

    public ReglaNegocioException(String message) {
        super(message);
    }

    public ReglaNegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
