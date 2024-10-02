package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.models.Group;
import in.mannvender.splitwise.models.User;

import java.util.List;
import java.util.Optional;

public interface IGroupService {
    public Group createGroup(String name, String description, User createdBy, List<User> members);

    Optional<Group> getGroupById(Long groupId);
}
