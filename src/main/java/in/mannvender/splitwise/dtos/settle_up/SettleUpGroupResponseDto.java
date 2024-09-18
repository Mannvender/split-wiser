package in.mannvender.splitwise.dtos.settle_up;

import in.mannvender.splitwise.models.Expense;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SettleUpGroupResponseDto {
    private List<Expense> transactions;
}
