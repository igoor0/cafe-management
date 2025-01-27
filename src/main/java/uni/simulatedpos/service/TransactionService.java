package uni.simulatedpos.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.TransactionReport;
import uni.simulatedpos.model.*;
import uni.simulatedpos.repository.EmployeeRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, EmployeeRepository employeeRepository) {
        this.transactionRepository = transactionRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Transaction not found with ID: " + id));
    }

    public List<Transaction> getTransactionsForEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ApiRequestException("Employee not found"));
        return transactionRepository.findByEmployee(employee);
    }

    public List<Transaction> getTransactionsByDate(LocalDate date) {
        return transactionRepository.findAllByDate(date);
    }

    public List<Transaction> getTransactionsByDateBetween(LocalDate start, LocalDate end) {
        return transactionRepository.findAllByDateBetween(start, end);
    }

    public TransactionReport generateYearlyReport() {
        LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endOfYear = LocalDate.of(LocalDate.now().getYear(), 12, 31);

        List<Transaction> transactions = getTransactionsByDateBetween(startOfYear, endOfYear);

        double totalSales = transactions.stream()
                .mapToDouble(Transaction::getTotalAmount)
                .sum();

        return new TransactionReport(transactions.size(), totalSales);
    }

    public TransactionReport generateMonthlyReport() {
        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<Transaction> transactions = getTransactionsByDateBetween(startOfMonth, endOfMonth);

        double totalSales = transactions.stream()
                .mapToDouble(Transaction::getTotalAmount)
                .sum();

        return new TransactionReport(transactions.size(), totalSales);
    }

    public TransactionReport generateDailyReport() {
        LocalDate today = LocalDate.now();
        List<Transaction> transactions = getTransactionsByDateBetween(today, today);

        double totalSales = transactions.stream()
                .mapToDouble(Transaction::getTotalAmount)
                .sum();

        return new TransactionReport(transactions.size(), totalSales);
    }

    public TransactionReport generateGeneralReport() {
        List<Transaction> transactions = getAllTransactions();
        double totalSales = transactions.stream()
                .mapToDouble(Transaction::getTotalAmount)
                .sum();

        return new TransactionReport(transactions.size(), totalSales);
    }

    public TransactionReport generateSpecificDateReport(LocalDate date) {
        List<Transaction> transactions = getTransactionsByDate(date);
        double totalSales = transactions.stream()
                .mapToDouble(Transaction::getTotalAmount)
                .sum();

        return new TransactionReport(transactions.size(), totalSales);
    }
}