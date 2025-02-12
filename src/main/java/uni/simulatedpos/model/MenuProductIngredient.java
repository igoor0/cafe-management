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

    private double quantity;

    public MenuProductIngredient( InventoryProduct inventoryProduct, double quantity) {
        this.inventoryProduct = inventoryProduct;
        this.quantity = quantity;
    }
}