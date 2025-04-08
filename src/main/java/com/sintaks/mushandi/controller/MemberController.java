package com.sintaks.mushandi.controller;

import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sintaks.mushandi.model.projections.MemberView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sintaks.mushandi.model.Member;
import com.sintaks.mushandi.service.MapValidationErrorService;
import com.sintaks.mushandi.service.MemberService;

@Slf4j
@AllArgsConstructor
@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class MemberController {

private final MemberService memberService;
private final MapValidationErrorService mapValidationErrorService;

@GetMapping("/members")
public Page<Member>findAll(Pageable pageable){
	return memberService.findAll(pageable);
}

@GetMapping("/members-list")
public void findAll(HttpServletResponse response,Principal principal){
	 memberService.viewAll(response,principal.getName());
}

@GetMapping("/youths")
public void findByBranch(HttpServletResponse response,Principal principal){
	 memberService.viewByBranch(response,principal.getName());
}

@GetMapping("/current-stoporders/{tuId}")
public ResponseEntity<List<MemberView>>findAllActiveMembers(@PathVariable Long tuId){
	
	return  ResponseEntity.ok(memberService.findAllActive(tuId));
}


@GetMapping("/members/{id}")
public ResponseEntity<?>findMember(@PathVariable Long id){
	return new ResponseEntity<>(memberService.findById(id),HttpStatus.OK);
}

@GetMapping("/members/institutions/{institutionId}")
public ResponseEntity<?>findMemberByInstitution(@PathVariable Long id){
	return new ResponseEntity<>(memberService.findById(id),HttpStatus.OK);
}

@GetMapping("/arrears-stats/{tuId}/{arrears}")
public ResponseEntity<?>findArrears(@PathVariable Long tuId,@PathVariable Boolean arrears){
	Long arrs=memberService.getArrearsStats(tuId,arrears);
	if(arrs!=null) {
	return new ResponseEntity<>(arrs,HttpStatus.OK);
	}else {
		return new ResponseEntity<>(0,HttpStatus.OK);
	}
	
}
	@PostMapping(path="/members-new",consumes={MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Member>addMemberNew(@Valid @RequestBody Member member){
	log.info("Request from casper ---------------------------");
//	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//    if(errorMap!=null)
//    	return errorMap;

		return new ResponseEntity<>(memberService.saveNew(member),HttpStatus.CREATED);
	}

@PostMapping(path="/members",consumes={MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_JSON_VALUE})
public ResponseEntity<Member>addMember(@Valid @RequestBody Member member,Principal principal){
//	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//    if(errorMap!=null)
//    	return errorMap;

	return new ResponseEntity<>(memberService.save(member,principal),HttpStatus.CREATED);
}
@PutMapping("/members/{id}")
public ResponseEntity<?>updateMember(@Valid @RequestBody Member member,BindingResult result,Principal principal){
	ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if(errorMap!=null) 
    	return errorMap;
    
	return new ResponseEntity<Member>(memberService.save(member,principal),HttpStatus.OK);
}
@DeleteMapping("/members/{id}")
public void deleteMember(@PathVariable Long id) {
	memberService.deleteById(id);
}

//	@ExceptionHandler
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public void handle(Exception e) {
//		log.warn("Returning HTTP 400 Bad Request", e);
//	}
}
