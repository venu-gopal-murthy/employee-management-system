package com.example.employee_management.Repository;

import com.example.employee_management.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface employeeRepository extends JpaRepository<Employee,Long>{


}
