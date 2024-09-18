package in.mannvender.splitwise.services;

import in.mannvender.splitwise.dtos.expense.AmountUserIdPair;
import in.mannvender.splitwise.models.*;
import in.mannvender.splitwise.repositories.ExpenseRepo;
import in.mannvender.splitwise.repositories.UserRepo;
import in.mannvender.splitwise.services.interfaces.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements IExpenseService {
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public Expense createExpense(String description, double amount, Boolean isSettlement, Long groupId, Long createdByUserId, List<AmountUserIdPair> paidByUserIds, List<AmountUserIdPair> hadToPayUserIds) {
        // Create User, ExpenseUser and Expense objects
        // Save the Expense object in the database
        // Return the Expense object
        Optional<User> optionalUser = userRepo.findById(createdByUserId);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        User createdByUser = optionalUser.get();
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setExpenseType(isSettlement ? ExpenseType.DUMMY : ExpenseType.REAL);
        expense.setCreatedBy(createdByUser);
        for(AmountUserIdPair paidByUserId : paidByUserIds){
            ExpenseUser expenseUser = insertExpenseUser(expense, paidByUserId);
            expenseUser.setExpenseUserType(ExpenseUserType.PAID_BY);
            expense.getExpenseUsers().add(expenseUser);
        }
        for(AmountUserIdPair hadToPayUserId : hadToPayUserIds){
            ExpenseUser expenseUser = insertExpenseUser(expense, hadToPayUserId);
            expenseUser.setExpenseUserType(ExpenseUserType.HAD_TO_PAY);
            expense.getExpenseUsers().add(expenseUser);
        }
        return expenseRepo.save(expense);
    }

    private ExpenseUser insertExpenseUser(Expense expense, AmountUserIdPair hadToPayUserId) {
        Optional<User> optionalHadToPayUser = userRepo.findById(hadToPayUserId.getUserId());
        if(optionalHadToPayUser.isEmpty()){
            throw new RuntimeException("User not found");
        }
        User hadToPayUser = optionalHadToPayUser.get();
        ExpenseUser expenseUser = new ExpenseUser();
        expenseUser.setExpense(expense);
        expenseUser.setUser(hadToPayUser);
        expenseUser.setAmount(hadToPayUserId.getAmount());
        return expenseUser;
    }

    @Override
    public Expense getExpenseById(Long expenseId) {
        return null;
    }
}
