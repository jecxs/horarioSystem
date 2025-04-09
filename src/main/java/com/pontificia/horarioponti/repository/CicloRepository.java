package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.Carrera;
import com.pontificia.horarioponti.repository.model.Ciclo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CicloRepository extends JpaRepository<Ciclo, Long> {

    List<Ciclo> findByCarrera(Carrera carrera);

    Optional<Ciclo> findByNumeroAndCarrera(Integer numero, Carrera carrera);

    boolean existsByNumeroAndCarrera(Integer numero, Carrera carrera);
}
