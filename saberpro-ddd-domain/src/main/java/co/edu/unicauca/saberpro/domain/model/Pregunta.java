package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Raíz del Agregado (Aggregate Root) para el Banco de Preguntas Saber PRO.
 *
 * <p>El Agregado Pregunta encapsula y gobierna su propio estado e invariantes de negocio:
 * <ul>
 *   <li>Las opciones de respuesta solo pueden agregarse o modificarse en estado {@link EstadoPregunta#BORRADOR}.</li>
 *   <li>Una pregunta no puede contener más de 4 opciones de respuesta.</li>
 *   <li>Para ser enviada a revisión ({@link #enviarARevision()}):
 *     <ul>
 *       <li>Debe contener exactamente 4 opciones de respuesta.</li>
 *       <li>Debe existir exactamente 1 opción marcada como clave correcta.</li>
 *       <li>Debe tener un enunciado asignado.</li>
 *       <li>Debe tener una justificación pedagógica asignada.</li>
 *     </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>Cualquier acceso o mutación sobre las opciones o revisiones debe realizarse
 * estrictamente a través de los métodos de esta Raíz del Agregado, preservando
 * el encapsulamiento mediante listas no modificables.</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public class Pregunta {

    private Long id;
    private Enunciado enunciado;
    private Justificacion justificacion;
    private final List<OpcionRespuesta> opciones = new ArrayList<>();
    private EstadoPregunta estado = EstadoPregunta.BORRADOR;
    private final List<Revision> revisiones = new ArrayList<>();

    /**
     * Constructor por defecto. Inicializa la pregunta en estado {@link EstadoPregunta#BORRADOR}.
     */
    public Pregunta() {
        this.estado = EstadoPregunta.BORRADOR;
    }

    /**
     * Constructor con identificador.
     *
     * @param id Identificador único de la pregunta.
     */
    public Pregunta(Long id) {
        this();
        this.id = id;
    }

    /**
     * Constructor completo de inicialización en borrador.
     *
     * @param id Identificador único de la pregunta.
     * @param enunciado Enunciado conceptual inicial.
     * @param justificacion Justificación pedagógica inicial.
     */
    public Pregunta(Long id, Enunciado enunciado, Justificacion justificacion) {
        this(id);
        this.enunciado = enunciado;
        this.justificacion = justificacion;
    }

    /**
     * Agrega una opción de respuesta a la pregunta garantizando las invariantes de negocio.
     *
     * @param texto Texto de la opción de respuesta.
     * @param esCorrecta Indicador booleano (true para clave correcta, false para distractor).
     * @throws ReglaNegocioException Si el estado no es BORRADOR o si ya existen 4 opciones.
     */
    public void agregarOpcion(String texto, boolean esCorrecta) {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo se pueden modificar opciones en estado Borrador.");
        }
        if (this.opciones.size() >= 4) {
            throw new ReglaNegocioException("Una pregunta no puede tener más de 4 opciones.");
        }
        this.opciones.add(new OpcionRespuesta(texto, esCorrecta));
    }

    /**
     * Remueve todas las opciones de respuesta registradas.
     * Solo es admisible en estado {@link EstadoPregunta#BORRADOR}.
     *
     * @throws ReglaNegocioException Si la pregunta no se encuentra en estado Borrador.
     */
    public void limpiarOpciones() {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo se pueden modificar opciones en estado Borrador.");
        }
        this.opciones.clear();
    }

    /**
     * Asigna o actualiza el enunciado de la pregunta.
     *
     * @param enunciado Objeto de valor Enunciado.
     * @throws ReglaNegocioException Si la pregunta no se encuentra en estado Borrador o el enunciado es nulo.
     */
    public void asignarEnunciado(Enunciado enunciado) {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo se puede asignar o modificar el enunciado en estado Borrador.");
        }
        if (enunciado == null) {
            throw new ReglaNegocioException("El enunciado asignado no puede ser nulo.");
        }
        this.enunciado = enunciado;
    }

    /**
     * Asigna o actualiza la justificación pedagógica de la pregunta.
     *
     * @param justificacion Objeto de valor Justificacion.
     * @throws ReglaNegocioException Si la pregunta no se encuentra en estado Borrador o la justificación es nula.
     */
    public void asignarJustificacion(Justificacion justificacion) {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo se puede asignar o modificar la justificación en estado Borrador.");
        }
        if (justificacion == null) {
            throw new ReglaNegocioException("La justificación asignada no puede ser nula.");
        }
        this.justificacion = justificacion;
    }

    /**
     * Envía la pregunta al contexto de revisión de pares, transicionando su estado a
     * {@link EstadoPregunta#PENDIENTE_REVISION} si y solo si satisface todas las invariantes de negocio.
     *
     * @throws ReglaNegocioException Si no cumple con las 4 opciones, la clave única o los componentes obligatorios.
     */
    public void enviarARevision() {
        validarInvariantesParaEnvio();
        this.estado = EstadoPregunta.PENDIENTE_REVISION;
    }

    /**
     * Valida las invariantes requeridas para que una pregunta pueda ser enviada a revisión.
     */
    private void validarInvariantesParaEnvio() {
        if (this.opciones.size() != 4) {
            throw new ReglaNegocioException("Debe tener exactamente 4 opciones de respuesta.");
        }
        long correctas = opciones.stream().filter(OpcionRespuesta::isEsCorrecta).count();
        if (correctas != 1) {
            throw new ReglaNegocioException("Debe existir exactamente 1 opción correcta.");
        }
        if (this.enunciado == null) {
            throw new ReglaNegocioException("La pregunta debe contar con un enunciado válido antes de enviarse a revisión.");
        }
        if (this.justificacion == null) {
            throw new ReglaNegocioException("La pregunta debe contar con una justificación pedagógica antes de enviarse a revisión.");
        }
    }

    /**
     * Inicia el proceso de revisión por pares cuando un docente toma la pregunta.
     * Transiciona el estado de {@link EstadoPregunta#PENDIENTE_REVISION} a {@link EstadoPregunta#EN_REVISION}.
     *
     * @param revisor Revisor asignado a la evaluación.
     * @throws ReglaNegocioException Si la pregunta no está en estado Pendiente de Revisión o el revisor es nulo.
     */
    public void iniciarRevision(Revisor revisor) {
        if (revisor == null) {
            throw new ReglaNegocioException("Debe especificarse un revisor para iniciar la evaluación.");
        }
        if (this.estado != EstadoPregunta.PENDIENTE_REVISION) {
            throw new ReglaNegocioException("Solo se puede iniciar revisión sobre preguntas en estado Pendiente de Revisión.");
        }
        this.estado = EstadoPregunta.EN_REVISION;
    }

    /**
     * Registra un dictamen formal de revisión pedagógica emitido por un revisor.
     *
     * <p>Efectos de estado:
     * <ul>
     *   <li>Si el dictamen es {@link Dictamen#APROBADO}, transiciona a {@link EstadoPregunta#APROBADA}.</li>
     *   <li>Si el dictamen es {@link Dictamen#RECHAZADO} o {@link Dictamen#REQUIERE_AJUSTES},
     *       regresa a {@link EstadoPregunta#BORRADOR} para permitir la subsanación por parte del autor.</li>
     * </ul>
     * </p>
     *
     * @param revision Entidad de revisión con el veredicto y observaciones.
     * @throws ReglaNegocioException Si la pregunta no se encuentra en revisión o la revisión es nula.
     */
    public void registrarRevision(Revision revision) {
        if (revision == null) {
            throw new ReglaNegocioException("La revisión a registrar no puede ser nula.");
        }
        if (this.estado != EstadoPregunta.EN_REVISION && this.estado != EstadoPregunta.PENDIENTE_REVISION) {
            throw new ReglaNegocioException("Solo se pueden registrar revisiones en preguntas pendientes o en revisión.");
        }

        this.revisiones.add(revision);

        if (revision.getDictamen() == Dictamen.APROBADO) {
            this.estado = EstadoPregunta.APROBADA;
        } else {
            // RECHAZADO o REQUIERE_AJUSTES retorna a BORRADOR para que el autor pueda modificarla
            this.estado = EstadoPregunta.BORRADOR;
        }
    }

    // --- Métodos de acceso (Getters y Setters respetuosos de DDD) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enunciado getEnunciado() {
        return enunciado;
    }

    public Justificacion getJustificacion() {
        return justificacion;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    /**
     * Retorna una vista inmodificable de las opciones de respuesta del agregado.
     * Preserva la regla de diseño táctico: los clientes no pueden mutar la colección interna directamente.
     *
     * @return Lista inmodificable de {@link OpcionRespuesta}.
     */
    public List<OpcionRespuesta> getOpciones() {
        return Collections.unmodifiableList(opciones);
    }

    /**
     * Retorna una vista inmodificable de las revisiones asociadas al agregado.
     *
     * @return Lista inmodificable de {@link Revision}.
     */
    public List<Revision> getRevisiones() {
        return Collections.unmodifiableList(revisiones);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pregunta pregunta = (Pregunta) o;
        return Objects.equals(id, pregunta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pregunta{" +
                "id=" + id +
                ", estado=" + estado +
                ", opciones=" + opciones.size() +
                ", revisiones=" + revisiones.size() +
                '}';
    }
}
