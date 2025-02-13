package uni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uni.cafemanagement.auth.AuthenticationService;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.InventoryProduct;
import uni.cafemanagement.model.ProductCategory;
import uni.cafemanagement.model.Role;
import uni.cafemanagement.model.User;
import uni.cafemanagement.repository.InventoryProductRepository;
import uni.cafemanagement.repository.ProductCategoryRepository;
import uni.cafemanagement.repository.UserRepository;
import uni.simulatedpos.model.*;
import uni.simulatedpos.repository.EmployeeRepository;
import uni.simulatedpos.repository.MenuProductCategoryRepository;
import uni.simulatedpos.repository.MenuProductRepository;
import uni.simulatedpos.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

import static uni.cafemanagement.model.InventoryProduct.createNonCountableInventoryProduct;

@Component
public class DatabaseSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuProductCategoryRepository menuProductCategoryRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public DatabaseSeeder(AuthenticationService authenticationService, UserRepository userRepository, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, MenuProductRepository menuProductRepository, MenuProductCategoryRepository menuProductCategoryRepository, InventoryProductRepository inventoryProductRepository, ProductCategoryRepository productCategoryRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuProductCategoryRepository = menuProductCategoryRepository;
        this.inventoryProductRepository = inventoryProductRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public void initializeAdminAccount() {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin account created successfully.");
        } else {
            System.out.println("Admin account already exists.");
        }
    }

    public void initializeEmployeesAccount() {
        List<Employee> employees = List.of(
                new Employee("Lucyna@employee.com", passwordEncoder.encode("password"), "Lucyna", "Wierzbowska", "Kelner"),
                new Employee("Maryla@employee.com", passwordEncoder.encode("password"), "Maryla", "Markowska", "Barista"),
                new Employee("Natalia@employee.com", passwordEncoder.encode("password"), "Natalia", "Nacińska", "Barista"),
                new Employee("Radoslaw@employee.com", passwordEncoder.encode("password"), "Radoslaw", "Tankow", "Head Barista"),
                new Employee("Franciszek@employee.com", passwordEncoder.encode("password"), "Franciszek", "Menger", "Backbar")
        );
        employeeRepository.saveAll(employees);
    }

    public void initializeProductCategories() {
        List<ProductCategory> productCategories = List.of(
                new ProductCategory("Coffee"),
                new ProductCategory("Milk"),
                new ProductCategory("Water"),
                new ProductCategory("Alcohol"),
                new ProductCategory("Chemical"),
                new ProductCategory("Takeaway"),
                new ProductCategory("Bakery"),
                new ProductCategory("Other")
        );
        productCategoryRepository.saveAll(productCategories);
    }

    public void initializeInventoryProducts() {
        ProductCategory milkCategory = productCategoryRepository.findByName("Milk").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory coffeeCategory = productCategoryRepository.findByName("Coffee").orElseThrow(() -> new ApiRequestException("Category Not Found"));

        InventoryProduct coffeeBeans = createNonCountableInventoryProduct("Ziarna kawy", "Jutowy worek kawy w ziarnach", coffeeCategory, 450, 8000, 1000);
        InventoryProduct milk = createNonCountableInventoryProduct("Mleko", "Mleko krowie 3,2%", milkCategory, 2.5, 12000, 2000);
        InventoryProduct oatMilk = createNonCountableInventoryProduct("Oatly", "Mleko roślinne owsiane 2,2%", milkCategory, 4.30, 12000, 2000);
        inventoryProductRepository.saveAll(List.of(coffeeBeans, milk, oatMilk));
    }

    public void initializeMenuProductCategories() {
        List<MenuProductCategory> categories = List.of(
                new MenuProductCategory("Kawa na ciepło"),
                new MenuProductCategory("Kawa na zimno"),
                new MenuProductCategory("Inne napoje"),
                new MenuProductCategory("Alkohole"),
                new MenuProductCategory("Dodatki"),
                new MenuProductCategory("Jedzenie"),
                new MenuProductCategory("Kawa w ziarnach")
        );
        menuProductCategoryRepository.saveAll(categories);
    }

    public void initializeMenuProducts() {
        MenuProductCategory hotCoffeeCategory = menuProductCategoryRepository.findByName("Kawa na ciepło")
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        MenuProductCategory otherDrinkCategory = menuProductCategoryRepository.findByName("Inne napoje")
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        InventoryProduct coffeeBeans = inventoryProductRepository.findByName("Ziarna kawy")
                .orElseThrow(() -> new IllegalStateException("Inventory product not found"));
        InventoryProduct milk = inventoryProductRepository.findByName("Mleko")
                .orElseThrow(() -> new IllegalStateException("Inventory product not found"));

        MenuProduct americano = new MenuProduct("Kawa Americano 300ml", "Espresso z dodatkiem wrzątku", hotCoffeeCategory, BigDecimal.valueOf(14.0), 1);
        americano.addIngredient(coffeeBeans, 18);
        americano.addIngredient(milk, 250);

        MenuProduct cappuccino = new MenuProduct("Cappuccino 300ml", "Espresso z mlekiem i pianką", hotCoffeeCategory, BigDecimal.valueOf(12.0), 1);
        cappuccino.addIngredient(coffeeBeans, 18);
        cappuccino.addIngredient(milk, 250);

        menuProductRepository.saveAll(List.of(americano, cappuccino));
    }
}