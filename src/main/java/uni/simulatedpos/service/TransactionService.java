package uni.simulatedpos.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.model.TransactionReport;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.simulatedpos.model.*;
import uni.simulatedpos.repository.EmployeeRepository;
import uni.simulatedpos.repository.MenuProductRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final EmployeeRepository employeeRepository;
    private final MenuProductRepository menuProductRepository;
    private final InventoryProductRepository inventoryProductRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, EmployeeRepository employeeRepository, MenuProductRepository menuProductRepository, InventoryProductRepository inventoryProductRepository) {
        this.transactionRepository = transactionRepository;
        this.employeeRepository = employeeRepository;
        this.menuProductRepository = menuProductRepository;
        this.inventoryProductRepository = inventoryProductRepository;
    }

    public void processTransaction(Transaction transaction) {
        for (TransactionProduct transactionProduct : transaction.getProducts()) {
            MenuProduct menuProduct = menuProductRepository.findByName(transactionProduct.getName())
                    .orElseThrow(() -> new RuntimeException("MenuProduct not found"));

            for (MenuProductIngredient ingredient : menuProduct.getIngredients()) {
                InventoryProduct inventoryProduct = ingredient.getInventoryProduct();
                int orderedQuantity = transactionProduct.getQuantity();

                if (inventoryProduct.isCountable()) {
                    double newQuantity = inventoryProduct.getQuantity() - (ingredient.getQuantity() * orderedQuantity);
                    if (newQuantity < 0) {
                        throw new RuntimeException("Not enough " + inventoryProduct.getName() + " in stock.");
                    }
                    inventoryProduct.setQuantity(newQuantity);
                } else {
                    double newWeight = inventoryProduct.getWeightInGrams() - (ingredient.getQuantity() * orderedQuantity);
                    if (newWeight < 0) {
                        throw new RuntimeException("Not enough " + inventoryProduct.getName() + " in stock.");
                    }
                    inventoryProduct.setWeightInGrams(newWeight);
                }

                inventoryProductRepository.save(inventoryProduct);
            }
        }

        // Zapisz transakcję
        transactionRepository.save(transaction);
    }

    private boolean isIngredientAvailable(InventoryProduct inventoryProduct, double requiredQuantity) {
        if (inventoryProduct.isCountable()) {
            return inventoryProduct.getQuantity() >= requiredQuantity;
        } else {
            return inventoryProduct.getWeightInGrams() >= requiredQuantity;
        }
    }

    private void deductInventory(InventoryProduct inventoryProduct, double usedQuantity) {
        if (inventoryProduct.isCountable()) {
            inventoryProduct.setQuantity(inventoryProduct.getQuantity() - (int) usedQuantity);
        } else {
            inventoryProduct.setWeightInGrams(inventoryProduct.getWeightInGrams() - usedQuantity);
        }
        inventoryProductRepository.save(inventoryProduct);
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