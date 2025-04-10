package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionHorarioRepository extends JpaRepository<AsignacionHorario, Long> {

    List<AsignacionHorario> findByCursoAndGrupo(Curso curso, Grupo grupo);

    List<AsignacionHorario> findByDocente(Docente docente);

    List<AsignacionHorario> findByGrupo(Grupo grupo);

    List<AsignacionHorario> findByAmbiente(Ambiente ambiente);

    @Query("SELECT a FROM AsignacionHorario a WHERE a.docente.idDocente = :docenteId AND a.diaSemana = :diaSemana")
    List<AsignacionHorario> findByDocenteAndDia(Long docenteId, DisponibilidadDocente.DiaSemana diaSemana);

    @Query("SELECT a FROM AsignacionHorario a JOIN a.bloques b WHERE b.idBloque = :bloqueId AND a.diaSemana = :diaSemana")
    List<AsignacionHorario> findByBloqueAndDia(Long bloqueId, DisponibilidadDocente.DiaSemana diaSemana);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AsignacionHorario a " +
            "JOIN a.bloques b WHERE a.docente.idDocente = :docenteId AND a.diaSemana = :diaSemana " +
            "AND b.idBloque IN :bloqueIds")
    boolean existsConflictoDocenteEnBloques(Long docenteId, DisponibilidadDocente.DiaSemana diaSemana, List<Long> bloqueIds);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AsignacionHorario a " +
            "JOIN a.bloques b WHERE a.ambiente.idAmbiente = :ambienteId AND a.diaSemana = :diaSemana " +
            "AND b.idBloque IN :bloqueIds")
    boolean existsConflictoAmbienteEnBloques(Long ambienteId, DisponibilidadDocente.DiaSemana diaSemana, List<Long> bloqueIds);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AsignacionHorario a " +
            "JOIN a.bloques b WHERE a.grupo.idGrupo = :grupoId AND a.diaSemana = :diaSemana " +
            "AND b.idBloque IN :bloqueIds")
    boolean existsConflictoGrupoEnBloques(Long grupoId, DisponibilidadDocente.DiaSemana diaSemana, List<Long> bloqueIds);


}
