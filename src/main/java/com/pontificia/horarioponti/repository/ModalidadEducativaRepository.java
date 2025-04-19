package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.ModalidadEducativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModalidadEducativaRepository extends JpaRepository<ModalidadEducativa, Long> {

    Optional<ModalidadEducativa> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
