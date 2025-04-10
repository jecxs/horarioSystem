package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.DisponibilidadDocente;
import com.pontificia.horarioponti.repository.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface DisponibilidadDocenteRepository extends JpaRepository<DisponibilidadDocente, Long> {

    List<DisponibilidadDocente> findByDocente(Docente docente);

    List<DisponibilidadDocente> findByDocenteAndDiaSemana(Docente docente, DisponibilidadDocente.DiaSemana diaSemana);

    /**
     * Usar esta consulta personalizada que maneja explícitamente la conversión de tipos en SQL Server:
     */
    @Query("SELECT d FROM DisponibilidadDocente d WHERE d.docente = :docente AND d.diaSemana = :diaSemana " +
            "AND CAST(d.horaInicio AS string) <= CAST(:horaInicio AS string) " +
            "AND CAST(d.horaFin AS string) >= CAST(:horaFin AS string)")
    List<DisponibilidadDocente> findDisponibilidadesQueContienen(
            @Param("docente") Docente docente,
            @Param("diaSemana") DisponibilidadDocente.DiaSemana diaSemana,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin);

    void deleteByDocente(Docente docente);
}
