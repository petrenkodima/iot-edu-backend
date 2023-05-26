package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.core.dto.CreateGroupDto;
import ru.iot.edu.core.dto.GroupDto;
import ru.iot.edu.core.dto.mapper.IotEduMapper;
import ru.iot.edu.model.*;
import ru.iot.edu.repository.*;
import ru.iot.edu.service.GroupService;

import java.util.List;
import java.util.NoSuchElementException;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;


@RestController
@RequestMapping(value = API_PATH + "/groups")
@CrossOrigin(origins = "*")
public class GroupController {
    //todo to service
    private final GroupRepository groupRepository;
    private final IotEduMapper mapper;

    private final GroupService groupService;

    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;


    public GroupController(GroupRepository groupRepository,
                           IotEduMapper mapper,
                           GroupService groupService,
                           StudentRepository studentRepository,
                           StudentGroupRepository studentGroupRepository) {
        this.groupRepository = groupRepository;
        this.mapper = mapper;
        this.groupService = groupService;
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
    }


    /**
     * Получить все группы.
     *
     * @return список групп {@link GroupDto}
     */
    @GetMapping
    @Operation(summary = "Получить все группы")
    @ApiResponse(responseCode = "200", description = "Список групп успешно получен")
    public List<GroupDto> getAll() {
        return mapper.mapAsList(groupRepository.findAll(), GroupDto.class);
    }

    /**
     * Создать новую группу.
     *
     * @param groupDto данные новой группы {@link GroupDto}
     * @return созданная группа {@link GroupDto}
     */
    @PostMapping
    @Operation(summary = "Создать новую группу")
    @ApiResponse(responseCode = "200", description = "Группа успешно создана")
    public ResponseEntity<GroupDto> create(@RequestBody CreateGroupDto groupDto) {
        if (groupDto.getStudentsIds().isEmpty()) {
            ResponseEntity.badRequest();
        }
        //todo transaction
        Group newGroup = Group.builder()
                .description(groupDto.getDescription())
                .name(groupDto.getName())
                .build();
        Group saveGroup = groupRepository.save(newGroup);
        groupDto.getStudentsIds().forEach(s -> {
            Student findStudent = studentRepository.findById(s).orElseThrow(() -> new NoSuchElementException("not found user"));
            studentGroupRepository.save(StudentGroup.builder()
                    .group(saveGroup)
                    .student(findStudent)
                    .build()
            );
        });

        return ResponseEntity.ok(mapper.map(saveGroup, GroupDto.class));
    }

    /**
     * Удаляет группу по её идентификатору.
     *
     * @param groupId идентификатор удаляемой групп
     * @return ResponseEntity со статусом 204 (Группа успешно удалена) или 404 (Группа не найдена)
     */
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @DeleteMapping("/{groupId}")
    @Operation(summary = "Удалить группу", description = "Удалить группу по её идентификатору")
    @ApiResponse(responseCode = "204", description = "Группа успешно удалена")
    @ApiResponse(responseCode = "404", description = "Группа не найдена")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }
}
