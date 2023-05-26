package ru.iot.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.iot.edu.model.TeacherCourse;
import ru.iot.edu.repository.TeacherCourseRepository;

import java.util.List;

@Service
public class TeacherCourseService {
    private final TeacherCourseRepository teacherCourseRepository;

    @Autowired
    public TeacherCourseService(TeacherCourseRepository teacherCourseRepository) {
        this.teacherCourseRepository = teacherCourseRepository;
    }

    public List<TeacherCourse> getAllCoursesByTeacherId(long teacherId) {
        return teacherCourseRepository.findAllCoursesByTeacherId(teacherId);
    }
}
