package in.mannvender.splitwise.dtos.settle_up;

import in.mannvender.splitwise.dtos.expense.ExpenseResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SettleUpGroupResponseDto {
    private Long groupId;
    private List<ExpenseResponseDto> transactions = new ArrayList<>();
}
