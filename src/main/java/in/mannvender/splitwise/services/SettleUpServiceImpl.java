package in.mannvender.splitwise.services;

import in.mannvender.splitwise.dtos.SettleUpGroupRequestDto;
import in.mannvender.splitwise.dtos.SettleUpGroupResponseDto;
import in.mannvender.splitwise.dtos.SettleUpUserRequestDto;
import in.mannvender.splitwise.dtos.SettleUpUserResponseDto;
import in.mannvender.splitwise.models.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettleUpServiceImpl implements ISettleUpService {
    @Override
    public List<Expense> settleUpUser(Long userId) {
        return List.of();
    }

    @Override
    public List<Expense> settleUpGroup(Long groupId) {
        return List.of();
    }
}
