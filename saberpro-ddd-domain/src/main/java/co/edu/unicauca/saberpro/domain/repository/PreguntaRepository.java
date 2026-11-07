package co.edu.unicauca.saberpro.domain.repository;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.List;
import java.util.Optional;

/**
 * Contrato puro de repositorio para la Raíz de Agregado {@link Pregunta}.
 *
 * <p>En Domain-Driven Design (DDD), los repositorios proporcionan una ilusión de colección
 * en memoria para acceder y persistir agregados. Este contrato pertenece estrictamente
 * a la capa de dominio y carece de cualquier dependencia hacia frameworks de infraestructura
 * (e.g., Spring Data, JPA, Hibernate o JDBC).</p>
 *
 * @author Santiago Rodríguez
 * @author Juan Manuel Meneses Daza
 * @version 1.0.0
 */
public interface PreguntaRepository {

    /**
     * Guarda o actualiza la Raíz de Agregado {@link Pregunta} preservando sus invariantes.
     *
     * @param pregunta Agregado a persistir.
     * @return El agregado persistido con su identificador asignado.
     */
    Pregunta save(Pregunta pregunta);

    /**
     * Busca una pregunta por su identificador único.
     *
     * @param id Identificador de la pregunta.
     * @return Un {@link Optional} con la pregunta si existe, o vacío en caso contrario.
     */
    Optional<Pregunta> findById(Long id);

    /**
     * Retorna todas las preguntas registradas en el banco.
     *
     * @return Lista de preguntas.
     */
    List<Pregunta> findAll();

    /**
     * Busca todas las preguntas que se encuentren en un estado determinado.
     *
     * @param estado Estado del ciclo de vida a filtrar.
     * @return Lista de preguntas en dicho estado.
     */
    List<Pregunta> findByEstado(EstadoPregunta estado);

    /**
     * Elimina una pregunta por su identificador.
     *
     * @param id Identificador de la pregunta a eliminar.
     */
    void deleteById(Long id);

    /**
     * Verifica si existe una pregunta con el identificador provisto.
     *
     * @param id Identificador a verificar.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existsById(Long id);

    /**
     * Retorna el número total de preguntas persistidas.
     *
     * @return Cantidad de preguntas.
     */
    long count();
}
