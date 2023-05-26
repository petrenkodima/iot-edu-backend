package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.core.dto.StandDto;
import ru.iot.edu.core.dto.mapper.IotEduMapper;
import ru.iot.edu.model.Stand;
import ru.iot.edu.repository.StandRepository;
import ru.iot.edu.service.StandService;

import java.util.List;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;


@RestController
@RequestMapping(value = API_PATH + "/stands")
@CrossOrigin(origins = "*")
public class StandController {
    //todo to service
    private final StandRepository standRepository;
    private final IotEduMapper mapper;

    private final StandService standService;

    public StandController(StandRepository standRepository, IotEduMapper mapper, StandService standService) {
        this.standRepository = standRepository;
        this.mapper = mapper;
        this.standService = standService;
    }

    /**
     * Получение списка всех стендов
     * @return Список объектов StandDto
     */
    @GetMapping
    @Operation(summary = "Получение списка стендов", description = "Получение списка всех стендов")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка стендов")
    public List<StandDto> getAllTest() {
        return mapper.mapAsList(standRepository.findAll(), StandDto.class);
    }

    /**
     * Создает новый стенд.
     *
     * @param standDto данные о новом стенде
     * @return созданный стенд
     */
    @PostMapping
    @Operation(summary = "Создание нового стенда", description = "Создание нового стенда")
    @ApiResponse(responseCode = "200", description = "Успешное создание нового стенда")
    public StandDto create(@RequestBody StandDto standDto) {
        Stand stand = mapper.map(standDto, Stand.class);
        //todo need dto
        Stand saveStand = standRepository.save(stand);
        return mapper.map(saveStand, StandDto.class);
    }


    /**
     * Удалить стенд по его идентификатору.
     *
     * @param standId идентификатор стенда
     * @return HTTP-ответ со статусом 204 No Content
     * @throws NotFoundException если стенд не найден
     */
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @DeleteMapping("/{standId}")
    @Operation(summary = "Удалить стенд", description = "Удалить стенд по его идентификатору")
    @ApiResponse(responseCode = "204", description = "Стенд успешно удален")
    @ApiResponse(responseCode = "404", description = "Стенд не найден")
    public ResponseEntity<Void> deleteStand(@PathVariable Long standId) throws NotFoundException {
        standService.deleteStand(standId);
        return ResponseEntity.noContent().build();
    }
}
