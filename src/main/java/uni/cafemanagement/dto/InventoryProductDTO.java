package uni.cafemanagement.dto;

import lombok.Getter;
import lombok.Setter;
import uni.cafemanagement.model.InventoryProduct;

@Getter
@Setter
public class InventoryProductDTO {
    private String name;
    private String description;
    private Long categoryId;
    private double price;
    private boolean isCountable;
    private double quantity;
    private double weightInGrams;
    private double minimalValue;

    public InventoryProductDTO(String name, String description, Long categoryId, double price, boolean isCountable, double quantity, double weightInGrams, double minimalValue) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.isCountable = isCountable;
        this.quantity = quantity;
        this.weightInGrams = weightInGrams;
        this.minimalValue = minimalValue;
    }

    public static InventoryProductDTO fromEntity(InventoryProduct product) {
        return new InventoryProductDTO(
                product.getName(),
                product.getDescription(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getPrice(),
                product.isCountable(),
                product.getQuantity(),
                product.getWeightInGrams(),
                product.getMinimalValue()
        );
    }
}