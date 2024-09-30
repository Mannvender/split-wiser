package in.mannvender.splitwise.dtos.expense;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExpenseResponseDto {
    private Long id;
    private String description;
    private double amount;
    private String expenseType;
    private Long createdBy;
    private String group;
    private String createdAt;
    private List<AmountUserIdPair> paidByUserIds = new ArrayList<>();
    private List<AmountUserIdPair> hadToPayUserIds = new ArrayList<>();
}
