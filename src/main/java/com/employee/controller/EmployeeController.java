package com.employee.controller;


import com.employee.model.Employee;
import jakarta.validation.Valid;  
import com.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


    // Create Employee
    @PostMapping
   // public Employee createEmployee(@RequestBody Employee employee) {
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        logger.info("Received request to create employee: {}", employee);
        //return employeeService.createEmployee(employee);
        Employee savedEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        //Employee savedEmployee = employeeService.createEmployee(employee);
        //return ResponseEntity.ok(savedEmployee);
    }

    // Get All Employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        logger.info("Received request to get all employees.");
        return employeeService.getAllEmployees();
        //return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable UUID id) {
        logger.info("Received request to get employee with ID: {}", id);
        return employeeService.getEmployeeById(id);
    }

    // Update Employee
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable UUID id, @RequestBody Employee employeeDetails) {
        logger.info("Received request to update employee with ID: {}", id);
        return employeeService.updateEmployee(id, employeeDetails);
        //Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        //return ResponseEntity.ok(updatedEmployee);
    }

    // Delete Employee
    @DeleteMapping("/{id}")
   // public void deleteEmployee(@PathVariable UUID id) {
    public ResponseEntity<String> deleteEmployee(@PathVariable UUID id) {
        logger.info("Received request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

