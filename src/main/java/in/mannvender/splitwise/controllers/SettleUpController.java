package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.dtos.settle_up.SettleUpGroupRequestDto;
import in.mannvender.splitwise.dtos.settle_up.SettleUpGroupResponseDto;
import in.mannvender.splitwise.dtos.settle_up.SettleUpUserRequestDto;
import in.mannvender.splitwise.dtos.settle_up.SettleUpUserResponseDto;
import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.services.interfaces.ISettleUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/settle-up")
public class SettleUpController {
    @Autowired
    private ISettleUpService settleUpService;

    @PostMapping("/user/{userId}")
    public SettleUpUserResponseDto settleUpUser(@PathVariable("userId") Long userId, @RequestBody SettleUpUserRequestDto requestDto){
        if(requestDto.getUserId() == null){
            throw new RuntimeException("User Id cannot be null");
        }
        List<Expense> transactions = settleUpService.settleUpUser(requestDto.getUserId());
        SettleUpUserResponseDto responseDto = new SettleUpUserResponseDto();
        responseDto.setTransactions(transactions);
        return responseDto;
    }

    public SettleUpGroupResponseDto settleUpGroup(SettleUpGroupRequestDto requestDto) {
        return null;
    }

}
