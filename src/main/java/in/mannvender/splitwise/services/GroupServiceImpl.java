package in.mannvender.splitwise.services;

import in.mannvender.splitwise.config.UserContext;
import in.mannvender.splitwise.models.Group;
import in.mannvender.splitwise.models.GroupRole;
import in.mannvender.splitwise.models.Status;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.repositories.GroupRepo;
import in.mannvender.splitwise.repositories.GroupRoleRepo;
import in.mannvender.splitwise.repositories.UserRepo;
import in.mannvender.splitwise.services.interfaces.IGroupService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements IGroupService {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(GroupServiceImpl.class);
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GroupRoleRepo groupRoleRepo;
    @Override
    public Group createGroup(String name, String description, User createdBy, List<GroupRole> groupRoles) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(createdBy);
        group.setGroupRoles(groupRoles);
        group.setStatus(Status.ACTIVE);

        // Save the group first to generate the group id
        Group savedGroup = groupRepo.save(group);

        // Set the group for each group role and save them
        groupRoles.forEach(groupRole -> {
            groupRole.setGroup(savedGroup);
            groupRoleRepo.save(groupRole);
        });

        return savedGroup;
    }

    @Override
    public Optional<Group> getGroupById(Long groupId) {
        return groupRepo.findById(groupId);
    }

    @Override
    public List<Group> getGroupsByLoggedInUser() {
        User currentUser = UserContext.getUser();
        if(currentUser == null){
            throw new RuntimeException("Unauthorized access");
        }
        List<GroupRole> groupRoles = groupRoleRepo.findGroupRolesByUser(currentUser);
        return groupRoles.stream().map(GroupRole::getGroup).toList();
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepo.deleteById(groupId);
    }
}
