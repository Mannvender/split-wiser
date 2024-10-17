package in.mannvender.splitwise.repositories;

import in.mannvender.splitwise.models.GroupRole;
import in.mannvender.splitwise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoleRepo extends JpaRepository<GroupRole, Long> {
    List<GroupRole> findGroupRolesByUser(User user);
}
