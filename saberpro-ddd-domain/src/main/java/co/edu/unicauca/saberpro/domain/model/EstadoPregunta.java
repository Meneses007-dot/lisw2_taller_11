package co.edu.unicauca.saberpro.domain.model;

/**
 * Enumeración que modela el ciclo de vida de una pregunta en el Banco Saber PRO.
 *
 * <p>Define los estados por los que transita el reactivo desde su formulación inicial
 * hasta su aprobación o rechazo en los contextos de Autoría, Ciclo de Vida y Revisión de Pares.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public enum EstadoPregunta {
    /**
     * Fase inicial de redacción por parte del autor.
     * En este estado se permite modificar el enunciado, la justificación y las opciones.
     */
    BORRADOR,

    /**
     * Estado en el que la pregunta ha cumplido todas las invariantes estructurales
     * y se encuentra a la espera de ser tomada por un revisor par.
     */
    PENDIENTE_REVISION,

    /**
     * La pregunta se encuentra actualmente bajo evaluación pedagógica y técnica
     * por parte de un docente par asignado.
     */
    EN_REVISION,

    /**
     * La pregunta ha recibido dictamen favorable de los revisores
     * y se encuentra lista para formar parte de cuadernillos y simulacros Saber PRO.
     */
    APROBADA,

    /**
     * La pregunta ha sido rechazada debido a inconsistencias pedagógicas o estructurales.
     */
    RECHAZADA
}
