package uni.simulatedpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.simulatedpos.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}