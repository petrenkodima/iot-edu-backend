package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.core.dto.CourseDto;
import ru.iot.edu.core.dto.CreateCourseDto;
import ru.iot.edu.core.dto.mapper.IotEduMapper;
import ru.iot.edu.model.*;
import ru.iot.edu.model.auth.User;
import ru.iot.edu.repository.*;
import ru.iot.edu.service.CourseService;
import ru.iot.edu.service.auth.UserDetailsServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;


@RestController
@RequestMapping(value = API_PATH + "/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    //todo to service
    private final CourseRepository courseRepository;

    private final TeacherRepository teacherRepository;

    private final UserDetailsServiceImpl userService;
    private final GroupRepository groupRepository;
    private final TeacherCourseRepository teacherCourseRepository;
    private final StudentGroupRepository studentGroupRepository;
    private final GroupCourseRepository groupCourseRepository;
    private final IotEduMapper mapper;
    private final CourseService courseService;

    public CourseController(CourseRepository courseRepository,
                            TeacherRepository teacherRepository, UserDetailsServiceImpl userService,
                            GroupRepository groupRepository,
                            TeacherCourseRepository teacherCourseRepository,
                            StudentGroupRepository studentGroupRepository,
                            GroupCourseRepository groupCourseRepository,
                            IotEduMapper mapper, CourseService courseService) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.userService = userService;
        this.groupRepository = groupRepository;
        this.teacherCourseRepository = teacherCourseRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.groupCourseRepository = groupCourseRepository;
        this.mapper = mapper;
        this.courseService = courseService;
    }

    //todo this method not work because findAllByStudentsId - not correct write
//    @GetMapping("/students/{studentId}")
//    @Operation(summary = "Получить все курсы для студента", description = "Позволяет получить список всех курсов, на которые записан студент")
//    public ResponseEntity<List<Course>> getAllForStudent(@PathVariable Long studentId) {
//        List<Course> courses = courseService.getAllForStudent(studentId);
//        return ResponseEntity.ok(courses);
//    }

    /**
     * Получить все курсы
     *
     * @return список всех курсов
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TEACHER', 'ROLE_ADMIN')")
    @Operation(summary = "Получить все курсы", tags = {"courses"})
    @ApiResponse(responseCode = "200", description = "Список всех курсов")
    public ResponseEntity<List<CourseDto>> getAll(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findUserByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("No found user"));
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        List<Course> all;

        if (roles.contains("ROLE_STUDENT")) {
            List<StudentGroup> studentGroups = studentGroupRepository.findallbystudentuserId(user.getId());
            all = groupCourseRepository.findAllByGroupIn(studentGroups.stream().map(StudentGroup::getGroup).collect(Collectors.toList()))
                    .stream()
                    .map(GroupCourse::getCourse)
                    .collect(Collectors.toList());
        } else if (roles.contains("ROLE_TEACHER")) {
            Teacher teacher = teacherRepository.findByUserUsername(authentication.getName())
                    .orElseThrow(() -> new NoSuchElementException("Not found teacher"));
            all = teacherCourseRepository.findAllCoursesByTeacherId(teacher.getId())
                    .stream()
                    .map(TeacherCourse::getCourse)
                    .collect(Collectors.toList());
        } else {
            all = courseRepository.findAll();
        }

        return ResponseEntity.ok(mapper.mapAsList(all, CourseDto.class));
    }

    /**
     * Создать курс
     *
     * @param courseDto данные для создания курса
     * @return созданный курс
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @Operation(summary = "Создать курс", tags = {"courses"})
    @ApiResponse(responseCode = "200", description = "Созданный курс")
    public CourseDto create(Authentication authentication, @RequestBody CreateCourseDto courseDto) {
        Course course = Course.builder()
                .name(courseDto.getName())
                .description(courseDto.getDescription())
                .build();
        Course saveCourse = courseRepository.save(course);

        Teacher teacher = teacherRepository
                .findAll()
                .stream()
                .filter(x -> x.getUser().getUsername().equals(authentication.getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("not found teacher"));

        teacherCourseRepository.save(TeacherCourse.builder()
                .teacher(teacher)
                .course(saveCourse)
                .build());

        courseDto.getGroupsIds().forEach(groupId -> {
                    Group group = groupRepository.findById(groupId)
                            .orElseThrow(() -> new NoSuchElementException("не найдены группы с id " + groupId));
                    groupCourseRepository.save(GroupCourse.builder()
                            .course(saveCourse)
                            .group(group)
                            .build()
                    );
                }
        );

        return mapper.map(saveCourse, CourseDto.class);
    }

    /**
     * Метод удаления курса
     *
     * @param id идентификатор курса
     * @return ResponseEntity с кодом ответа 204 в случае успешного удаления курса, либо кодом ответа 404 в случае, если курс не найден
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @Operation(summary = "Удалить курс", description = "Удалить курс по его идентификатору")
    @ApiResponse(responseCode = "204", description = "Курс успешно удален")
    @ApiResponse(responseCode = "404", description = "Курс не найден")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}
