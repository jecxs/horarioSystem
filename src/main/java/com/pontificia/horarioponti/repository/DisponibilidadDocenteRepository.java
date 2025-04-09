package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.repository.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface DisponibilidadDocenteRepository extends JpaRepository<DisponibilidadDocente, Long> {

    List<DisponibilidadDocente> findByDocente(Docente docente);

    List<DisponibilidadDocente> findByDocenteAndDiaSemana(Docente docente, DisponibilidadDocente.DiaSemana diaSemana);

    List<DisponibilidadDocente> findByDocenteAndDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
            Docente docente,
            DisponibilidadDocente.DiaSemana diaSemana,
            LocalTime horaFin,
            LocalTime horaInicio);

    void deleteByDocente(Docente docente);
}
