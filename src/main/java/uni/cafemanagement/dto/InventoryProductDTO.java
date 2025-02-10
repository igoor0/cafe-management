package uni.cafemanagement.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean isCountable;
    private double quantity;
    private double weightInGrams;
    private Long categoryId;
}