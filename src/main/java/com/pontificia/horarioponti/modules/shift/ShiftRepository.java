package com.pontificia.horarioponti.modules.shift;

import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad {@link ShiftEntity}.
 * Extiende {@link BaseRepository} para proporcionar operaciones CRUD
 * básicas y personalizadas para los turnos (shifts).
 */
@Repository
public interface ShiftRepository extends BaseRepository<ShiftEntity> {
}