package uni.simulatedpos.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uni.simulatedpos.model.*;
import uni.simulatedpos.repository.EmployeeRepository;
import uni.simulatedpos.service.TransactionService;
import uni.simulatedpos.repository.MenuProductRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TransactionScheduler {

    private final EmployeeRepository employeeRepository;
    private final MenuProductRepository menuProductRepository;
    private final TransactionService transactionService;
    private final Random random = new Random();

    @Autowired
    public TransactionScheduler(EmployeeRepository employeeRepository,
                                MenuProductRepository menuProductRepository,
                                TransactionService transactionService) {
        this.employeeRepository = employeeRepository;
        this.menuProductRepository = menuProductRepository;
        this.transactionService = transactionService;
    }

    @Scheduled(fixedRate = 80000)
    public void generateRandomTransaction() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isBefore(LocalTime.of(10, 0)) || currentTime.isAfter(LocalTime.of(21, 30))) {
            return;
        }

        int numberOfProducts = random.nextInt(3) + 1;

        List<MenuProduct> menuProducts = menuProductRepository.findAll();
        Transaction transaction = new Transaction();

        List<Employee> employees = employeeRepository.findAll();
        Employee randomEmployee = employees.get(random.nextInt(employees.size()));

        transaction.setEmployee(randomEmployee);

        LocalDate transactionDate = LocalDate.now();
        transaction.setDate(transactionDate);

        LocalTime transactionTime = LocalTime.now();
        transaction.setTime(transactionTime);

        PaymentMethod paymentMethod = random.nextBoolean() ? PaymentMethod.CASH : PaymentMethod.CARD;
        transaction.setPaymentMethod(paymentMethod);

        List<TransactionProduct> transactionProducts = new ArrayList<>();

        for (int i = 0; i < numberOfProducts; i++) {
            MenuProduct menuProduct = menuProducts.get(random.nextInt(menuProducts.size()));
            int quantity = random.nextInt(1) + 1;

            TransactionProduct transactionProduct = new TransactionProduct(menuProduct, quantity);
            transactionProducts.add(transactionProduct);
        }

        transaction.setProducts(transactionProducts);

        transaction.calculateTotalAmount();

        transactionService.createTransaction(transaction);
    }
}