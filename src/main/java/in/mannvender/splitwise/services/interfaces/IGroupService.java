package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.models.Group;
import in.mannvender.splitwise.models.GroupRole;
import in.mannvender.splitwise.models.User;

import java.util.List;
import java.util.Optional;

public interface IGroupService {
    public Group createGroup(String name, String description, User createdBy, List<GroupRole> members);

    Optional<Group> getGroupById(Long groupId);

    List<Group> getGroupsByLoggedInUser();

    void deleteGroup(Long groupId);
}
