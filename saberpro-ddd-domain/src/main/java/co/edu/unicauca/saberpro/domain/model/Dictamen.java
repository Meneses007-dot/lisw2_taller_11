package co.edu.unicauca.saberpro.domain.model;

/**
 * Dictamen emitido por un docente revisor par tras evaluar una pregunta.
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public enum Dictamen {
    /**
     * La pregunta cumple a cabalidad con los lineamientos técnicos, metodológicos
     * y pedagógicos de Saber PRO.
     */
    APROBADO,

    /**
     * La pregunta posee fallas estructurales o de validez que impiden su uso
     * y debe ser descartada o reelaborada exhaustivamente.
     */
    RECHAZADO,

    /**
     * La pregunta requiere modificaciones menores en redacción, distractores o justificación
     * por parte del autor antes de poder ser reconsiderada.
     */
    REQUIERE_AJUSTES
}
