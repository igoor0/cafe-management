package dev.cafemanagement.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product {

    @Getter
    @Setter
    @Id
    private Long id;

    private String name;
    private double price;
    private String description;
    private String category;
    private int quantityInStock;
    private boolean isOnSale;
    private int discountPercentage;
    private String imageUrl;

}
