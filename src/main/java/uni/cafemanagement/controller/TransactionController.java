package uni.cafemanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.cafemanagement.model.TransactionReport;
import uni.simulatedpos.model.Transaction;
import uni.simulatedpos.service.TransactionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/generate-report")
    public ResponseEntity<TransactionReport> getGeneralTransactionReport() {
        return ResponseEntity.ok(transactionService.generateGeneralReport());
    }

    @GetMapping("/generate-report/daily")
    public ResponseEntity<TransactionReport> getDailyTransactionReport() {
        return ResponseEntity.ok(transactionService.generateDailyReport());
    }

    @GetMapping("/generate-report/monthly")
    public ResponseEntity<TransactionReport> getMonthlyTransactionReport() {
        return ResponseEntity.ok(transactionService.generateMonthlyReport());
    }

    @GetMapping("/generate-report/yearly")
    public ResponseEntity<TransactionReport> getYearlyTransactionReport() {
        return ResponseEntity.ok(transactionService.generateYearlyReport());
    }

    @GetMapping("/generate-report/{date}")
    public ResponseEntity<TransactionReport> getSpecificDateTransactionReport(@PathVariable LocalDate date) {
        return ResponseEntity.ok(transactionService.generateSpecificDateReport(date));
    }


}