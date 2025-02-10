package uni.simulatedpos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private MenuProductCategory category;
    private BigDecimal price;
    private int quantity;
    @OneToMany(mappedBy = "menuProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProductIngredient> ingredients;

    public MenuProduct(String name, String description, MenuProductCategory category, BigDecimal price, int quantity) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
}