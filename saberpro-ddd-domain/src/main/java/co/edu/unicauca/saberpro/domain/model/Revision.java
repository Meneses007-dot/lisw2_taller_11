package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad que modela una Revisión Pedagógica emitida por un Revisor Par.
 *
 * <p>Posee identidad conceptual única (ID de evaluación) y registra el dictamen,
 * fecha de emisión y observaciones pedagógicas.</p>
 *
 * <p>Invariante: Todo dictamen de rechazo o solicitud de ajustes debe incluir
 * obligatoriamente observaciones detalladas para guiar la corrección del autor.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public class Revision {

    private final Long id;
    private final Revisor revisor;
    private final Dictamen dictamen;
    private final String observaciones;
    private final LocalDate fechaRevision;

    /**
     * Construye una entidad Revision validando sus invariantes.
     *
     * @param id Identificador único de la evaluación.
     * @param revisor Docente par asignado (no nulo).
     * @param dictamen Dictamen emitido (no nulo).
     * @param observaciones Retroalimentación y sustentación del dictamen.
     * @param fechaRevision Fecha en que se realizó la evaluación.
     * @throws ReglaNegocioException Si el revisor o dictamen son nulos, o si se rechaza sin observaciones.
     */
    public Revision(Long id, Revisor revisor, Dictamen dictamen, String observaciones, LocalDate fechaRevision) {
        if (id == null) {
            throw new ReglaNegocioException("El ID de la revisión no puede ser nulo.");
        }
        if (revisor == null) {
            throw new ReglaNegocioException("Toda revisión debe contar con un Revisor Par asignado.");
        }
        if (dictamen == null) {
            throw new ReglaNegocioException("El dictamen de la revisión no puede ser nulo.");
        }

        if ((dictamen == Dictamen.RECHAZADO || dictamen == Dictamen.REQUIERE_AJUSTES) &&
                (observaciones == null || observaciones.trim().length() < 10)) {
            throw new ReglaNegocioException("Un dictamen de rechazo o ajuste requiere observaciones detalladas (mínimo 10 caracteres).");
        }

        this.id = id;
        this.revisor = revisor;
        this.dictamen = dictamen;
        this.observaciones = observaciones != null ? observaciones.trim() : "";
        this.fechaRevision = fechaRevision != null ? fechaRevision : LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Revisor getRevisor() {
        return revisor;
    }

    public Dictamen getDictamen() {
        return dictamen;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDate getFechaRevision() {
        return fechaRevision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Revision revision = (Revision) o;
        return Objects.equals(id, revision.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Revision{" +
                "id=" + id +
                ", revisor=" + revisor.getNombreCompleto() +
                ", dictamen=" + dictamen +
                ", fechaRevision=" + fechaRevision +
                '}';
    }
}
