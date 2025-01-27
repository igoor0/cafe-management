package uni.simulatedpos.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uni.simulatedpos.model.Employee;
import uni.simulatedpos.model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByDate(LocalDate date);

    List<Transaction> findByEmployee(Employee employee);

    List<Transaction> findAllByDateBetween(LocalDate start, LocalDate end);
}