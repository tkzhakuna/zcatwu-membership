package com.sintaks.mushandi.controller;

import com.sintaks.mushandi.model.Employee;
import com.sintaks.mushandi.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add-new")
    ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee, Principal principal){
        return new ResponseEntity<>(employeeService.save(employee,principal), HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<Employee>updateEmployee(@RequestBody Employee employee){
        return new ResponseEntity<>(employeeService.update(employee), HttpStatus.OK);

    }

    @GetMapping("/all")
    ResponseEntity<List<Employee>>findAll(){
        return new ResponseEntity<>(employeeService.findAll(),HttpStatus.OK);
    }
    @GetMapping("/find-by-id/{id}")
    ResponseEntity<Employee>findById(@PathVariable Long id){
        return new ResponseEntity<>(employeeService.findById(id).get(),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Object>deleteEmployee(@PathVariable Long id){
        return new ResponseEntity<>(employeeService.deleteById(id),HttpStatus.OK);
    }
}
