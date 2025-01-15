package com.sintaks.mushandi.controller;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sintaks.mushandi.model.Receipt;
import com.sintaks.mushandi.model.ReceiptBatch;
import com.sintaks.mushandi.service.MapValidationErrorService;
import com.sintaks.mushandi.service.ReceiptBatchService;
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class ReceiptBatchController {
	@Autowired
	private ReceiptBatchService rbsi;
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	@Value("${tempDirectory:/tmp/}")
	 private String tempFolder;
	public static final String DIRECTORY_MARKER = "/";
	
	@PostMapping(value="/receipt-batches/{batchId}",consumes= {MediaType.MULTIPART_FORM_DATA_VALUE},produces="application/json")
	public ResponseEntity<?> saveReceipts(@RequestParam(value="files" )  MultipartFile[] files,@PathVariable String batchId,Principal principal) throws Exception{
		
		for(MultipartFile file:files) {
			try {
				
			   file.transferTo(Paths.get(tempFolder+DIRECTORY_MARKER+file.getOriginalFilename()));
				Long batch=Long.valueOf(batchId);
			rbsi.saveReceipt(file,principal.getName(),batch);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping(value="/receipts/{batchId}",produces="application/json")
	public CompletableFuture<ResponseEntity<List<Receipt>>>findAll(@PathVariable String batchId)throws Exception{
		Long batch=Long.valueOf(batchId);
		return rbsi.findByReceiptBatch(batch).thenApply(ResponseEntity::ok);
	}
	
	@GetMapping("/receiptBatches")
	public List<ReceiptBatch>findAll(){
		Pageable pageable= PageRequest.of(0,1000000, Sort.by(Sort.Direction.DESC,"id"));

		return rbsi.findAll(pageable);
	}

	@GetMapping("/receiptBatches/{id}")
	public ResponseEntity<?>findReceiptBatch(@PathVariable Long id){
		return new ResponseEntity<>(rbsi.findById(id),HttpStatus.OK);
	}

	@PostMapping("/receiptBatches")
	public ResponseEntity<?>addReceiptBatch(@Valid @RequestBody ReceiptBatch receiptBatch,BindingResult result,Principal principal)throws URISyntaxException{
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
	    if(errorMap!=null) 
	    	return errorMap;
		
		return new ResponseEntity<ReceiptBatch>(rbsi.saveBatch(receiptBatch,principal.getName()),HttpStatus.CREATED);
	}
	@PutMapping("/receiptBatches/{id}")
	public ResponseEntity<?>updateReceiptBatch(@Valid @RequestBody ReceiptBatch receiptBatch,BindingResult result){
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
	    if(errorMap!=null) 
	    	return errorMap;
	    
		return new ResponseEntity<ReceiptBatch>(rbsi.updateBatch(receiptBatch),HttpStatus.OK);
	}
	@DeleteMapping("/receiptBatches/{id}")
	public ResponseEntity<?> deleteReceiptBatch(@PathVariable Long id) {
		rbsi.deleteBatch(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/batch-list/{batchDate}")
	public void findAll(HttpServletResponse response, Principal principal,
						@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate batchDate){
		rbsi.viewBatches(response,principal.getName(),batchDate);
	}

	@GetMapping("/batch-print/{batchId}")
	public void printBatch(HttpServletResponse response, Principal principal,
						@PathVariable Integer batchId){
		rbsi.printBatch(response,principal.getName(),batchId);
	}
}
