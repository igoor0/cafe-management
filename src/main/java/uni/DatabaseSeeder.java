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

import static uni.cafemanagement.model.InventoryProduct.createCountableInventoryProduct;
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
                new Employee("Lucyna@employee.com", passwordEncoder.encode("password"), "Lucyna", "Wierzbowska", "Kelner", "+48500900900"),
                new Employee("Maryla@employee.com", passwordEncoder.encode("password"), "Maryla", "Markowska", "Barista", "+48500900900"),
                new Employee("Natalia@employee.com", passwordEncoder.encode("password"), "Natalia", "Nacińska", "Barista", "+48500900900"),
                new Employee("Radoslaw@employee.com", passwordEncoder.encode("password"), "Radoslaw", "Tankow", "Head Barista", "+48500900900"),
                new Employee("Franciszek@employee.com", passwordEncoder.encode("password"), "Franciszek", "Menger", "Backbar", "+48500900900")
        );
        employeeRepository.saveAll(employees);
    }

    public void initializeProductCategories() {
        List<ProductCategory> productCategories = List.of(
                new ProductCategory("Coffee"),
                new ProductCategory("Milk"),
                new ProductCategory("Water"),
                new ProductCategory("Syrups"),
                new ProductCategory("Alcohol"),
                new ProductCategory("Takeaway"),
                new ProductCategory("Bakery"),
                new ProductCategory("Other")
        );
        productCategoryRepository.saveAll(productCategories);
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

    public void InitializeProducts() {
        ProductCategory milkCategory = productCategoryRepository.findByName("Milk").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory coffeeCategory = productCategoryRepository.findByName("Coffee").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory waterCategory = productCategoryRepository.findByName("Water").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory syrupsCategory = productCategoryRepository.findByName("Syrups").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory alcoholCategory = productCategoryRepository.findByName("Alcohol").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory takeawayCategory = productCategoryRepository.findByName("Takeaway").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory bakeryCategory = productCategoryRepository.findByName("Bakery").orElseThrow(() -> new ApiRequestException("Category Not Found"));
        ProductCategory otherCategory = productCategoryRepository.findByName("Other").orElseThrow(() -> new ApiRequestException("Category Not Found"));

        InventoryProduct coffeeBeans = createNonCountableInventoryProduct("Ziarna kawy", "Jutowy worek kawy w ziarnach", coffeeCategory, 450, 8000, 1000);

        InventoryProduct milk = createNonCountableInventoryProduct("Mleko", "Mleko krowie 3,2%", milkCategory, 60, 12000, 2000);
        InventoryProduct oatMilk = createNonCountableInventoryProduct("Oatly", "Mleko roślinne owsiane 2,2%", milkCategory, 4.30, 12000, 2000);
        InventoryProduct almondMilk = createNonCountableInventoryProduct("Mleko migdałowe", "Roślinne mleko migdałowe", milkCategory, 5.50, 10000, 1500);
        InventoryProduct coconutMilk = createNonCountableInventoryProduct("Mleko kokosowe", "Mleko roślinne kokosowe", milkCategory, 6.00, 8000, 1500);
        InventoryProduct soyMilk = createNonCountableInventoryProduct("Mleko sojowe", "Mleko roślinne sojowe", milkCategory, 5.50, 9000, 1500);

        InventoryProduct caramelSyrup = createNonCountableInventoryProduct("Syrop karmelowy", "Syrop do kawy i deserów", syrupsCategory, 0.31, 1000, 500);
        InventoryProduct vanillaSyrup = createNonCountableInventoryProduct("Syrop waniliowy", "Syrop o smaku waniliowym", syrupsCategory, 0.35, 1000, 500);
        InventoryProduct chocolateSyrup = createNonCountableInventoryProduct("Syrop czekoladowy", "Syrop czekoladowy do kawy", syrupsCategory, 0.35, 1000, 500);

        InventoryProduct smallTakeawayCup = createCountableInventoryProduct("Kubek na wynos 250ml", "Papierowy kubek na małe kawy", takeawayCategory, 0.50, 500, 50);
        InventoryProduct largeTakeawayCup = createCountableInventoryProduct("Kubek na wynos 400ml", "Papierowy kubek na duże kawy", takeawayCategory, 0.70, 500, 50);
        InventoryProduct mediumTakeawayCup = createCountableInventoryProduct("Średni kubek na wynos 300ml", "Papierowy kubek na średnie kawy", takeawayCategory, 0.5, 500, 100);

        InventoryProduct whiskey = createNonCountableInventoryProduct("Whiskey", "Alkohol do Irish Coffee", alcoholCategory, 100, 3000, 500);
        InventoryProduct amaretto = createCountableInventoryProduct("Amaretto", "Likier migdałowy", alcoholCategory, 90, 2000, 300);

        InventoryProduct cocoaPowder = createNonCountableInventoryProduct("Kakao", "Kakao mielone", otherCategory, 40, 2000, 200);
        InventoryProduct whippedCream = createNonCountableInventoryProduct("Bita śmietana", "Bita Śmietana w aerozolu", otherCategory, 15, 800, 200);


        inventoryProductRepository.saveAll(List.of(
                coffeeBeans, milk, oatMilk, almondMilk,
                caramelSyrup, vanillaSyrup, chocolateSyrup,
                smallTakeawayCup, largeTakeawayCup, whiskey, amaretto
        ));


        MenuProductCategory hotCoffeeCategory = menuProductCategoryRepository.findByName("Kawa na ciepło")
                .orElseThrow(() -> new RuntimeException("Category not found"));
        MenuProductCategory icedCoffeeCategory = menuProductCategoryRepository.findByName("Kawa na zimno")
                .orElseThrow(() -> new RuntimeException("Category not found"));



        MenuProduct smallLatte = new MenuProduct("Latte 250ml", "Espresso + mleko", hotCoffeeCategory, BigDecimal.valueOf(15), 1);
        smallLatte.addIngredient(coffeeBeans, 18.0);
        smallLatte.addIngredient(milk, 250.0);
        smallLatte.addIngredient(smallTakeawayCup, 1.0);

        MenuProduct largeLatte = new MenuProduct("Latte 400ml", "Espresso + mleko", hotCoffeeCategory, BigDecimal.valueOf(18), 1);
        largeLatte.addIngredient(coffeeBeans, 22.0);
        largeLatte.addIngredient(milk, 400.0);
        largeLatte.addIngredient(largeTakeawayCup, 1.0);

        // ☕ Cappuccino
        MenuProduct smallCappuccino = new MenuProduct("Cappuccino 250ml", "Espresso + mleczna pianka", hotCoffeeCategory, BigDecimal.valueOf(14), 1);
        smallCappuccino.addIngredient(coffeeBeans, 18.0);
        smallCappuccino.addIngredient(milk, 150.0);
        smallCappuccino.addIngredient(smallTakeawayCup, 1.0);

        MenuProduct largeCappuccino = new MenuProduct("Cappuccino 400ml", "Espresso + mleczna pianka", hotCoffeeCategory, BigDecimal.valueOf(17), 1);
        largeCappuccino.addIngredient(coffeeBeans, 22.0);
        largeCappuccino.addIngredient(milk, 300.0);
        largeCappuccino.addIngredient(largeTakeawayCup, 1.0);

        // ☕ Irish Coffee (alkoholowa)
        MenuProduct irishCoffee = new MenuProduct("Irish Coffee 250ml", "Kawa z whiskey i bitą śmietaną", hotCoffeeCategory, BigDecimal.valueOf(25), 1);
        irishCoffee.addIngredient(coffeeBeans, 18.0);
        irishCoffee.addIngredient(milk, 100.0);
        irishCoffee.addIngredient(whiskey, 50.0);
        irishCoffee.addIngredient(smallTakeawayCup, 1.0);

        // ☕ Mocha
        MenuProduct mocha = new MenuProduct("Mocha", "Espresso + mleko + czekolada", hotCoffeeCategory, BigDecimal.valueOf(18), 1);
        mocha.addIngredient(coffeeBeans, 18.0);
        mocha.addIngredient(milk, 250.0);
        mocha.addIngredient(cocoaPowder, 10.0);
        mocha.addIngredient(whippedCream, 20.0);
        mocha.addIngredient(mediumTakeawayCup, 1.0);

        // ☕ Flat White
        MenuProduct flatWhite = new MenuProduct("Flat White", "Podwójne espresso + mleko", hotCoffeeCategory, BigDecimal.valueOf(16), 1);
        flatWhite.addIngredient(coffeeBeans, 36.0);
        flatWhite.addIngredient(milk, 250.0);
        flatWhite.addIngredient(mediumTakeawayCup, 1.0);

        // Espresso Tonic
        MenuProduct espressoTonic = new MenuProduct("Espresso Tonic", "Espresso + tonik + lód", icedCoffeeCategory, BigDecimal.valueOf(17), 1);
        espressoTonic.addIngredient(coffeeBeans, 18.0);
//        espressoTonic.addIngredient(tonic, 250.0);
        espressoTonic.addIngredient(mediumTakeawayCup, 1.0);

        MenuProduct matchaLatte = new MenuProduct("Matcha Latte", "Zielona herbata + mleko kokosowe", hotCoffeeCategory, BigDecimal.valueOf(20), 1);
        matchaLatte.addIngredient(coconutMilk, 250.0);
        matchaLatte.addIngredient(milk, 100.0);
        matchaLatte.addIngredient(mediumTakeawayCup, 1.0);


        menuProductRepository.saveAll(List.of(
                smallLatte, largeLatte,
                smallCappuccino, largeCappuccino,
                irishCoffee
        ));
    }
}