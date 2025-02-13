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
    private double pricePerUnit;
    private boolean isCountable;
    private double quantity;
    private double weightInGrams;
    private double minimalValue;

    private InventoryProduct(String name, String description, ProductCategory category, double price, double pricePerUnit, double weightInGrams, double minimalValue) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.pricePerUnit = pricePerUnit;
        this.isCountable = false;
        this.weightInGrams = weightInGrams;
        this.minimalValue = minimalValue;
    }

    private InventoryProduct(String name, String description, ProductCategory category, double price, double pricePerUnit, int quantity, double minimalValue) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.pricePerUnit = pricePerUnit;
        this.isCountable = true;
        this.quantity = quantity;
        this.minimalValue = minimalValue;
    }

    public static InventoryProduct createCountableInventoryProduct(String name, String description, ProductCategory category, double price, int quantity, double minimalValue) {
        if (quantity <= 0) {
            throw new ApiRequestException("Produkt policzalny musi mieć ilość większą niż 0!");
        }
        double pricePerUnit = price / quantity;
        return new InventoryProduct(name, description, category, price, pricePerUnit, quantity, minimalValue);
    }

    public static InventoryProduct createNonCountableInventoryProduct(String name, String description, ProductCategory category, double price, double weightInGrams, double minimalValue) {
        if (weightInGrams <= 0) {
            throw new ApiRequestException("Produkt niepoliczalny musi mieć wagę większą niż 0!");
        }
        double pricePerUnit = price / weightInGrams;
        return new InventoryProduct(name, description, category, price, pricePerUnit, weightInGrams, minimalValue);
    }

    public boolean isLowStock() {
        if (isCountable) {
            return quantity <= minimalValue;
        } else {
            return weightInGrams <= minimalValue;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}