package uni.cafemanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.simulatedpos.model.Transaction;
import uni.simulatedpos.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return transactions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/date")
    public ResponseEntity<List<Transaction>> getTransactionsByDate(@RequestParam("date") String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            List<Transaction> transactions = transactionService.getTransactionsByDate(parsedDate);
            return transactions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactions);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<Transaction> transactions = transactionService.getTransactionsByDateBetween(start, end);
            return transactions.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactions);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}