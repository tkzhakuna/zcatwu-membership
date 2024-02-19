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

import com.sintaks.mushandi.model.Receipt;
import com.sintaks.mushandi.service.MapValidationErrorService;
import com.sintaks.mushandi.service.ReceiptService;
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class ReceiptController {
@Autowired 
private ReceiptService recService;
@Autowired
private MapValidationErrorService mapValidationErrorService;


//@GetMapping("/receipts")
//public List<Receipt>findAll(){
//	return recService.findAll();
//}
//
//@GetMapping("/receipt/{id}")
//public ResponseEntity<?>findReceipt(@PathVariable Long id){
//	return new ResponseEntity<>(recService.findById(id),HttpStatus.OK);
//}

@PostMapping("/receipts")
public ResponseEntity<?>addReceipt(@Valid @RequestBody Receipt receipt,BindingResult result,Principal principal)throws URISyntaxException{
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
   
    Receipt rec=recService.saveReceipt(receipt,principal);
    if(rec!=null) {
	return new ResponseEntity<>(rec,HttpStatus.CREATED);
    }else {
    	return new ResponseEntity<>("Receipt not saved",HttpStatus.BAD_REQUEST);
    }
}

@GetMapping("/findbybatch/{batchId}")
public List<Receipt>getReceiptList(@PathVariable Long batchId){
	return recService.findByReceiptBatchId(batchId);
}
//@PutMapping("/receipt/{id}")
//public ResponseEntity<Receipt>updateReceipt(@Valid @RequestBody Receipt receipt){
//	return new ResponseEntity<Receipt>(recService.save(receipt),HttpStatus.OK);
//}
//@DeleteMapping("/receipt/{id}")
//public void deleteReceipt(@PathVariable Long id) {
//	recService.deleteById(id);
//}
}
