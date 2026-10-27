package com.example.employee_management.Controller;


import com.example.employee_management.Entity.Employee;
import com.example.employee_management.Service.employeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

//import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins={
    "http://localhost:5173",
    "http://localhost:5174"
})
public class EmployeeController {


    private final employeeService employeeService;
    public EmployeeController(employeeService employeeService){
        this.employeeService=employeeService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@Valid @RequestBody Employee employee){
        return employeeService.createEmp(employee);
    }

    @GetMapping("/{id}")
        public Employee getOne(@PathVariable Long id){
            return employeeService.getOne(id);
        }

    @GetMapping
        public List<Employee> getAll(){
            return employeeService.getAll();
        }

    @PutMapping("/{id}")
    public Employee update(@Valid @RequestBody Employee employee,@PathVariable Long id){
        return employeeService.update(employee,id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String delete(@PathVariable Long id){
        return employeeService.delete(id);
    }
}
