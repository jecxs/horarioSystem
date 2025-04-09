package com.pontificia.horarioponti.repository;

import com.pontificia.horarioponti.repository.model.Carrera;
import com.pontificia.horarioponti.repository.model.ModalidadEducativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    List<Carrera> findByModalidad(ModalidadEducativa modalidad);

    Optional<Carrera> findByNombreAndModalidad(String nombre, ModalidadEducativa modalidad);

    boolean existsByNombreAndModalidad(String nombre, ModalidadEducativa modalidad);
}
