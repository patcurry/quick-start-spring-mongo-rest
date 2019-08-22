package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck() {
        return "{ \"isWorking\" : true }";
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        List<Employee> employeesList = employeeRepository.findAll();
        return employeesList;
    }

    @GetMapping("/employees/{id}")
    public Optional<Employee> getEmployee(@PathVariable String id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee;
    }

    @PutMapping("/employees/{id}")
    public Optional<Employee> updateEmployee(@RequestBody Employee newEmployee, @PathVariable String id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setFirstName(newEmployee.getFirstName());
            employee.setLastName(newEmployee.getLastName());
            employee.setEmail(newEmployee.getEmail());
            employeeRepository.save(employee);
        }
        return optionalEmployee;
    }

    @DeleteMapping(value = "/employees/{id}", produces = "application/json; charset=utf-8")
    public String deleteEmployee(@PathVariable String id) {
        Boolean result = employeeRepository.existsById(id);
        employeeRepository.deleteById(id);
        return "{ \"success\" : " + (result ? "true" : "false") + " }";
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee newEmployee) {
        String id = String.valueOf(new Random().nextInt());
        Employee emp = new Employee(id, newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getEmail());
        employeeRepository.insert(emp);
        return emp;
    }
}