package in.mannvender.splitwise.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Expense extends BaseModel {
    private String description;
    private double amount;
    private ExpenseType expenseType;
    private List<ExpenseUser> expenseUsers;
    private User createdBy;
    private Group group;
}
