package in.mannvender.splitwise.repositories;

import in.mannvender.splitwise.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session, Long> {
    Optional<Session> findSessionByTokenAndUser_Id(String token, Long userId);
    Session save(Session session);
}
