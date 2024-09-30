package in.mannvender.splitwise.services;

import in.mannvender.splitwise.dtos.expense.AmountUserIdPair;
import in.mannvender.splitwise.models.*;
import in.mannvender.splitwise.repositories.ExpenseRepo;
import in.mannvender.splitwise.repositories.ExpenseUserRepo;
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
    @Autowired
    private ExpenseUserRepo expenseUserRepo;

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

        // Save the expense first
        Expense savedExpense = expenseRepo.save(expense);

        for(AmountUserIdPair paidByUserId : paidByUserIds){
            ExpenseUser expenseUser = insertExpenseUser(savedExpense, paidByUserId);
            expenseUser.setExpenseUserType(ExpenseUserType.PAID_BY);
            // save expenseUser
            ExpenseUser savedExpenseUser = expenseUserRepo.save(expenseUser);
            expense.getExpenseUsers().add(savedExpenseUser);
        }
        for(AmountUserIdPair hadToPayUserId : hadToPayUserIds){
            ExpenseUser expenseUser = insertExpenseUser(savedExpense, hadToPayUserId);
            expenseUser.setExpenseUserType(ExpenseUserType.HAD_TO_PAY);
            // save expenseUser
            ExpenseUser savedExpenseUser = expenseUserRepo.save(expenseUser);
            expense.getExpenseUsers().add(savedExpenseUser);
        }
        return savedExpense;
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
        Optional<Expense> optionalExpense = expenseRepo.findById(expenseId);
        if(optionalExpense.isEmpty()){
            throw new RuntimeException("Expense not found");
        }
        return optionalExpense.get();
    }
}
