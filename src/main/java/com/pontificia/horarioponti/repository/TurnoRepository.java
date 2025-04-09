package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    Optional<Turno> findByNombre(String nombre);

    List<Turno> findByHoraInicioGreaterThanEqualAndHoraFinLessThanEqual(LocalTime horaInicio, LocalTime horaFin);

    @Query("SELECT t FROM Turno t WHERE :hora BETWEEN t.horaInicio AND t.horaFin")
    Optional<Turno> findTurnoContainingHora(LocalTime hora);

    boolean existsByNombre(String nombre);
}
