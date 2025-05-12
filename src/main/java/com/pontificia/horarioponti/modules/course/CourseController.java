package com.pontificia.horarioponti.modules.course;

import com.pontificia.horarioponti.config.ApiResponse;
import com.pontificia.horarioponti.modules.course.dto.CourseFilterDTO;
import com.pontificia.horarioponti.modules.course.dto.CourseRequestDTO;
import com.pontificia.horarioponti.modules.course.dto.CourseResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/protected/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * Endpoint para obtener todos los cursos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(
                ApiResponse.success(courses, "Cursos recuperados con éxito")
        );
    }

    /**
     * Endpoint para obtener un curso por ID
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> getCourseById(@PathVariable UUID uuid) {
        CourseResponseDTO course = courseService.getCourseById(uuid);
        return ResponseEntity.ok(
                ApiResponse.success(course, "Curso recuperado con éxito")
        );
    }

    /**
     * Endpoint para crear un nuevo curso
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(
            @Valid @RequestBody CourseRequestDTO courseDTO) {
        CourseResponseDTO newCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(newCourse, "Curso creado con éxito"));
    }

    /**
     * Endpoint para actualizar un curso existente
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>> updateCourse(
            @PathVariable UUID uuid,
            @Valid @RequestBody CourseRequestDTO courseDTO) {
        CourseResponseDTO updatedCourse = courseService.updateCourse(uuid, courseDTO);
        return ResponseEntity.ok(
                ApiResponse.success(updatedCourse, "Curso actualizado con éxito")
        );
    }



    /* Endpoint para eliminar un curso (Falta implementar asignacion)
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable UUID uuid) {
        courseService.deleteCourse(uuid);
        return ResponseEntity.ok(
                ApiResponse.success(null, "Curso eliminado con éxito")
        );
    }
    */
    /**
     * Endpoint para filtrar cursos según criterios
     */
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> filterCourses(
            @RequestParam(required = false) UUID modalityUuid,
            @RequestParam(required = false) UUID careerUuid,
            @RequestParam(required = false) UUID cycleUuid,
            @RequestParam(required = false) String courseName) {

        CourseFilterDTO filters = CourseFilterDTO.builder()
                .modalityUuid(modalityUuid)
                .careerUuid(careerUuid)
                .cycleUuid(cycleUuid)
                .courseName(courseName)
                .build();

        List<CourseResponseDTO> courses = courseService.filterCourses(filters);
        return ResponseEntity.ok(
                ApiResponse.success(courses, "Cursos filtrados recuperados con éxito")
        );
    }
}
