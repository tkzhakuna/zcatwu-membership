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

import com.sintaks.mushandi.model.Institution;
import com.sintaks.mushandi.service.InstitutionService;
import com.sintaks.mushandi.service.MapValidationErrorService;
import com.sintaks.mushandi.service.MemberService;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class InstitutionController {
@Autowired
private InstitutionService institutionService;
@Autowired
private MapValidationErrorService mapValidationErrorService;
@Autowired
private MemberService memberService;

@GetMapping("/institutions")
public List<Institution>findAll(){
	return institutionService.findAll();
}

@GetMapping("/institutions/{id}")
public ResponseEntity<?>findInstitution(@PathVariable Long id){
	return new ResponseEntity<>(institutionService.findById(id),HttpStatus.OK);
}

@GetMapping("/institutions/members/{institutionId}")
public ResponseEntity<?>findMemberByInstitution(@PathVariable Long institutionId){
	return new ResponseEntity<>(memberService.batchList(institutionId),HttpStatus.OK);
}

@PostMapping("/institutions")
public ResponseEntity<?>addInstitution(@Valid @RequestBody Institution institution, BindingResult result, Principal principal)throws URISyntaxException{
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
	
	return new ResponseEntity<Institution>(institutionService.save(institution,principal),HttpStatus.CREATED);
}
@PutMapping("/institutions/{id}")
public ResponseEntity<?>updateInstitution(@Valid @RequestBody Institution institution,BindingResult result,Principal principal){
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
    
	return new ResponseEntity<Institution>(institutionService.save(institution,principal),HttpStatus.OK);
}
@DeleteMapping("/institutions/{id}")
public void deleteInstitution(@PathVariable Long id) {
	institutionService.deleteById(id);
}
}
