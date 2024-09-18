package in.mannvender.splitwise.dtos.expense;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmountUserIdPair {
    private double amount;
    private Long userId;
}
