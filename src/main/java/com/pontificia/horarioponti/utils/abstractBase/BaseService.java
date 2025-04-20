package com.pontificia.horarioponti.utils.abstractBase;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public abstract class BaseService<T extends BaseEntity, R extends BaseRepository<T>> {

    protected final R repository;

    protected BaseService(R repository) {
        this.repository = repository;
    }

    @Transactional
    public List<T> findAll() {
        return repository.findAll();
    }

    @Transactional
    public T findByUuid(UUID uuid) {
        return repository.findByUuid(uuid).orElse(null);
    }

    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Transactional
    public boolean deleteByUuid(UUID uuid) {
        if (repository.existsByUuid(uuid)) {
            repository.deleteById(uuid);
            return true;
        }
        return false;
    }
}
