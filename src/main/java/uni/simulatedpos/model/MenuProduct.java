package uni.simulatedpos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uni.cafemanagement.model.InventoryProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MenuProduct {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonManagedReference
    private MenuProductCategory category;
    private BigDecimal price;
    private int quantity;
    @ElementCollection(fetch = FetchType.EAGER)  // Zmieniamy na EAGER loading
    @MapKeyJoinColumn(name = "inventory_product_id")
    @Column(name = "quantity")
    private Map<InventoryProduct, Double> ingredients;

    public MenuProduct(String name, String description, MenuProductCategory category, BigDecimal price, int quantity) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.ingredients = new HashMap<>();
    }

    public void addIngredient(InventoryProduct inventoryProduct, double quantity) {
        this.ingredients.put(inventoryProduct, quantity);
    }

    public BigDecimal calculateIngredientCost() {
        return ingredients.entrySet().stream()
                .map(entry -> BigDecimal.valueOf(entry.getKey().getPricePerUnit() * entry.getValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}