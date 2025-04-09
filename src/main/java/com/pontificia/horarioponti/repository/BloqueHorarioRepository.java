package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.BloqueHorario;
import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.repository.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface BloqueHorarioRepository extends JpaRepository<BloqueHorario, Long> {

    List<BloqueHorario> findByTurno(Turno turno);

    List<BloqueHorario> findByTurnoOrderByOrden(Turno turno);

    @Query("SELECT b FROM BloqueHorario b WHERE " +
            "(:horaInicio BETWEEN b.horaInicio AND b.horaFin) OR " +
            "(:horaFin BETWEEN b.horaInicio AND b.horaFin) OR " +
            "(b.horaInicio BETWEEN :horaInicio AND :horaFin)")
    List<BloqueHorario> findBloquesSolapados(LocalTime horaInicio, LocalTime horaFin);

    @Query("SELECT b FROM BloqueHorario b " +
            "WHERE NOT EXISTS (SELECT a FROM AsignacionHorario a JOIN a.bloques ab " +
            "WHERE ab.idBloque = b.idBloque AND a.diaSemana = :diaSemana " +
            "AND a.ambiente.idAmbiente = :ambienteId)")
    List<BloqueHorario> findBloquesDisponiblesByAmbienteAndDia(Long ambienteId, DisponibilidadDocente.DiaSemana diaSemana);
}