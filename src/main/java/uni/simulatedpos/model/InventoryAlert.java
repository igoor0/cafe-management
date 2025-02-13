package uni.simulatedpos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uni.cafemanagement.model.InventoryProduct;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class InventoryAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private InventoryProduct ingredient;
    private String message;
    private LocalDateTime timestamp;

    public InventoryAlert(InventoryProduct ingredient, String message, LocalDateTime timestamp) {
        this.ingredient = ingredient;
        this.message = message;
        this.timestamp = timestamp;
    }
}