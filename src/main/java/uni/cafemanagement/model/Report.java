package uni.cafemanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    private LocalDate startDate;
    private LocalDate endDate;

    private int totalTransactions;
    private BigDecimal totalSales;

    private LocalDate generatedAt;

    public Report(ReportType reportType, LocalDate startDate, LocalDate endDate, int totalTransactions, BigDecimal totalSales) {
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalTransactions = totalTransactions;
        this.totalSales = totalSales;
        this.generatedAt = LocalDate.now();
    }

}