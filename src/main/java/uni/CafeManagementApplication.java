package uni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CafeManagementApplication {

    private final DatabaseSeeder databaseSeeder;

    @Autowired
    public CafeManagementApplication(DatabaseSeeder databaseSeeder) {
        this.databaseSeeder = databaseSeeder;
    }

    public static void main(String[] args) {
        SpringApplication.run(CafeManagementApplication.class, args);

    }
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            databaseSeeder.initializeAdminAccount();
            databaseSeeder.initializeEmployeesAccount();
            databaseSeeder.initializeProductCategories();
            databaseSeeder.initializeInventoryProducts();
            databaseSeeder.initializeMenuProductCategories();
            databaseSeeder.initializeMenuProducts();
            databaseSeeder.initializeSampleTransactions();
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("POST", "GET", "PUT", "DELETE").allowedOrigins("*");
            }
        };
    }



}