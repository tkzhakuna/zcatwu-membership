package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.exceptions.NotSavedException;
import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.model.Employee;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.EmployeeRepository;
import com.sintaks.mushandi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@Service
public class EmployeeServiceImpl implements EmployeeService {
    public final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository=userRepository;
    }

    @Override
    public Employee save(Employee employee, Principal principal) {
        Employee savedEmployee=null;
        User user=userRepository.findByUsername(principal.getName());

        if(employee!=null){
            try {
                employee.setTradeUnion(user.getEmployee().getTradeUnion());
                savedEmployee= employeeRepository.save(employee);
            }catch (Exception ex){
                throw new UnexpectedException("Error saving employee: "+ex.getLocalizedMessage());
            }
        }
        if(savedEmployee!=null) {
            return savedEmployee;
        }else{
            throw new NotSavedException("Employee not saved");
        }
    }

    @Override
    public Employee saveNew(Employee employee) {
        return null;
    }

    @Override
    public Employee update(Employee employee) {
        Boolean bool=false;
        try{
            bool=employeeRepository.existsById(employee.getId());
        }catch (Exception ex){
            throw new UnexpectedException("Employee not found: "+ex.getLocalizedMessage());
        }

        if(bool){
            return employeeRepository.save(employee);
        }else{
            throw new NotSavedException("Employee details not saved");
        }

    }

    @Override
    public List<Employee> findAll() {
        List<Employee>employees;
        try{
            employees=employeeRepository.findAll();
        }catch (Exception ex){
            throw new UnexpectedException("Error retrieving employees: "+ex.getLocalizedMessage());
        }

        if(employees!=null){
            return employees;
        }else{
            throw new NotFoundException("No employees found");
        }

    }

    @Override
    public Optional<Employee> findById(Long id) {
        Optional<Employee>employee;
        try{
            employee=employeeRepository.findById(id);
        }catch (Exception ex){
            throw new UnexpectedException("Error fetching employee details: "+ex.getLocalizedMessage());
        }
        if(employee.isPresent()){
            return employee;
        }else{
            throw new NotFoundException("Employee not found");
        }

    }

    @Override
    public Boolean deleteById(Long id) {
        Boolean bool;
        try{
            bool=employeeRepository.existsById(id);
        }catch(Exception ex){
            throw new UnexpectedException("Error checking employee for deleting: "+ex.getLocalizedMessage());
        }

        if(bool){
            try {
                employeeRepository.deleteById(id);
            }catch(Exception ex){
                throw new UnexpectedException("Failed to delete employee: "+ex.getLocalizedMessage());
            }
        }else{
            throw new NotFoundException("Employee not found for deleting");
        }
        return null;
    }
}
