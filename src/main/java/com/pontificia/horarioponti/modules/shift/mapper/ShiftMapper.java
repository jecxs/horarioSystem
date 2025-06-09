package com.pontificia.horarioponti.modules.shift.mapper;

import com.pontificia.horarioponti.modules.shift.ShiftEntity;
import com.pontificia.horarioponti.modules.shift.dto.ShiftRequestDTO;
import com.pontificia.horarioponti.modules.shift.dto.ShiftResponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper para convertir entre {@link ShiftEntity} y sus DTO {@link ShiftRequestDTO} y {@link ShiftResponseDTO}.
 * Utiliza MapStruct para generar las implementaciones automáticamente.
 */
@Mapper(componentModel = "spring")
public interface ShiftMapper {

    /**
     * Convierte un {@link ShiftRequestDTO} a una entidad {@link ShiftEntity}.
     *
     * @param dto DTO de entrada con los datos del turno.
     * @return Entidad {@link ShiftEntity} lista para ser persistida.
     */
    ShiftEntity toEntity(ShiftRequestDTO dto);

    /**
     * Convierte una entidad {@link ShiftEntity} a su representación {@link ShiftResponseDTO}.
     *
     * @param entity Entidad del turno a convertir.
     * @return DTO con los datos del turno.
     */
    ShiftResponseDTO toResponseDTO(ShiftEntity entity);

    /**
     * Convierte una lista de entidades {@link ShiftEntity} a una lista de {@link ShiftResponseDTO}.
     *
     * @param entities Lista de entidades de turno.
     * @return Lista de DTO con la información de cada turno.
     */
    List<ShiftResponseDTO> toResponseDTOList(List<ShiftEntity> entities);

    /**
     * Actualiza una entidad {@link ShiftEntity} existente con los valores de un {@link ShiftRequestDTO}.
     * Solo se actualizan los campos no nulos del DTO.
     *
     * @param dto    DTO con los nuevos valores.
     * @param entity Entidad que será actualizada.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ShiftRequestDTO dto, @MappingTarget ShiftEntity entity);
}
