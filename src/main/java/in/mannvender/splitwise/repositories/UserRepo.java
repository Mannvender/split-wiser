package in.mannvender.splitwise.repositories;

import in.mannvender.splitwise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    // read
    List<User> findAll();
    List<User> findAllById(Iterable<Long> userIds);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);

    boolean existsById(Long userId);

    // create/update
    User save(User user);

    // delete
    User deleteUserById(Long userId);

}
