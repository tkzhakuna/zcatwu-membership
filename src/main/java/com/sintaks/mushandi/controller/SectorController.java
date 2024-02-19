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

import com.sintaks.mushandi.model.Sector;
import com.sintaks.mushandi.service.MapValidationErrorService;
import com.sintaks.mushandi.service.SectorService;
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class SectorController {
@Autowired
private SectorService sectorService;
@Autowired
private MapValidationErrorService mapValidationErrorService;

@GetMapping("/sectors")
public List<Sector>findAll(){
	return sectorService.findAll();
}

@GetMapping("/sectors/{id}")
public ResponseEntity<?>findSector(@PathVariable Long id){
	return new ResponseEntity<>(sectorService.findById(id),HttpStatus.OK);
}

@PostMapping("/sectors")
public ResponseEntity<?> addSector(@Valid @RequestBody Sector sector,BindingResult result,Principal principal)throws URISyntaxException{
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
	
	return new ResponseEntity<Sector>(sectorService.save(sector,principal),HttpStatus.CREATED);
}
@PutMapping("/sectors/{id}")
public ResponseEntity<?>updateSector(@Valid @RequestBody Sector sector, BindingResult result, Principal principal){
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
	
	return new ResponseEntity<Sector>(sectorService.save(sector,principal),HttpStatus.OK);
}
@DeleteMapping("/sectors/{id}")
public void deleteSector(@PathVariable Long id) {
	sectorService.deleteById(id);
}
}
