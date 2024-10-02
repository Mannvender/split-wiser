package in.mannvender.splitwise.services;

import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.models.ExpenseUser;
import in.mannvender.splitwise.models.Group;
import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.repositories.ExpenseUserRepo;
import in.mannvender.splitwise.repositories.GroupRepo;
import in.mannvender.splitwise.repositories.UserRepo;
import in.mannvender.splitwise.services.interfaces.ISettleUpService;
import in.mannvender.splitwise.strategies.SettleUpStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SettleUpServiceImpl implements ISettleUpService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private ExpenseUserRepo expenseUserRepo;
    @Autowired
    private SettleUpStrategy settleUpStrategy;
    @Override
    public List<Expense> settleUpUser(Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User not found");
        }

        List<ExpenseUser> expenseUsers = expenseUserRepo.findByUser_Id(userId);

//        Dinner expense (E)
//                u1, u2, u3, u4
//        PaidBy <E, u1, 800> <E, u3, 1200>
//                HadToPay <E, u1, 500> <E, u2, 500> <E, u3, 500> <E, u4, 500>
//
//                Request to settle Up u3
//        ExpenseUsers involving u3 -> <E, u3, 1200> <E, u3, 500>
        Set<Expense> expenses = new HashSet<>();
        for(ExpenseUser expenseUser : expenseUsers){
            expenses.add(expenseUser.getExpense());
        }

        // Iterate all expenses and find out who has paid extra/lesser
        // For every user involved in expenses
        // use MinHeap and MaxHeap to find out list of settle up transactions
        return settleUpStrategy.settleUp(expenses.stream().toList());
    }

    @Override
    public List<Expense> settleUpGroup(Long groupId) {
        Optional<Group> optionalGroup = groupRepo.findById(groupId);
        if(optionalGroup.isEmpty()){
            throw new RuntimeException("Group not found");
        }
        return settleUpStrategy.settleUp(optionalGroup.get().getExpenses());
    }
}
