package in.mannvender.splitwise.services;

import in.mannvender.splitwise.models.Group;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.repositories.GroupRepo;
import in.mannvender.splitwise.services.interfaces.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements IGroupService {
    @Autowired
    private GroupRepo groupRepo;
    @Override
    public Group createGroup(String name, String description, User createdBy, List<User> members) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(createdBy);
        group.setMembers(members);
        return groupRepo.save(group);
    }

    @Override
    public Optional<Group> getGroupById(Long groupId) {
        return groupRepo.findById(groupId);
    }
}
