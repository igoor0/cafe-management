package uni.simulatedpos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uni.cafemanagement.model.InventoryProduct;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MenuProductIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_product_id")
    private MenuProduct menuProduct;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inventory_product_id")
    private InventoryProduct inventoryProduct;

    // Ilość składnika potrzebna na jedną porcję (w gramach lub sztukach)
    private double quantity;

    public MenuProductIngredient(MenuProduct menuProduct, InventoryProduct inventoryProduct, double quantity) {
        this.menuProduct = menuProduct;
        this.inventoryProduct = inventoryProduct;
        this.quantity = quantity;
    }
}