package in.mannvender.splitwise.services;

import in.mannvender.splitwise.config.UserContext;
import in.mannvender.splitwise.models.Group;
import in.mannvender.splitwise.models.GroupRole;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.repositories.GroupRepo;
import in.mannvender.splitwise.repositories.UserRepo;
import in.mannvender.splitwise.services.interfaces.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements IGroupService {
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public Group createGroup(String name, String description, User createdBy, List<GroupRole> groupRoles) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(createdBy);
        group.setGroupRoles(groupRoles);
        return groupRepo.save(group);
    }

    @Override
    public Optional<Group> getGroupById(Long groupId) {
        return groupRepo.findById(groupId);
    }

    @Override
    public List<Group> getGroupsByUserId(Long userId) {
        User currentUser = UserContext.getUser();
        System.out.println(currentUser + " " + userId);
        if(currentUser == null || !currentUser.getId().equals(userId)){
            throw new RuntimeException("Unauthorized access");
        }
        return groupRepo.findByGroupRoles(List.of(currentUser));
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepo.deleteById(groupId);
    }
}
