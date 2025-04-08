package com.sintaks.mushandi.controller;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sintaks.mushandi.model.Branch;
import com.sintaks.mushandi.service.BranchService;
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class BranchController {
@Autowired
private BranchService branchService;
@Autowired
private com.sintaks.mushandi.service.MapValidationErrorService mapValidationErrorService;


@GetMapping("/branches")
public List<Branch>findAll(){
	return branchService.findAll();
}

@GetMapping("/branches/{id}")
public ResponseEntity<?>findBranch(@PathVariable Long id){
	return new ResponseEntity<>(branchService.findById(id),HttpStatus.OK);
}

@PostMapping("/branches")
public ResponseEntity<?>addBranch(@Valid @RequestBody Branch branch, BindingResult result, Principal principal)throws URISyntaxException{
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
    
	return new ResponseEntity<Branch>(branchService.save(branch,principal),HttpStatus.CREATED);
}
@PutMapping("/branches/{id}")
public ResponseEntity<?>updateBranch(@Valid @RequestBody Branch branch,BindingResult result, Principal principal){
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
	
	return new ResponseEntity<Branch>(branchService.save(branch,principal),HttpStatus.OK);
}
@DeleteMapping("/branches/{id}")
public void deleteBranch(@PathVariable Long id) {
	branchService.deleteById(id);
}
}
