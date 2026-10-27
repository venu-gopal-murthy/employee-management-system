package com.example.employee_management.Service;


import com.example.employee_management.Entity.Employee;
import com.example.employee_management.Repository.employeeRepository;
import com.example.employee_management.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {


    @Mock
    private employeeRepository employeeRepository;
    @InjectMocks
    private employeeService employeeService;
    @Test
    void getAllEmployeeReturnsEmployees(){
        Employee employee=new Employee();
        employee.setId(1L);
        employee.setFirstName("Rahul");
        employee.setLastName("sharma");
        employee.setEmail("rahul@example.com");
        when(employeeRepository.findAll())
                .thenReturn(List.of(employee));
        List <Employee> result=employeeService.getAll();

        assertEquals(1,result.size());
        assertEquals("Rahul",result.getFirst().getFirstName());
        verify(employeeRepository).findAll();   
    }

    @Test
    void getEmployeeByIdReturnsEmployeeEWhenFound(){
        Employee employee =new Employee();
        employee.setId(1L);
        employee.setFirstName("Rahul");
        when (employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        Employee result=employeeService.getOne(1L);
        assertEquals(1L,result.getId());
        assertEquals("Rahul",result.getFirstName());
    }
    @Test
    void getEmployeeByIdThrowWhenMissing(){
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class,()-> employeeService.getOne(999L));
    }

    @Test
    void createEmployeeSavesEmployee(){
        Employee employee =new Employee();
        employee.setFirstName("Rahul");
        employee.setLastName("sharma");
        employee.setEmail("rahul@example.com");
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee result=employeeService.createEmp(employee);
        assertNotNull(result);
        assertEquals("rahul@example.com",result.getEmail());
        verify(employeeRepository).save(employee);
    }
}
