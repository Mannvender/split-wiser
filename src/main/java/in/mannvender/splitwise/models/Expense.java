package in.mannvender.splitwise.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "expenses")
public class Expense extends BaseModel {
    private String description;
    private double amount;
    @Enumerated(EnumType.ORDINAL)
    private ExpenseType expenseType;
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseUser> expenseUsers = new ArrayList<>();
    @ManyToOne
    private User createdBy;
    @ManyToOne
    private Group group;

    public String getExpenseTypeString() {
        return expenseType != null ? expenseType.name() : null;
    }
}
