package uni.cafemanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.cafemanagement.model.Report;
import uni.cafemanagement.model.ReportType;
import uni.cafemanagement.repository.ReportRepository;
import uni.simulatedpos.model.Transaction;
import uni.simulatedpos.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final TransactionService transactionService;
    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(TransactionService transactionService, ReportRepository reportRepository) {
        this.transactionService = transactionService;
        this.reportRepository = reportRepository;
    }

    public Report generateReport(ReportType type, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionService.getTransactionsByDateBetween(startDate, endDate);

        BigDecimal totalSales = transactions.stream()
                .map(Transaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalTransactions = transactions.size();

        Report report = new Report(type, startDate, endDate, totalTransactions, totalSales);
        return reportRepository.save(report);
    }

    public Report generateDailyReport() {
        LocalDate today = LocalDate.now();
        return generateReport(ReportType.DAILY, today, today);
    }

    public Report generateMonthlyReport() {
        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        return generateReport(ReportType.MONTHLY, startOfMonth, endOfMonth);
    }

    public Report generateYearlyReport() {
        LocalDate startOfYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endOfYear = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        return generateReport(ReportType.YEARLY, startOfYear, endOfYear);
    }

    public Report generateGeneralReport() {
        List<Transaction> transactions = transactionService.getAllTransactions();

        BigDecimal totalSales = transactions.stream()
                .map(Transaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalTransactions = transactions.size();
        LocalDate today = LocalDate.now();
        LocalDate dateOfFirstTransaction = transactions.stream()
                .findFirst()
                .map(Transaction::getDate)
                .orElse(today);
        Report report = new Report(ReportType.GENERAL, dateOfFirstTransaction, today, totalTransactions, totalSales);
        return reportRepository.save(report);
    }

    public Report generateSpecificDateReport(LocalDate date) {
        return generateReport(ReportType.SPECIFIC_DATE, date, date);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}