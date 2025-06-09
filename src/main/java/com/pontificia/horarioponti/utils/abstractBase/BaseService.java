package com.pontificia.horarioponti.utils.abstractBase;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Clase base genérica para servicios que proporciona operaciones CRUD comunes.
 *
 * @param <T> Tipo de la entidad.
 */
public abstract class BaseService<T> {

    /**
     * Repositorio genérico utilizado por el servicio.
     */
    protected final BaseRepository<T> baseRepository;

    /**
     * Constructor que recibe el repositorio genérico.
     *
     * @param baseRepository Repositorio base para la entidad T.
     */
    public BaseService(BaseRepository<T> baseRepository) {
        this.baseRepository = baseRepository;
    }

    /**
     * Retorna todos los registros de la entidad.
     *
     * @return Lista de todas las entidades del tipo T.
     */
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    /**
     * Busca una entidad por su UUID.
     *
     * @param id UUID de la entidad.
     * @return Optional conteniendo la entidad si existe, o vacío si no existe.
     */
    public Optional<T> findById(UUID id) {
        return baseRepository.findById(id);
    }

    /**
     * Guarda una nueva entidad en la base de datos.
     *
     * @param entity Entidad a guardar.
     * @return La entidad guardada.
     */
    public T save(T entity) {
        return baseRepository.save(entity);
    }

    /**
     * Actualiza una entidad existente.
     * Es equivalente al método save.
     *
     * @param entity Entidad a actualizar.
     * @return La entidad actualizada.
     */
    public T update(T entity) {
        return baseRepository.save(entity);
    }

    /**
     * Elimina una entidad por su UUID.
     *
     * @param id UUID de la entidad a eliminar.
     */
    public void deleteById(UUID id) {
        baseRepository.deleteById(id);
    }

    /**
     * Busca una entidad por su UUID o lanza una excepción si no se encuentra.
     *
     * @param uuid UUID de la entidad.
     * @return La entidad encontrada.
     * @throws EntityNotFoundException si la entidad no existe.
     */
    public T findOrThrow(@NotEmpty UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Entidad no encontrada con ID: " + uuid));
    }
}
