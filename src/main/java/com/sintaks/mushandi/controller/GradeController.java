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

import com.sintaks.mushandi.model.Grade;
import com.sintaks.mushandi.service.GradeService;
import com.sintaks.mushandi.service.MapValidationErrorService;
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class GradeController {
@Autowired
private GradeService gradeService;
@Autowired
private MapValidationErrorService mapValidationErrorService;

@GetMapping("/grades")
public List<Grade>findAll(){
	return gradeService.findAll();
}

@GetMapping("/grades/{id}")
public ResponseEntity<?>findGrade(@PathVariable Long id){
	return new ResponseEntity<>(gradeService.findById(id),HttpStatus.OK);
}

@PostMapping("/grades")
public ResponseEntity<?>addGrade(@Valid @RequestBody Grade grade, BindingResult result, Principal principal)throws URISyntaxException{
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
	
	return new ResponseEntity<Grade>(gradeService.save(grade,principal),HttpStatus.CREATED);
}
@PutMapping("/grades/{id}")
public ResponseEntity<?>updateGrade(@Valid @RequestBody Grade grade,BindingResult result,Principal principal){
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
    
	return new ResponseEntity<Grade>(gradeService.save(grade,principal),HttpStatus.OK);
}
@DeleteMapping("/grades/{id}")
public void deleteGrade(@PathVariable Long id) {
	gradeService.deleteById(id);
}
}
