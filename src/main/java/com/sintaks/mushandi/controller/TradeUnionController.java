package com.sintaks.mushandi.controller;

import java.net.URISyntaxException;


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
import com.sintaks.mushandi.model.TradeUnion;
import com.sintaks.mushandi.service.MapValidationErrorService;
import com.sintaks.mushandi.service.TradeUnionService;
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class TradeUnionController {
@Autowired
private TradeUnionService tradeUnionService;
@Autowired
private MapValidationErrorService mapValidationErrorService;

@GetMapping("/trade-unions")
public ResponseEntity<?>findAll(){
	return new ResponseEntity<>(tradeUnionService.findAll(),HttpStatus.OK);
}

@GetMapping("/trade-unions/{id}")
public ResponseEntity<?>findSetup(@PathVariable Long id){
	return new ResponseEntity<>(tradeUnionService.findById(id),HttpStatus.OK);
}

@PostMapping("/trade-unions")
public ResponseEntity<?>addSetup(@Valid @RequestBody TradeUnion tradeUnion,BindingResult result)throws URISyntaxException{
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
    
	return new ResponseEntity<TradeUnion>(tradeUnionService.save(tradeUnion),HttpStatus.CREATED);
}
@PutMapping("/trade-unions/{id}")
public ResponseEntity<?>updateSetup(@Valid @PathVariable Long id, @RequestBody TradeUnion tradeUnion,BindingResult result){
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
    
    if(id!=tradeUnion.getId()) {
    	
    	return new ResponseEntity<>("Invalid Id suplied",HttpStatus.NOT_FOUND);
    }
    
	return new ResponseEntity<TradeUnion>(tradeUnionService.save(tradeUnion),HttpStatus.OK);
}

@DeleteMapping("/trade-unions/{id}")
public ResponseEntity<?> deleteSetup(@PathVariable Long id) {
	    
	tradeUnionService.deleteById(id);
	return new ResponseEntity<>("Trade union deleted successfully",HttpStatus.OK);
}
}
