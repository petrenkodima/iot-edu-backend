package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.model.Course;
import ru.iot.edu.repository.CourseRepository;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);

        if (course != null) {
            courseRepository.delete(course);
        }
    }

    //todo this method not work because findAllByStudentsId - not correct write
//    public List<Course> getAllForStudent(Long studentId) {
//        return courseRepository.findAllByStudentsId(studentId);
//    }
}
