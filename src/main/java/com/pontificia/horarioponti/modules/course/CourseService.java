package com.pontificia.horarioponti.modules.course;

import com.pontificia.horarioponti.modules.course.dto.CourseFilterDTO;
import com.pontificia.horarioponti.modules.course.dto.CourseRequestDTO;
import com.pontificia.horarioponti.modules.course.dto.CourseResponseDTO;
import com.pontificia.horarioponti.modules.course.mapper.CourseMapper;
import com.pontificia.horarioponti.modules.cycle.CycleEntity;
import com.pontificia.horarioponti.modules.cycle.CycleService;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeEntity;
import com.pontificia.horarioponti.modules.teaching_type.TeachingTypeService;
import com.pontificia.horarioponti.utils.abstractBase.BaseService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService extends BaseService<CourseEntity> {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeachingTypeService teachingTypeService;
    private final CycleService cycleService;

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         CourseMapper courseMapper,
                         TeachingTypeService teachingTypeService,
                         CycleService cycleService) {
        super(courseRepository);
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.teachingTypeService = teachingTypeService;
        this.cycleService = cycleService;
    }

    public List<CourseResponseDTO> getAllCourses() {
        List<CourseEntity> courses = findAll();
        return courseMapper.toResponseDTOList(courses);
    }

    public CourseResponseDTO getCourseById(UUID uuid) {
        CourseEntity course = findCourseOrThrow(uuid);
        return courseMapper.toResponseDTO(course);
    }

    public CourseEntity findCourseOrThrow(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + uuid));
    }

    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO courseDTO) {
        // Verificar si ya existe un curso con el mismo nombre en el ciclo
        if (courseRepository.existsByNameAndCycleUuid(courseDTO.getName(), courseDTO.getCycleUuid())) {
            throw new IllegalArgumentException("Ya existe un curso con el mismo nombre en este ciclo");
        }

        // Obtener el ciclo
        CycleEntity cycle = cycleService.findCycleOrThrow(courseDTO.getCycleUuid());

        // Obtener los tipos de enseñanza
        Set<TeachingTypeEntity> teachingTypes = getTeachingTypesFromUuids(courseDTO.getTeachingTypeUuids());

        // Crear y guardar el curso
        CourseEntity course = courseMapper.toEntity(courseDTO, cycle, teachingTypes);
        CourseEntity savedCourse = save(course);

        return courseMapper.toResponseDTO(savedCourse);
    }

    @Transactional
    public CourseResponseDTO updateCourse(UUID uuid, CourseRequestDTO courseDTO) {
        // Verificar que exista el curso
        CourseEntity course = findCourseOrThrow(uuid);

        // Verificar si hay otro curso (no este mismo) con el mismo nombre en el ciclo
        boolean existsAnotherCourse = courseRepository.findByCycleUuid(courseDTO.getCycleUuid()).stream()
                .anyMatch(c -> c.getName().equals(courseDTO.getName()) && !c.getUuid().equals(uuid));

        if (existsAnotherCourse) {
            throw new IllegalArgumentException("Ya existe otro curso con el mismo nombre en este ciclo");
        }

        // Obtener el ciclo
        CycleEntity cycle = cycleService.findCycleOrThrow(courseDTO.getCycleUuid());

        // Obtener los tipos de enseñanza
        Set<TeachingTypeEntity> teachingTypes = getTeachingTypesFromUuids(courseDTO.getTeachingTypeUuids());

        // Actualizar el curso
        courseMapper.updateEntityFromDTO(course, courseDTO, cycle, teachingTypes);
        CourseEntity updatedCourse = save(course);

        return courseMapper.toResponseDTO(updatedCourse);
    }

    /*

    @Transactional
    public void deleteCourse(UUID uuid) {
        CourseEntity course = findCourseOrThrow(uuid);

        // Verificar si tiene asignaciones
        if (course.getAssignments() != null && !course.getAssignments().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el curso porque tiene asignaciones de horario");
        }

        deleteById(uuid);
    }

    */
    public List<CourseResponseDTO> filterCourses(CourseFilterDTO filters) {
        List<CourseEntity> courses = new ArrayList<>();

        // Aplicar filtros en orden de especificidad
        if (filters.getModalityUuid() != null) {
            if (filters.getCareerUuid() != null) {
                if (filters.getCycleUuid() != null) {
                    // Filtrar por ciclo
                    courses = courseRepository.findByCycleUuid(filters.getCycleUuid());
                } else {
                    // Filtrar por carrera
                    courses = courseRepository.findByCareerUuid(filters.getCareerUuid());
                }
            } else {
                // Filtrar por modalidad
                courses = courseRepository.findByModalityUuid(filters.getModalityUuid());
            }
        } else {
            // Sin filtros específicos, traer todos
            courses = findAll();
        }

        // Filtrar por nombre si se especificó
        if (filters.getCourseName() != null && !filters.getCourseName().trim().isEmpty()) {
            final String searchTerm = filters.getCourseName().toLowerCase();
            courses = courses.stream()
                    .filter(c -> c.getName().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
        }

        return courseMapper.toResponseDTOList(courses);
    }

    /**
     * Obtiene las entidades TeachingType a partir de una lista de UUID
     */
    private Set<TeachingTypeEntity> getTeachingTypesFromUuids(List<UUID> teachingTypeUuids) {
        return teachingTypeUuids.stream()
                .map(teachingTypeService::findTeachingTypeOrThrow)
                .collect(Collectors.toSet());
    }
}
