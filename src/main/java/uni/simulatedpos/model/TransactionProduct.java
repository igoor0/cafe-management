package uni.simulatedpos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TransactionProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_product_id")
    private MenuProduct menuProduct;

    private int quantity;
    private BigDecimal price;
    private BigDecimal ingredientCostAtPurchase;
    @ManyToOne
    @JsonBackReference
    private Transaction transaction;

    public TransactionProduct(MenuProduct menuProduct, int quantity) {
        this.menuProduct = menuProduct;
        this.quantity = quantity;
        this.price = menuProduct.getPrice();
    }

}