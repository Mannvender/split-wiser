package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.models.Expense;

import java.util.List;

public interface ISettleUpService {
    List<Expense> settleUpUser(Long userId);
    List<Expense> settleUpGroup(Long groupId);
}
