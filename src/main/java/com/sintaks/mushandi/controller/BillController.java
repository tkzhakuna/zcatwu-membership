package com.sintaks.mushandi.controller;

import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sintaks.mushandi.model.Bill;
import com.sintaks.mushandi.service.BillService;
import com.sintaks.mushandi.service.MapValidationErrorService;
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class BillController {
@Autowired 
private BillService billService;
@Autowired
private MapValidationErrorService mapValidationErrorService;


@PostMapping("/bills")
public ResponseEntity<?>addBill(@Valid @RequestBody Bill bill, BindingResult result,Principal principal)throws URISyntaxException{
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
    
	return new ResponseEntity<Bill>(billService.saveBill(bill,principal),HttpStatus.CREATED);
}
@PostMapping("/bill-all/{billDate}")
public ResponseEntity<?>updateBill(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate billDate,Principal principal)throws Exception{
	   
    if(billService.billAll(principal.getName(), billDate)==true) {
    	return  ResponseEntity.ok("Billing successful");
    }else {
    	return new ResponseEntity<>("Billing failed",HttpStatus.BAD_REQUEST);
    }
	
}

}
