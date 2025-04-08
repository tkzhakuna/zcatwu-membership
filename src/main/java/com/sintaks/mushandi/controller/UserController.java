package com.sintaks.mushandi.controller;


import static com.sintaks.mushandi.security.SecurityConstants.TOKEN_PREFIX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sintaks.mushandi.model.Role;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.payload.JWTLoginSucessReponse;
import com.sintaks.mushandi.payload.LoginRequest;
import com.sintaks.mushandi.security.JwtTokenProvider;
import com.sintaks.mushandi.service.CustomUserDetailsService;
import com.sintaks.mushandi.service.MapValidationErrorService;
import com.sintaks.mushandi.service.UserService;
import com.sintaks.mushandi.validator.UserValidator;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService cus;
    
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
//        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//        if(errorMap != null) return errorMap;

        User user=userService.findByUsername(loginRequest.getUsername());

        if(user!=null) {
        	if(user.getStatus()==0||user.getRoles()==null|user.getRoles().isEmpty()) {
        		 Map<String, String> errorMap1 = new HashMap<>();
        		 errorMap1.put("message", "Your account is not activated, please contact Administrator");
        		 return new ResponseEntity<Map<String, String>>(errorMap1, HttpStatus.BAD_REQUEST);
        	}
        }      
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = TOKEN_PREFIX +  tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){

        // Validate passwords match
        userValidator.validate(user,result);

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null)return errorMap;

        User newUser = userService.saveUser(user);

        return  new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
    
    @PatchMapping("/update-user/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER')")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,@Valid @RequestBody User user, BindingResult result){
        
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null)return errorMap;

        User newUser = userService.updateUser(id,user);

        return  new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    
    @GetMapping("/users")
    public List<User>findAll(){
    	return userService.findAll();
    }

    @GetMapping("/find-by-id/{id}")
    public User findById(@PathVariable Integer id){
        return userService.findById(id);
    }
    
    @GetMapping("/roles")
    public List<Role>findRoles(){
    	return userService.findRoles();
    }
    
}
