package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.Ciclo;
import com.pontificia.horarioponti.repository.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByCiclo(Ciclo ciclo);

    List<Curso> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT c FROM Curso c WHERE c.ciclo.carrera.idCarrera = :carreraId")
    List<Curso> findByCarreraId(Long carreraId);

    @Query("SELECT c FROM Curso c WHERE c.ciclo.carrera.modalidad.idModalidad = :modalidadId")
    List<Curso> findByModalidadId(Long modalidadId);

    boolean existsByNombreAndCiclo(String nombre, Ciclo ciclo);
}
