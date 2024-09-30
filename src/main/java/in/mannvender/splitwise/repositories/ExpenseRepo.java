package in.mannvender.splitwise.repositories;

import in.mannvender.splitwise.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    Expense save(Expense expense);
    Optional<Expense> findById(Long expenseId);
    void deleteById(Long expenseId);
}
