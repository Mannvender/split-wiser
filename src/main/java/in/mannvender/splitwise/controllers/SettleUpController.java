package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.dtos.expense.ExpenseResponseDto;
import in.mannvender.splitwise.dtos.settle_up.SettleUpGroupRequestDto;
import in.mannvender.splitwise.dtos.settle_up.SettleUpGroupResponseDto;
import in.mannvender.splitwise.dtos.settle_up.SettleUpUserRequestDto;
import in.mannvender.splitwise.dtos.settle_up.SettleUpUserResponseDto;
import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.services.interfaces.IGroupService;
import in.mannvender.splitwise.services.interfaces.ISettleUpService;
import in.mannvender.splitwise.utils.ExpenseDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/settle-up")
public class SettleUpController {
    @Autowired
    private ISettleUpService settleUpService;
    @Autowired
    private IGroupService groupService;

    @PostMapping("/user")
    public SettleUpUserResponseDto settleUpUser(@RequestBody SettleUpUserRequestDto requestDto){
        if(requestDto.getUserId() == null){
            throw new RuntimeException("User Id cannot be null");
        }
        List<Expense> transactions = settleUpService.settleUpUser(requestDto.getUserId());
        SettleUpUserResponseDto settleUpUserResponseDto = new SettleUpUserResponseDto();
        for(Expense expense: transactions){
            settleUpUserResponseDto.getTransactions().add(ExpenseDtoConverter.getExpenseResponseDto(expense));
        }
        return settleUpUserResponseDto;
    }

    @PostMapping("/group")
    public SettleUpGroupResponseDto settleUpGroup(@RequestBody SettleUpGroupRequestDto requestDto) {
        if(requestDto.getGroupId() == null){
            throw new RuntimeException("Group Id cannot be null");
        }
        List<Expense> transactions = settleUpService.settleUpGroup(requestDto.getGroupId());
        SettleUpGroupResponseDto responseDto = new SettleUpGroupResponseDto();
        responseDto.setGroupId(requestDto.getGroupId());
        for(Expense expense: transactions){
            responseDto.getTransactions().add(ExpenseDtoConverter.getExpenseResponseDto(expense));
        }
        return responseDto;
    }
}
