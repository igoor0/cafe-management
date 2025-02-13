package uni.cafemanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uni.cafemanagement.exception.ApiRequestException;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InventoryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private ProductCategory category;
    private double price;
    private boolean isCountable;
    private double quantity;
    private double weightInGrams;
    private double minimalValue;

    public InventoryProduct(String name, String description, ProductCategory category, double price, double weightInGrams, double minimalValue) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.isCountable = false;
        this.weightInGrams = weightInGrams;
        this.minimalValue = minimalValue;
    }

    public InventoryProduct(String name, String description, ProductCategory category, double price, int quantity, double minimalValue) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.isCountable = true;
        this.quantity = quantity;
        this.minimalValue = minimalValue;
    }

    public static InventoryProduct createCountableInventoryProduct(String name, String description, ProductCategory category, double price, int quantity, double minimalValue) {
        if (quantity < 0) {
            throw new ApiRequestException("Produkt policzalny musi mieć nieujemną ilość sztuk!");
        }
        return new InventoryProduct(name, description, category, price, quantity, minimalValue);
    }

    public static InventoryProduct createNonCountableInventoryProduct(String name, String description, ProductCategory category, double price, double weightInGrams, double minimalValue) {
        if (weightInGrams < 0) {
            throw new ApiRequestException("Produkt niepoliczalny musi mieć nieujemną wagę!");
        }
        return new InventoryProduct(name, description, category, price, weightInGrams, minimalValue);
    }

    public boolean isLowStock() {
        if (isCountable) {
            return quantity <= minimalValue;
        } else {
            return weightInGrams <= minimalValue;
        }
    }
}