package uni.simulatedpos.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.InventoryProduct;
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
    public TransactionService(TransactionRepository transactionRepository,
                              EmployeeRepository employeeRepository,
                              MenuProductRepository menuProductRepository,
                              InventoryProductRepository inventoryProductRepository) {
        this.transactionRepository = transactionRepository;
        this.employeeRepository = employeeRepository;
        this.menuProductRepository = menuProductRepository;
        this.inventoryProductRepository = inventoryProductRepository;
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
    public Transaction createTransaction(Transaction transaction) {
        for (TransactionProduct transactionProduct : transaction.getProducts()) {
            MenuProduct menuProduct = menuProductRepository.findById(transactionProduct.getMenuProduct().getId())
                    .orElseThrow(() -> new ApiRequestException("Menu product not found with ID: " + transactionProduct.getMenuProduct().getId()));

            for (MenuProductIngredient ingredient : menuProduct.getIngredients()) {
                InventoryProduct inventoryProduct = inventoryProductRepository.findById(ingredient.getInventoryProduct().getId())
                        .orElseThrow(() -> new ApiRequestException("Inventory product not found with ID: " + ingredient.getInventoryProduct().getId()));

                double requiredQuantity = ingredient.getQuantity() * transactionProduct.getQuantity();

                if ((inventoryProduct.isCountable() && inventoryProduct.getQuantity() < requiredQuantity) ||
                        (!inventoryProduct.isCountable() && inventoryProduct.getWeightInGrams() < requiredQuantity)) {
                    throw new ApiRequestException("Not enough ingredients for product: " + inventoryProduct.getName());
                }

                if (inventoryProduct.isCountable()) {
                    inventoryProduct.setQuantity(inventoryProduct.getQuantity() - requiredQuantity);
                } else {
                    inventoryProduct.setWeightInGrams(inventoryProduct.getWeightInGrams() - requiredQuantity);
                }

                inventoryProductRepository.save(inventoryProduct);
            }
        }
        transaction.calculateTotalAmount();

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Transaction not found with ID: " + id));
        transactionRepository.delete(transaction);
    }
}