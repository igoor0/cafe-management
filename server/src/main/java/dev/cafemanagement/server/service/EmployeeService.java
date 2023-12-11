package dev.cafemanagement.server.service;

import dev.cafemanagement.server.model.Employee;
import dev.cafemanagement.server.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }
    public Employee getEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(employeeId);
        if(existingEmployee != null) {
            existingEmployee.setEmployeeId(updatedEmployee.getEmployeeId());
            existingEmployee.setRoles(updatedEmployee.getRoles());
            existingEmployee.setUsername(updatedEmployee.getUsername());
            existingEmployee.setPassword(updatedEmployee.getPassword());
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
            existingEmployee.setLastName(updatedEmployee.getLastName());
            return employeeRepository.save(existingEmployee);
        } else {
            return null;
        }
    }

    public List<Employee> getAllEmployees() { return employeeRepository.findAll();
    }
}
