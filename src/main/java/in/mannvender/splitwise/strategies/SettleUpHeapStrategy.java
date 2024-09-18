package in.mannvender.splitwise.strategies;

import in.mannvender.splitwise.models.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SettleUpHeapStrategy implements SettleUpStrategy {
    @Override
    public List<Expense> settleUp(List<Expense> expenses) {
        Map<Long, Double> netBalances = getLongDoubleMap(expenses);

        // Min heap to store users who have to receive money
        PriorityQueue<Map.Entry<Long, Double>> minHeap = new PriorityQueue<>(Map.Entry.comparingByValue());
        // Max heap to store users who have to pay
        PriorityQueue<Map.Entry<Long, Double>> maxHeap = new PriorityQueue<>((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // insert values into heaps
        for (Map.Entry<Long, Double> entry : netBalances.entrySet()) {
            if (entry.getValue() < 0) {
                minHeap.offer(entry);
            } else if (entry.getValue() > 0) {
                maxHeap.offer(entry);
            }
        }

        List<Expense> transactions = new ArrayList<>();

        // Settle transactions
        // Payer here means the user who has to pay during settlement
        while (!minHeap.isEmpty() && !maxHeap.isEmpty()) {
            Map.Entry<Long, Double> receiver = minHeap.poll();
            Map.Entry<Long, Double> payer = maxHeap.poll();

            // Create a transaction to settle the amount
            Expense transaction = new Expense();
            transaction.setAmount(Math.min(-receiver.getValue(), payer.getValue()));
            transaction.setDescription("UserId " + payer.getKey() + " has to pay UserId " + receiver.getKey());
            transaction.setExpenseType(ExpenseType.DUMMY);
            // transaction contains expense users
            User receiverUser = new User();
            receiverUser.setId(receiver.getKey());
            User payerUser = new User();
            payerUser.setId(payer.getKey());
            ExpenseUser receiverExpenseUser = new ExpenseUser();
            receiverExpenseUser.setUser(receiverUser);
            receiverExpenseUser.setAmount(transaction.getAmount());
            receiverExpenseUser.setExpenseUserType(ExpenseUserType.HAD_TO_PAY);
            receiverExpenseUser.setExpense(transaction);
            ExpenseUser payerExpenseUser = new ExpenseUser();
            payerExpenseUser.setUser(payerUser);
            payerExpenseUser.setAmount(transaction.getAmount());
            payerExpenseUser.setExpenseUserType(ExpenseUserType.PAID_BY);
            payerExpenseUser.setExpense(transaction);
            transaction.setExpenseUsers(List.of(receiverExpenseUser, payerExpenseUser));
            transactions.add(transaction);


            // put updated balances back in heaps
            if (payer.getValue() + receiver.getValue() < 0) {
                minHeap.offer(new AbstractMap.SimpleEntry<>(receiver.getKey(), payer.getValue() + receiver.getValue()));
            } else if (payer.getValue() + receiver.getValue() > 0) {
                maxHeap.offer(new AbstractMap.SimpleEntry<>(payer.getKey(), payer.getValue() + receiver.getValue()));
            }

        }
        return transactions;
    }

    private static Map<Long, Double> getLongDoubleMap(List<Expense> expenses) {
        Map<Long, Double> netBalances = new HashMap<>();

        // Calculate net balances for each user
        for (Expense expense : expenses) {
            for (ExpenseUser expenseUser : expense.getExpenseUsers()) {
                netBalances.put(expenseUser.getUser().getId(),
                        netBalances.getOrDefault(expenseUser.getUser().getId(), 0.0) + (expenseUser.getExpenseUserType().equals(ExpenseUserType.HAD_TO_PAY) ? expenseUser.getAmount(): -expenseUser.getAmount()));
            }
        }
        return netBalances;
    }
}