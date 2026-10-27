package com.example.employee_management.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="first name is required")
    @Column(nullable=false,name="first_name")
    private String firstName;

    @NotBlank(message="last name is required")
    @Column(nullable=false,name="last_name")
    private String lastName;

    @NotBlank(message="Email is required")
    @Email(message="Enter a valid email")
    @Column(nullable = false,unique=true)
    private String email;

    private String department;

    private String designation;

    @PositiveOrZero(message=" salary cannot be negative")
    private int salary;

}
