package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.Group;
import ru.iot.edu.model.GroupCourse;

import java.util.List;

@Repository
public interface GroupCourseRepository extends JpaRepository<GroupCourse, Long> {
    List<GroupCourse> findAllByGroupIn(List<Group> groups);
}


