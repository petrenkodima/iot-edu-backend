package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.core.dto.GroupCourseDto;
import ru.iot.edu.core.dto.mapper.IotEduMapper;
import ru.iot.edu.model.GroupCourse;
import ru.iot.edu.repository.GroupCourseRepository;
import ru.iot.edu.service.CourseGroupService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;


@RestController
@RequestMapping(value = API_PATH + "/group_course")
@CrossOrigin(origins = "*")
public class GroupCourseController {
    //todo to service
    private final GroupCourseRepository groupCourseRepository;
    private final CourseGroupService courseGroupService;
    private final IotEduMapper mapper;

    public GroupCourseController(GroupCourseRepository groupCourseRepository, CourseGroupService courseGroupService, IotEduMapper mapper) {
        this.groupCourseRepository = groupCourseRepository;
        this.courseGroupService = courseGroupService;
        this.mapper = mapper;
    }

    /**
     * Получение всех групп курсов
     * @return список DTO групп курсов
     */
    @GetMapping
    @Operation(summary = "Получение всех групп курсов", description = "Получение списка всех групп курсов системы")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка групп курсов")
    public List<GroupCourseDto> getAll() {
        return mapper.mapAsList(groupCourseRepository.findAll(), GroupCourseDto.class);
    }


    /**
     * Создание новой группы курсов
     * @param groupCourseDto DTO новой группы курсов
     * @return DTO созданной группы курсов
     */
    @PostMapping
    @Operation(summary = "Создание новой группы курсов", description = "Создание новой группы курсов в системе")
    @ApiResponse(responseCode = "200", description = "Успешное создание новой группы курсов")
    public GroupCourseDto create(@RequestBody GroupCourseDto groupCourseDto) {
        GroupCourse groupCourse = mapper.map(groupCourseDto, GroupCourse.class);
        GroupCourse saveGroupCourse = groupCourseRepository.save(groupCourse);
        return mapper.map(saveGroupCourse, GroupCourseDto.class);
    }

    /**
     * Удаляет группу курсов по ее идентификатору
     *
     * @param groupId идентификатор группы курсов, которую нужно удалить
     * @return ResponseEntity со статусом 204 No Content, если группа курсов успешно удалена
     * @throws EntityNotFoundException если группа курсов с указанным идентификатором не найдена
     */
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @DeleteMapping("/{groupId}")
    @Operation(summary = "Удалить группу курсов", description = "Удалить группу курсов по её идентификатору")
    @ApiResponse(responseCode = "204", description = "Группа курсов успешно удалена")
    @ApiResponse(responseCode = "404", description = "Группа курсов не найдена")
    public ResponseEntity<Void> deleteCourseGroup(@PathVariable Long groupId) {
        courseGroupService.deleteCourseGroup(groupId);
        return ResponseEntity.noContent().build();
    }
}
