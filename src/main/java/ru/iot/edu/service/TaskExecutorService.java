package ru.iot.edu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.iot.edu.core.dto.ExecuteTaskDto;
import ru.iot.edu.model.StudentTask;
import ru.iot.edu.model.Task;
import ru.iot.edu.model.auth.User;
import ru.iot.edu.repository.StudentTasksRepository;

import java.util.Map;
import java.util.NoSuchElementException;

import static ru.iot.edu.util.FileUtil.getUploadedPath;

@Service
public class TaskExecutorService {
    private final StudentTasksRepository studentTasksRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    //todo add config url server:
    String serverUrl = "http://localhost:8080/api/task";

    //todo add ping server
    private final RestTemplate restTemplate = new RestTemplate();

    public TaskExecutorService(StudentTasksRepository studentTasksRepository) {
        this.studentTasksRepository = studentTasksRepository;
    }

    public HttpEntity<String> executeScript(long studentTaskId, User user) {

        StudentTask studentTask = studentTasksRepository.findById(studentTaskId)
                .orElseThrow(() -> new NoSuchElementException("not found task"));
        Task task = studentTask.getTask();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        String filePath = getUploadedPath(studentTask);

        String userRole = user.getRoles()
                .stream()
                .findFirst()
                .map(Enum::toString)
                .orElseThrow(() -> new NoSuchElementException("not found user role"));

        ExecuteTaskDto executeTask = ExecuteTaskDto.builder()
                .nameFile(studentTask.getFilePath())
                .initiatorUsername(user.getUsername())
                .labID(task.getId())
                .standId(studentTask.getTask().getStand().getId())
                .roleUsername(userRole)
                .duration(task.getDuration())
                .usbPort(studentTask.getTask().getStand().getPortName())
                .build();

        Map<String, Object> fieldMap = mapper.convertValue(
                executeTask,
                new TypeReference<>() {
                }
        );
        body.setAll(fieldMap);
        body.add("file", new FileSystemResource(filePath));


        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        return restTemplate
                .postForEntity(serverUrl, requestEntity, String.class);
    }

}
