package co.edu.unicauca.saberpro.domain.exception;

/**
 * Excepción base no comprobada para todas las anomalías y violaciones conceptuales
 * originadas en el núcleo del dominio de Saber PRO.
 *
 * <p>Al heredar de {@link RuntimeException}, preserva la pureza del modelo de dominio,
 * permitiendo un diseño desacoplado de dependencias tecnológicas o frameworks externos.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
