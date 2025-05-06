package com.pontificia.horarioponti.utils.abstractBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public abstract class BaseService<T extends BaseEntity> {

    @Autowired
    protected BaseRepository<T> baseRepository;

    public List<T> findAll() {
        return baseRepository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
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
}
