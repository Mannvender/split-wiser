package in.mannvender.splitwise.dtos;

import in.mannvender.splitwise.models.Expense;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SettleUpUserResponseDto {
    private List<Expense> transactions;
}
