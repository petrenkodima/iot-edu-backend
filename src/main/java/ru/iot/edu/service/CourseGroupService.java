package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.model.GroupCourse;
import ru.iot.edu.repository.GroupCourseRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class CourseGroupService {
    private final GroupCourseRepository courseGroupRepository;

    public CourseGroupService(GroupCourseRepository groupCourseRepository) {
        this.courseGroupRepository = groupCourseRepository;
    }

    /**
     * Удаляет группу курсов по ее идентификатору
     *
     * @param groupId идентификатор группы курсов, которую нужно удалить
     * @throws EntityNotFoundException если группа курсов с указанным идентификатором не найдена
     */
    public void deleteCourseGroup(Long groupId) {
        GroupCourse groupCourse = courseGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Course group not found with id: " + groupId));

        courseGroupRepository.delete(groupCourse);
    }
}
