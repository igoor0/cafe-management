package uni.simulatedpos.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uni.simulatedpos.model.*;
import uni.simulatedpos.repository.EmployeeRepository;
import uni.simulatedpos.repository.MenuProductRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TransactionScheduler {

    private final EmployeeRepository employeeRepository;
    private final MenuProductRepository menuProductRepository;
    private final TransactionRepository transactionRepository;
    private final Random random = new Random();

    @Autowired
    public TransactionScheduler(EmployeeRepository employeeRepository,
                                MenuProductRepository menuProductRepository,
                                TransactionRepository transactionRepository) {
        this.employeeRepository = employeeRepository;
        this.menuProductRepository = menuProductRepository;
        this.transactionRepository = transactionRepository;
    }

    // Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void generateRandomTransaction() {
        List<Employee> employees = employeeRepository.findAll();
        List<MenuProduct> menuProducts = menuProductRepository.findAll();

        if (employees.isEmpty() || menuProducts.isEmpty()) {
            System.out.println("Brak danych do wygenerowania transakcji.");
            return;
        }

        Employee employee = employees.get(random.nextInt(employees.size()));
        int productCount = random.nextInt(3) + 1;  // 1 to 3 products per transaction
        List<TransactionProduct> transactionProducts = new ArrayList<>();
        double totalAmount = 0.0;

        for (int i = 0; i < productCount; i++) {
            MenuProduct product = menuProducts.get(random.nextInt(menuProducts.size()));
            int quantity = random.nextInt(3) + 1;
            totalAmount += product.getPrice() * quantity;

            TransactionProduct transactionProduct = new TransactionProduct(
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    quantity,
                    product.getCategory()
            );
            transactionProducts.add(transactionProduct);
        }

        PaymentMethod paymentMethod = random.nextBoolean() ? PaymentMethod.CASH : PaymentMethod.CARD;

        Transaction transaction = new Transaction(
                employee,
                transactionProducts,
                totalAmount,
                paymentMethod,
                LocalDate.now()
        );

        transactionRepository.save(transaction);
        System.out.println("Nowa transakcja została wygenerowana: " + transaction);
    }
}