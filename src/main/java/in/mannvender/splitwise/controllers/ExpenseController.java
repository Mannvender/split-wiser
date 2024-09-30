package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.dtos.expense.AmountUserIdPair;
import in.mannvender.splitwise.dtos.expense.ExpenseRequestDto;
import in.mannvender.splitwise.dtos.expense.ExpenseResponseDto;
import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.services.interfaces.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    @Autowired
    private IExpenseService expenseService;

    @PostMapping
    public ExpenseResponseDto createExpense(@RequestBody ExpenseRequestDto requestDto){
        if(requestDto == null){
            throw new RuntimeException("Expense cannot be null");
        }
        if(requestDto.getAmount() <= 0){
            throw new RuntimeException("Amount cannot be less than or equal to 0");
        }
        if(requestDto.getHadToPayUserIds() == null || requestDto.getHadToPayUserIds().isEmpty()){
            throw new RuntimeException("Had to pay user ids cannot be null or empty");
        }
        if(requestDto.getPaidByUserIds() == null || requestDto.getPaidByUserIds().isEmpty()){
            throw new RuntimeException("Paid by user ids cannot be null or empty");
        }
        // sum of amounts in paidByUserIds should be equal to amount
        if(requestDto.getPaidByUserIds().stream().mapToDouble(AmountUserIdPair::getAmount).sum() != requestDto.getAmount()){
            throw new RuntimeException("Sum of amounts in paid by user ids should be equal to amount");
        }
        // sum of amounts in hadToPayUserIds should be equal to amount
        if(requestDto.getHadToPayUserIds().stream().mapToDouble(AmountUserIdPair::getAmount).sum() != requestDto.getAmount()){
            throw new RuntimeException("Sum of amounts in had to pay user ids should be equal to amount");
        }

        // call service
        Expense expense = expenseService.createExpense(requestDto.getDescription(), requestDto.getAmount(), requestDto.isSettlement(), requestDto.getGroupId(), requestDto.getCreatedByUserId(), requestDto.getPaidByUserIds(), requestDto.getHadToPayUserIds());
        ExpenseResponseDto responseDto = new ExpenseResponseDto();
        responseDto.setId(expense.getId());
        responseDto.setCreatedBy(expense.getCreatedBy().getId().toString());
        responseDto.setDescription(expense.getDescription());
        responseDto.setAmount(expense.getAmount());
        responseDto.setCreatedAt(expense.getCreatedAt().toString());

        return responseDto;
    }
}
