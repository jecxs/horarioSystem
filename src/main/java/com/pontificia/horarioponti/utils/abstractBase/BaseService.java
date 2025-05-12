package com.pontificia.horarioponti.utils.abstractBase;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public abstract class BaseService<T> {

    protected final BaseRepository<T> baseRepository;

    public BaseService(BaseRepository<T> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public List<T> findAll() {
        return baseRepository.findAll();
    }

    public Optional<T> findById(UUID id) {
        return baseRepository.findById(id);
    }

    public T save(T entity) {
        return baseRepository.save(entity);
    }

    public T update(T entity) {
        return baseRepository.save(entity);
    }

    public void deleteById(UUID id) {
        baseRepository.deleteById(id);
    }

    public T findOrThrow(@NotEmpty UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Entidad no encontrada con ID: " + uuid));
    }
}
