package com.pontificia.horarioponti.modules.teaching_type;

import com.pontificia.horarioponti.enums.ETeachingType;
import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import com.pontificia.horarioponti.modules.teaching_type.mapper.TeachingTypeMapper;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TeachingTypeService extends BaseService<TeachingTypeEntity> {

    private final TeachingTypeRepository teachingTypeRepository;
    private final TeachingTypeMapper teachingTypeMapper;

    public TeachingTypeService(TeachingTypeRepository teachingTypeRepository,
                               TeachingTypeMapper teachingTypeMapper) {
        super(teachingTypeRepository);
        this.teachingTypeRepository = teachingTypeRepository;
        this.teachingTypeMapper = teachingTypeMapper;
    }

    /**
     * Inicializa tipos de enseñanza por defecto si no existen en la base de datos.
     * Crea dos entradas: THEORY y PRACTICE.
     */
    @PostConstruct
    @Transactional
    public void initializeTeachingTypes() {
        if (teachingTypeRepository.count() == 0) {
            TeachingTypeEntity theoretical = new TeachingTypeEntity();
            theoretical.setName(ETeachingType.THEORY);

            TeachingTypeEntity practical = new TeachingTypeEntity();
            practical.setName(ETeachingType.PRACTICE);

            saveAll(Arrays.asList(theoretical, practical));
        }
    }

    /**
     * Devuelve todos los tipos de enseñanza en forma de DTO.
     *
     * @return lista de objetos TeachingTypeResponseDTO
     */
    public List<TeachingTypeResponseDTO> getAllTeachingTypes() {
        List<TeachingTypeEntity> types = findAll();
        return teachingTypeMapper.toResponseDTOList(types);
    }

    /**
     * Devuelve todos los tipos de enseñanza en forma de DTO.
     *
     * @return lista de objetos TeachingTypeResponseDTO
     */
    public TeachingTypeResponseDTO getTeachingTypeById(UUID uuid) {
        TeachingTypeEntity type = findTeachingTypeOrThrow(uuid);
        return teachingTypeMapper.toResponseDTO(type);
    }

    /**
     * Devuelve un tipo de enseñanza por su UUID como DTO.
     *
     * @param uuid identificador único del tipo de enseñanza
     * @return TeachingTypeResponseDTO correspondiente
     * @throws EntityNotFoundException si no se encuentra el tipo
     */
    public TeachingTypeEntity findTeachingTypeOrThrow(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Teaching type not found with ID: " + uuid));
    }


    /**
     * Guarda una lista de entidades de tipo enseñanza en la base de datos.
     *
     * @param types lista de entidades a guardar
     * @return lista de entidades persistidas
     */
    @Transactional
    public List<TeachingTypeEntity> saveAll(List<TeachingTypeEntity> types) {
        return teachingTypeRepository.saveAll(types);
    }
}
