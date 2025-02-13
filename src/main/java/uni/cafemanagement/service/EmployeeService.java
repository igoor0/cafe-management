package uni.cafemanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import uni.cafemanagement.dto.EmployeeDTO;
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

    public Employee createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmail(employeeDTO.getEmail());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPosition(employeeDTO.getPosition());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, EmployeeDTO updatedEmployeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Employee not found with ID: " + id));

        existingEmployee.setEmail(updatedEmployeeDTO.getEmail());
        existingEmployee.setFirstName(updatedEmployeeDTO.getFirstName());
        existingEmployee.setLastName(updatedEmployeeDTO.getLastName());
        existingEmployee.setPosition(updatedEmployeeDTO.getPosition());
        existingEmployee.setPhoneNumber(updatedEmployeeDTO.getPhoneNumber());

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ApiRequestException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
    }
}