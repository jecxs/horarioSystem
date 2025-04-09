package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    List<Grupo> findByCiclo(Ciclo ciclo);

    Optional<Grupo> findByNombreAndCiclo(String nombre, Ciclo ciclo);

    @Query("SELECT g FROM Grupo g WHERE g.ciclo.carrera.idCarrera = :carreraId")
    List<Grupo> findByCarreraId(Long carreraId);

    boolean existsByNombreAndCiclo(String nombre, Ciclo ciclo);
}