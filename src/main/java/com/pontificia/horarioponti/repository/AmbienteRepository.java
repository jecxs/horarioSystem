package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.Ambiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmbienteRepository extends JpaRepository<Ambiente, Long> {

    List<Ambiente> findByTipo(Ambiente.TipoAmbiente tipo);

    List<Ambiente> findByCapacidadGreaterThanEqual(Integer capacidad);

    @Query("SELECT a FROM Ambiente a WHERE a.tipo = :tipo AND a.capacidad >= :capacidadMinima")
    List<Ambiente> findByTipoAndCapacidadMinima(Ambiente.TipoAmbiente tipo, Integer capacidadMinima);

    boolean existsByNombre(String nombre);
}
