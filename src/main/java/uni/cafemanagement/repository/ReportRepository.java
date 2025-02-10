package uni.cafemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.cafemanagement.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}