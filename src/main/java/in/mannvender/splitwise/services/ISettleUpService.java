package in.mannvender.splitwise.services;

import in.mannvender.splitwise.dtos.SettleUpGroupRequestDto;
import in.mannvender.splitwise.dtos.SettleUpGroupResponseDto;
import in.mannvender.splitwise.dtos.SettleUpUserRequestDto;
import in.mannvender.splitwise.dtos.SettleUpUserResponseDto;
import in.mannvender.splitwise.models.Expense;

import java.util.List;

public interface ISettleUpService {
    List<Expense> settleUpUser(Long userId);
    List<Expense> settleUpGroup(Long groupId);
}
