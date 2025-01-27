package uni.cafemanagement.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionReport {
    private double totalSales;
    private double transactionCount;

    public TransactionReport(double totalSales, double transactionCount) {
        this.totalSales = totalSales;
        this.transactionCount = transactionCount;
    }

}