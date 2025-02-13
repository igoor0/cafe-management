package uni.simulatedpos.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.simulatedpos.model.*;
import uni.simulatedpos.repository.EmployeeRepository;
import uni.simulatedpos.repository.InventoryAlertRepository;
import uni.simulatedpos.repository.MenuProductRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final EmployeeRepository employeeRepository;
    private final MenuProductRepository menuProductRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final InventoryAlertRepository inventoryAlertRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              EmployeeRepository employeeRepository,
                              MenuProductRepository menuProductRepository,
                              InventoryProductRepository inventoryProductRepository, InventoryAlertRepository inventoryAlertRepository) {
        this.transactionRepository = transactionRepository;
        this.employeeRepository = employeeRepository;
        this.menuProductRepository = menuProductRepository;
        this.inventoryProductRepository = inventoryProductRepository;
        this.inventoryAlertRepository = inventoryAlertRepository;
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
                .orElseThrow(() -> new ApiRequestException("Employee not found with ID: " + employeeId));
        return transactionRepository.findByEmployee(employee);
    }

    public List<Transaction> getTransactionsByDate(LocalDate date) {
        return transactionRepository.findAllByDate(date);
    }

    public List<Transaction> getTransactionsByDateBetween(LocalDate start, LocalDate end) {
        return transactionRepository.findAllByDateBetween(start, end);
    }

    @Transactional
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);

        for (TransactionProduct transactionProduct : transaction.getProducts()) {
            MenuProduct menuProduct = transactionProduct.getMenuProduct();
            int quantity = transactionProduct.getQuantity();

            for (Map.Entry<InventoryProduct, Double> entry : menuProduct.getIngredients().entrySet()) {
                InventoryProduct ingredient = entry.getKey();
                double requiredQuantity = entry.getValue() * quantity;

                if (!updateInventoryStock(ingredient, requiredQuantity)) {
                    createInventoryAlert(ingredient, "Brak wystarczającej ilości: " + ingredient.getName());
                }

                inventoryProductRepository.save(ingredient);
            }
        }
    }


    private boolean updateInventoryStock(InventoryProduct ingredient, double requiredQuantity) {
        if (ingredient.isCountable()) {
            if (ingredient.getQuantity() < requiredQuantity) {
                return false; // Brak wystarczającej ilości
            }
            ingredient.setQuantity(ingredient.getQuantity() - requiredQuantity);
        } else {
            if (ingredient.getWeightInGrams() < requiredQuantity) {
                return false; // Brak wystarczającej ilości
            }
            ingredient.setWeightInGrams(ingredient.getWeightInGrams() - requiredQuantity);
        }

        if (ingredient.isLowStock()) {
            createInventoryAlert(ingredient, "⚠️ Niski poziom zapasów: " + ingredient.getName());
        }
        return true;
    }

    // Metoda do tworzenia alertu
    private void createInventoryAlert(InventoryProduct ingredient, String message) {
        // Zapisz alert w systemie, lub wyślij powiadomienie
        InventoryAlert alert = new InventoryAlert();
        alert.setIngredient(ingredient);
        alert.setMessage(message);
        alert.setTimestamp(LocalDateTime.now());
        inventoryAlertRepository.save(alert);
    }
}