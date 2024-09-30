package in.mannvender.splitwise.utils;

import in.mannvender.splitwise.dtos.expense.AmountUserIdPair;
import in.mannvender.splitwise.dtos.expense.ExpenseResponseDto;
import in.mannvender.splitwise.models.Expense;
import in.mannvender.splitwise.models.ExpenseUser;
import in.mannvender.splitwise.models.ExpenseUserType;

public class ExpenseDtoConverter {

    public static ExpenseResponseDto getExpenseResponseDto(Expense expense) {
        ExpenseResponseDto responseDto = new ExpenseResponseDto();
        responseDto.setId(expense.getId());
        if(expense.getCreatedBy() != null){
            responseDto.setCreatedBy(expense.getCreatedBy().getId());
        }

        responseDto.setDescription(expense.getDescription());
        responseDto.setAmount(expense.getAmount());
        if(expense.getCreatedAt() != null){
            responseDto.setCreatedAt(expense.getCreatedAt().toString());
        }
        responseDto.setExpenseType(expense.getExpenseType().toString());
        if (expense.getGroup() != null) {
            responseDto.setGroupId(expense.getGroup().getId());
        }
        AmountUserIdPair hadToPayUserIds = new AmountUserIdPair();
        for (ExpenseUser hadToPayExpenseUser : expense.getExpenseUsers()) {
            if (hadToPayExpenseUser.getExpenseUserType().equals(ExpenseUserType.HAD_TO_PAY)) {
                hadToPayUserIds.setAmount(hadToPayExpenseUser.getAmount());
                hadToPayUserIds.setUserId(hadToPayExpenseUser.getUser().getId());
                responseDto.getHadToPayUserIds().add(hadToPayUserIds);
            }
        }
        AmountUserIdPair paidByUserIds = new AmountUserIdPair();
        for (ExpenseUser paidByExpenseUser : expense.getExpenseUsers()) {
            if (paidByExpenseUser.getExpenseUserType().equals(ExpenseUserType.PAID_BY)) {
                paidByUserIds.setAmount(paidByExpenseUser.getAmount());
                paidByUserIds.setUserId(paidByExpenseUser.getUser().getId());
                responseDto.getPaidByUserIds().add(paidByUserIds);
            }
        }

        return responseDto;
    }
}
