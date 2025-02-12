package uni.simulatedpos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Employee employee;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transaction_id")
    @JsonManagedReference
    private List<TransactionProduct> products = new ArrayList<>();

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate date;
    private LocalTime time;

    public Transaction(Employee employee, List<TransactionProduct> products, BigDecimal totalAmount, PaymentMethod paymentMethod, LocalDate date) {
        this.employee = employee;
        this.products = products;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }
    public void calculateTotalAmount() {
        totalAmount = products.stream()
                .map(transactionProduct -> transactionProduct.getMenuProduct().getPrice()
                        .multiply(BigDecimal.valueOf(transactionProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}