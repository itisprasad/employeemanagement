package com.employee.controller;

import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private UUID employeeId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        employeeId = UUID.randomUUID();
        employee = new Employee(employeeId, "John", "Doe", "john.doe@example.com", "Engineering", 75000.0);
        //employee = new Employee("John", "Doe", "john.doe@example.com", "Engineering", 75000.0);
        //employeeId = employee.getId();
    }

    //  Test Create Employee (POST)
    @Test
    void shouldCreateEmployee() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(jsonPath("$.department", is(employee.getDepartment())))
                .andExpect(jsonPath("$.salary", is(employee.getSalary())));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    //  Test Get Employee by ID (GET)
    @Test
    void shouldReturnEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(eq(employeeId))).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employeeId.toString())))
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    //  Test Update Employee (PUT)
    @Test
    void shouldUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(employee);

        mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.department", is(employee.getDepartment())));

        verify(employeeService, times(1)).updateEmployee(eq(employeeId), any(Employee.class));
    }

    // Test Delete Employee (DELETE)
    @Test
    void shouldDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(employeeId);

        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

    //  Test Invalid Employee Creation (Missing Fields)
    @Test
    void shouldReturnBadRequestWhenInvalidEmployee() throws Exception {
        Employee invalidEmployee = new Employee("", "", "invalid-email", "", 0.0);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest());
    }
}

