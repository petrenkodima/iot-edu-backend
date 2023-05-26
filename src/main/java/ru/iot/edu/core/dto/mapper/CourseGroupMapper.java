package ru.iot.edu.core.dto.mapper;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import ru.iot.edu.core.dto.GroupCourseDto;
import ru.iot.edu.model.Course;
import ru.iot.edu.model.Group;
import ru.iot.edu.model.GroupCourse;
import ru.iot.edu.repository.CourseRepository;
import ru.iot.edu.repository.GroupRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

class CourseGroupMapper extends BidirectionalConverter<GroupCourseDto, GroupCourse> {
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    CourseGroupMapper(GroupRepository groupRepository, CourseRepository courseRepository) {
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public GroupCourse convertTo(GroupCourseDto source, Type<GroupCourse> destinationType, MappingContext mappingContext) {
        Optional<Group> group = groupRepository.findById(source.getGroupId());
        Optional<Course> course = courseRepository.findById(source.getCourseId());
        return GroupCourse.builder()
                .id(source.getId())
                .group(group.orElseThrow(() -> new NoSuchElementException("not found group")))
                .course(course.orElseThrow(() -> new NoSuchElementException("not found course")))
                .isShow(source.isShow())
                .build();
    }

    @Override
    public GroupCourseDto convertFrom(GroupCourse source, Type<GroupCourseDto> destinationType, MappingContext mappingContext) {
        return GroupCourseDto.builder()
                .id(source.getId())
                .groupId(source.getGroup().getId())
                .courseId(source.getCourse().getId())
                .isShow(source.isShow())
                .build();
    }
}