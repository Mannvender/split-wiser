package in.mannvender.splitwise.strategies;

import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.models.ExpenseUser;

import java.util.List;

public interface SettleUpStrategy {
    List<Expense> settleUp(List<Expense> expenses);
}
