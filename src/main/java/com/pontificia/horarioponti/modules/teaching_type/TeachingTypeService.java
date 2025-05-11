package com.pontificia.horarioponti.modules.teaching_type;

import com.pontificia.horarioponti.enums.ETeachingType;
import com.pontificia.horarioponti.modules.teaching_type.mapper.TeachingTypeMapper;
import com.pontificia.horarioponti.modules.teaching_type.dto.TeachingTypeResponseDTO;
import com.pontificia.horarioponti.utils.abstractBase.BaseRepository;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TeachingTypeService extends BaseService<TeachingType> {

    private final TeachingTypeRepository teachingTypeRepository;
    private final TeachingTypeMapper teachingTypeMapper;

    @Autowired
    public TeachingTypeService(TeachingTypeRepository teachingTypeRepository,
                              TeachingTypeMapper teachingTypeMapper) {
        this.teachingTypeRepository = teachingTypeRepository;
        this.teachingTypeMapper = teachingTypeMapper;
    }

    protected BaseRepository<TeachingType> getRepository() {
        return teachingTypeRepository;
    }

    // Inicialización de tipos básicos
    @PostConstruct
    @Transactional
    public void initializeTeachingTypes() {
        if (teachingTypeRepository.count() == 0) {
            TeachingType theoretical = new TeachingType();
            theoretical.setName(ETeachingType.THEORY);

            TeachingType practical = new TeachingType();
            practical.setName(ETeachingType.PRACTICE);

            saveAll(Arrays.asList(theoretical, practical));
        }
    }

    public List<TeachingTypeResponseDTO> getAllTeachingTypes() {
        List<TeachingType> types = findAll();
        return teachingTypeMapper.toResponseDTOList(types);
    }

    public TeachingTypeResponseDTO getTeachingTypeById(UUID uuid) {
        TeachingType type = findTeachingTypeOrThrow(uuid);
        return teachingTypeMapper.toResponseDTO(type);
    }

    public TeachingType findTeachingTypeOrThrow(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Teaching type not found with ID: " + uuid));
    }


    /**
     * Guarda una colección de tipos de enseñanza
     */
    @Transactional
    public List<TeachingType> saveAll(List<TeachingType> types) {
        return teachingTypeRepository.saveAll(types);
    }
}
