package com.employee.service;

import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employee.exception.DatabaseException;
import com.employee.exception.EmployeeNotFoundException;
import com.employee.exception.InvalidInputException;
import com.employee.exception.DatabaseException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Create Employee
    public Employee createEmployee(Employee employee) {

        if (employee.getFirstName() == null || employee.getEmail() == null) {
            throw new InvalidInputException("First name and email cannot be null");
        }

        try {
            return employeeRepository.save(employee);
        } catch (Exception e) {
            throw new DatabaseException("Error saving employee to the database");
        }

        //return employeeRepository.save(employee);
    }

    // Get All Employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get Employee by ID
    public Optional <Employee> getEmployeeById(UUID id) {
      //return employeeRepository.findById(id)
      //          .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
      return employeeRepository.findById(id);
    }

    // Update Employee
    public Employee updateEmployee(UUID id, Employee employeeDetails) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setEmail(employeeDetails.getEmail());
            employee.setDepartment(employeeDetails.getDepartment());
            employee.setSalary(employeeDetails.getSalary());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    // Delete Employee
    public void deleteEmployee(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found");
        }
        try {
            employeeRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Error deleting employee from the database");
        }

        //employeeRepository.deleteById(id);
    }
}

