package in.mannvender.splitwise.dtos.expense;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExpenseRequestDto {
    private String description;
    private double amount;
    private Long groupId;
    private List<AmountUserIdPair> paidByUserIds;
    private List<AmountUserIdPair> hadToPayUserIds;
    private boolean isSettlement;
    private Long createdByUserId;
}
