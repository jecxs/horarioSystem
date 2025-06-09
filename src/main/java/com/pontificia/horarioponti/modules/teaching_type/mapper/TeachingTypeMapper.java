package com.pontificia.horarioponti.modules.teaching_type.mapper;

import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeachingTypeMapper {

    /**
     * Convierte una entidad {@link TeachingTypeEntity} a su correspondiente DTO {@link TeachingTypeResponseDTO}.
     * Mapea el campo enum 'name' a su representación {@link String} mediante el método {@link #enumName(Enum)}.
     *
     * @param entity La entidad que se quiere convertir.
     * @return El DTO correspondiente, o {@code null} si la entidad es {@code null}.
     */
    @Mapping(source = "name", target = "name", qualifiedByName = "enumName")
    TeachingTypeResponseDTO toResponseDTO(TeachingTypeEntity entity);


    /**
     * Convierte una lista de entidades {@link TeachingTypeEntity} a una lista de DTO {@link TeachingTypeResponseDTO}.
     *
     * @param entities Lista de entidades a convertir.
     * @return Lista de DTOs correspondientes.
     */
    List<TeachingTypeResponseDTO> toResponseDTOList(List<TeachingTypeEntity> entities);

    /**
     * Convierte un valor enum a su nombre {@link String}.
     * Este método es usado por MapStruct para mapear el campo enum {@code name} a {@link String} en el DTO.
     *
     * @param enumValue Valor enum a convertir.
     * @return El nombre del enum como {@link String}, o {@code null} si el valor es {@code null}.
     */
    @Named("enumName")
    default String enumName(Enum<?> enumValue) {
        return enumValue == null ? null : enumValue.name();
    }
}