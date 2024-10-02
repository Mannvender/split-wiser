package in.mannvender.splitwise.repositories;

import in.mannvender.splitwise.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepo extends JpaRepository<Group, Long> {
    Optional<Group> findById(Long groupId);
    Group save(Group group);
}
