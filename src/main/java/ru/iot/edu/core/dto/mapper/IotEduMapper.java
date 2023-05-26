package ru.iot.edu.core.dto.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.iot.edu.core.dto.*;
import ru.iot.edu.model.Course;
import ru.iot.edu.model.Stand;
import ru.iot.edu.model.StudentTask;
import ru.iot.edu.model.Task;
import ru.iot.edu.model.auth.User;
import ru.iot.edu.repository.CourseRepository;
import ru.iot.edu.repository.GroupRepository;

@Component
public class IotEduMapper extends ConfigurableMapper implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public IotEduMapper() {
        super(false);
    }


    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(User.class, UserDto.class)
                .byDefault()
                .register();

        factory.classMap(Course.class, CourseDto.class)
                .byDefault()
                .register();

        factory.classMap(Task.class, TaskDto.class)
                .byDefault()
                .register();

        factory.classMap(Stand.class, StandDto.class)
                .byDefault()
                .register();

        factory.classMap(StudentTask.class, StudentTaskDto.class)
                .byDefault()
                .register();

        GroupRepository groupRepository = applicationContext.getBean(GroupRepository.class);
        CourseRepository courseRepository = applicationContext.getBean(CourseRepository.class);
        factory.getConverterFactory()
                .registerConverter(new CourseGroupMapper(groupRepository, courseRepository));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        init();
    }
}
