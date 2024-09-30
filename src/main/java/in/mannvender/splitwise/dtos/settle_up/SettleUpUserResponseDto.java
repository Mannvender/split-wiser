package in.mannvender.splitwise.dtos.settle_up;

import in.mannvender.splitwise.dtos.expense.ExpenseResponseDto;
import in.mannvender.splitwise.models.Expense;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SettleUpUserResponseDto {
    private List<ExpenseResponseDto> transactions = new ArrayList<>();
}
