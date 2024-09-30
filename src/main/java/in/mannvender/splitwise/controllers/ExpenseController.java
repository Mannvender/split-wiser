package in.mannvender.splitwise.controllers;

import in.mannvender.splitwise.dtos.expense.AmountUserIdPair;
import in.mannvender.splitwise.dtos.expense.ExpenseRequestDto;
import in.mannvender.splitwise.dtos.expense.ExpenseResponseDto;
import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.models.ExpenseUser;
import in.mannvender.splitwise.models.ExpenseUserType;
import in.mannvender.splitwise.services.interfaces.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return getExpenseResponseDto(expense);
    }

    @GetMapping("/{id}")
    public ExpenseResponseDto getExpenseById(@PathVariable Long id){
        Expense expense = expenseService.getExpenseById(id);
        return getExpenseResponseDto(expense);
    }

    private ExpenseResponseDto getExpenseResponseDto(Expense expense) {
        ExpenseResponseDto responseDto = new ExpenseResponseDto();
        responseDto.setId(expense.getId());
        responseDto.setCreatedBy(expense.getCreatedBy().getId());
        responseDto.setDescription(expense.getDescription());
        responseDto.setAmount(expense.getAmount());
        responseDto.setCreatedAt(expense.getCreatedAt().toString());
        responseDto.setExpenseType(expense.getExpenseType().toString());
        AmountUserIdPair hadToPayUserIds = new AmountUserIdPair();
        for (ExpenseUser hadToPayExpenseUser: expense.getExpenseUsers()) {
            if(hadToPayExpenseUser.getExpenseUserType().equals(ExpenseUserType.HAD_TO_PAY)){
                hadToPayUserIds.setAmount(hadToPayExpenseUser.getAmount());
                hadToPayUserIds.setUserId(hadToPayExpenseUser.getUser().getId());
                responseDto.getHadToPayUserIds().add(hadToPayUserIds);
            }
        }
        AmountUserIdPair paidByUserIds = new AmountUserIdPair();
        for (ExpenseUser paidByExpenseUser: expense.getExpenseUsers()) {
            if(paidByExpenseUser.getExpenseUserType().equals(ExpenseUserType.PAID_BY)){
                paidByUserIds.setAmount(paidByExpenseUser.getAmount());
                paidByUserIds.setUserId(paidByExpenseUser.getUser().getId());
                responseDto.getPaidByUserIds().add(paidByUserIds);
            }
        }

        return responseDto;
    }
}
