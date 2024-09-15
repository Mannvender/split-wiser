package in.mannvender.splitwise.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseUser extends BaseModel {
    private Expense expense;
    private User user;
    private ExpenseUserType expenseUserType;
}
