package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.dtos.expense.AmountUserIdPair;
import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.models.ExpenseType;

import java.util.List;

public interface IExpenseService {
    public Expense createExpense(String description, double amount, Boolean isSettlement, Long groupId, Long createdByUserId, List<AmountUserIdPair> paidByUserIds, List<AmountUserIdPair> hadToPayUserIds);
    public Expense getExpenseById(Long expenseId);
    public void deleteExpenseById(Long expenseId);
}
