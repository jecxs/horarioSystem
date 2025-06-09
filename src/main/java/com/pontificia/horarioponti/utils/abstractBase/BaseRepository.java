package com.pontificia.horarioponti.utils.abstractBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Repositorio base genérico para todas las entidades que usan UUID como identificador.
 * Esta interfaz extiende {@link JpaRepository} y proporciona las operaciones CRUD estándar
 * Está anotada con {@link NoRepositoryBean}, lo que indica que Spring no debe instanciar
 * un bean de este repositorio directamente. En cambio, debe ser extendido por repositorios específicos
 * de cada entidad.
 *
 * @param <T> Tipo de la entidad.
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, UUID> { }