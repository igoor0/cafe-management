package uni.cafemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uni.simulatedpos.model.Transaction;
import uni.simulatedpos.service.TransactionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/date")
    public List<Transaction> getTransactionsByDate(@RequestParam("date") String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return transactionService.getTransactionsByDate(parsedDate);
    }

    @GetMapping("/date-range")
    public List<Transaction> getTransactionsByDateRange(@RequestParam("startDate") String startDate,
                                                        @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return transactionService.getTransactionsByDateBetween(start, end);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }

}