package in.mannvender.splitwise.dtos.expense;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseResponseDto {
    private Long id;
    private String description;
    private double amount;
    private String expenseType;
    private String createdBy;
    private String group;
    private String createdAt;
}
