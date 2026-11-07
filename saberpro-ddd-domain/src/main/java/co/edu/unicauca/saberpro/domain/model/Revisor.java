package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Objects;

/**
 * Entidad que representa a un Docente Revisor Par en el Banco de Preguntas Saber PRO.
 *
 * <p>Posee una identidad conceptual única y persistente en el tiempo (definida por su identificador docente).
 * Dos revisores con idéntico nombre y especialidad son entidades distintas si sus identificadores difieren.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public class Revisor {

    private final String idDocente;
    private String nombreCompleto;
    private String email;
    private String areaEspecialidad;

    /**
     * Construye una entidad Revisor.
     *
     * @param idDocente Identificador único e inmutable del docente (e.g. cédula o código de nómina).
     * @param nombreCompleto Nombre completo del docente.
     * @param email Correo institucional.
     * @param areaEspecialidad Área o componente de evaluación (e.g. Lectura Crítica, Razonamiento Cuantitativo).
     */
    public Revisor(String idDocente, String nombreCompleto, String email, String areaEspecialidad) {
        if (idDocente == null || idDocente.trim().isEmpty()) {
            throw new ReglaNegocioException("El ID del docente revisor no puede ser nulo ni vacío.");
        }
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del docente revisor no puede ser nulo ni vacío.");
        }
        this.idDocente = idDocente.trim();
        this.nombreCompleto = nombreCompleto.trim();
        this.email = email != null ? email.trim() : "";
        this.areaEspecialidad = areaEspecialidad != null ? areaEspecialidad.trim() : "";
    }

    public String getIdDocente() {
        return idDocente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del docente revisor no puede ser vacío.");
        }
        this.nombreCompleto = nombreCompleto.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : "";
    }

    public String getAreaEspecialidad() {
        return areaEspecialidad;
    }

    public void setAreaEspecialidad(String areaEspecialidad) {
        this.areaEspecialidad = areaEspecialidad != null ? areaEspecialidad.trim() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Revisor revisor = (Revisor) o;
        return Objects.equals(idDocente, revisor.idDocente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocente);
    }

    @Override
    public String toString() {
        return "Revisor{" +
                "idDocente='" + idDocente + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", areaEspecialidad='" + areaEspecialidad + '\'' +
                '}';
    }
}
