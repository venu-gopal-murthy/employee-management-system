package com.example.employee_management.Controller;

import com.example.employee_management.Entity.Employee;
//import com.example.employee_management.Service.EmployeeService;
import com.example.employee_management.Service.employeeService;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.exception.GlobalExceptionHandler;

//import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

    @WebMvcTest(EmployeeController.class)
    @Import(GlobalExceptionHandler.class)
    class EmployeeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private employeeService employeeService;

        private Employee sampleEmployee() {
            Employee employee = new Employee();
            employee.setId(1L);
            employee.setFirstName("Rahul");
            employee.setLastName("Sharma");
            employee.setEmail("rahul@example.com");
            employee.setDepartment("IT");
            employee.setDesignation("Developer");
            employee.setSalary(45000);
            return employee;
        }

        private String validEmployeeJson() throws Exception {
            return objectMapper.writeValueAsString(sampleEmployee());
        }

        @Test
        void getAllEmployeesReturns200() throws Exception {
            when(employeeService.getAll())
                    .thenReturn(List.of(sampleEmployee()));

            mockMvc.perform(get("/api/employees"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].firstName").value("Rahul"))
                    .andExpect(jsonPath("$[0].email")
                            .value("rahul@example.com"));
        }

        @Test
        void getEmployeeByIdReturns200() throws Exception {
            when(employeeService.getOne(1L))
                    .thenReturn(sampleEmployee());

            mockMvc.perform(get("/api/employees/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.lastName").value("Sharma"));
        }

        @Test
        void createEmployeeReturns201() throws Exception {
            Employee employee = sampleEmployee();
            when(employeeService.createEmp(any(Employee.class)))
                    .thenReturn(employee);

            mockMvc.perform(post("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validEmployeeJson()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.email")
                            .value("rahul@example.com"));
        }
        @Test
        void updateEmployeeReturns200() throws Exception {
            when(employeeService.update(
                    any(Employee.class),eq(1L)))
                    .thenReturn(sampleEmployee());

            mockMvc.perform(put("/api/employees/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(validEmployeeJson()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.firstName").value("Rahul"));
        }

        @Test
        void deleteEmployeeReturns204() throws Exception {
            //doNothing().when(employeeService).delete(1L);

            mockMvc.perform(delete("/api/employees/1"))
                    .andExpect(status().isNoContent());

            verify(employeeService).delete(1L);
        }

        @Test
        void missingEmailReturns400() throws Exception {
            String invalidJson = """
                {
                  "firstName": "Rahul",
                  "lastName": "Sharma",
                  "department": "IT",
                  "designation": "Developer",
                  "salary": 45000
                }
                """;

            mockMvc.perform(post("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message")
                            .value(org.hamcrest.Matchers.containsString(
                                    "Email is required")));
        }

        @Test
        void nonexistentEmployeeReturns404() throws Exception {
            when(employeeService.getOne(999L))
                    .thenThrow(new EmployeeNotFoundException(999L));

            mockMvc.perform(get("/api/employees/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message")
                            .value("Employee not found with id: 999"));
        }

        @Test
        void malformedJsonReturns400() throws Exception {
            mockMvc.perform(post("/api/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{ invalid json }"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message")
                            .value(org.hamcrest.Matchers.containsString(
                                    "Invalid JSON")));
        }
    }



