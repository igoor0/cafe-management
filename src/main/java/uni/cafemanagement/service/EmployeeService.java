package uni.cafemanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uni.cafemanagement.exception.ApiRequestException;
import uni.simulatedpos.model.Employee;
import uni.simulatedpos.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Employee not found with ID: " + id));
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->  new ApiRequestException("Employee not found with ID: " + id));

        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setRole(updatedEmployee.getRole());
        existingEmployee.setPassword(updatedEmployee.getPassword());

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ApiRequestException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
    }
}