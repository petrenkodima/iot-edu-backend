package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.model.auth.User;
import ru.iot.edu.repository.GroupRepository;
import ru.iot.edu.repository.UserRepo;

import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepo userRepo;

    public GroupService(GroupRepository groupRepository, UserRepo userRepo) {
        this.groupRepository = groupRepository;
        this.userRepo = userRepo;
    }

    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }
}
