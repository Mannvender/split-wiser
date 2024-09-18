package in.mannvender.splitwise.repositories;

import in.mannvender.splitwise.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Long> {
    public Expense save(Expense expense);
}
