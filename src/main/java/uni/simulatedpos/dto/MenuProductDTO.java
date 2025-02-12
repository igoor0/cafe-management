package uni.simulatedpos.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuProductDTO {
    private String name;
    private String description;
    private Long categoryId;
    private Double price;
    private Integer quantity;
    private List<MenuProductIngredientDTO> ingredients;
}