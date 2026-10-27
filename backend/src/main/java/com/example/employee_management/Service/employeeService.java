package com.example.employee_management.Service;

import com.example.employee_management.Entity.Employee;
import com.example.employee_management.Repository.employeeRepository;

import com.example.employee_management.exception.EmployeeNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class employeeService {


    private final employeeRepository repo;
    public employeeService(employeeRepository repo){
        this.repo=repo;
    }

    public Employee createEmp(Employee employee){
        return repo.save(employee);
    }


    public Employee getOne(Long id){
        return repo.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));
    }

    public List<Employee> getAll(){
        return repo.findAll();
    }


    public String delete(Long id){

        Employee emp=getOne(id);
        repo.delete(emp);
        return "Deleted Employee "+id+"Successfully";

    }


    public Employee update(Employee employee,Long id){
        Employee existingEmp=getOne(id);
        existingEmp.setFirstName(employee.getFirstName());
        existingEmp.setLastName(employee.getLastName());
        existingEmp.setDesignation(employee.getDesignation());
        existingEmp.setEmail(employee.getEmail());
        existingEmp.setSalary(employee.getSalary());
        existingEmp.setDepartment(employee.getDepartment());
        return repo.save(existingEmp);
    }


}
