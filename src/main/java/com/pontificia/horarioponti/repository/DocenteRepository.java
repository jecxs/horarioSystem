package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    List<Docente> findByNombreCompletoContainingIgnoreCase(String nombre);

    List<Docente> findByEspecialidadContainingIgnoreCase(String especialidad);

    @Query("SELECT DISTINCT d FROM Docente d JOIN d.asignaciones a JOIN a.curso c WHERE c.ciclo.carrera.idCarrera = :carreraId")
    List<Docente> findDocentesByCarrera(Long carreraId);

    @Query("SELECT d FROM Docente d LEFT JOIN FETCH d.disponibilidades WHERE d.idDocente = :docenteId")
    Docente findByIdWithDisponibilidad(Long docenteId);
}
