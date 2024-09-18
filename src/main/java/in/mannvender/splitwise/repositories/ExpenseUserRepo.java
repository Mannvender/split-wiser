package in.mannvender.splitwise.repositories;

import in.mannvender.splitwise.models.ExpenseUser;
import in.mannvender.splitwise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseUserRepo extends JpaRepository<ExpenseUser, Long> {
    List<ExpenseUser> findByUser_Id(Long userId);
}
