package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.exceptions.UsernameAlreadyExistsException;
import com.sintaks.mushandi.model.ERole;
import com.sintaks.mushandi.model.Employee;
import com.sintaks.mushandi.model.Role;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.EmployeeRepository;
import com.sintaks.mushandi.repository.RoleRepository;
import com.sintaks.mushandi.repository.TradeUnionRepository;
import com.sintaks.mushandi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService  {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserService(UserRepository userRepository, EntityManager entityManager,
					   RoleRepository roleRepository,EmployeeRepository employeeRepository) {
		this.userRepository = userRepository;
		this.entityManager = entityManager;
		this.roleRepository = roleRepository;
		this.employeeRepository=employeeRepository;
	}

	public User saveUser (User user){
		Set<Role> roles = new HashSet<>();
        try{
        	if(userRepository.findByUsername(user.getUsername())!=null) {
        		throw new UsernameAlreadyExistsException("Username '"+user.getUsername()+"' already exists");
        	}else{
        	Set<String> strRoles = user.getStrRoles();

    		strRoles.add("admin");//to be removed
    		strRoles.add("membership");//to be removed
    		strRoles.add("accounts");//to be removed
    		if (strRoles != null|!strRoles.isEmpty()) {
    			
    			strRoles.forEach(role -> {
    				switch (role) {
    				case "admin":
    					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
    							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    					roles.add(adminRole);

    					break;
    				case "membership":
    					Role stockRole = roleRepository.findByName(ERole.ROLE_MEMBERSHIP)
    							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    					roles.add(stockRole);

    					break;
    				case "accounts":
    					Role cashierRole = roleRepository.findByName(ERole.ROLE_ACCOUNTS)
    							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    					roles.add(cashierRole);

    					break;
    				
    				default:
    					throw new RuntimeException("Error: Role is not found.");
    					
    				}
    			});
    		}
			}
		}catch(UsernameAlreadyExistsException uae) {
			throw new RuntimeException("User already exists");

		}catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}


    		Employee employee;
    		//check employyee
				try{
					employee=employeeRepository.findByEmail(user.getUsername());
				}catch(Exception ex){
					throw new NotFoundException("Error fetching employee: "+ex.getLocalizedMessage());
				}

				if(employee!=null) {
					//Add Employee
					user.setEmployee(employee);//Fix to dynamically add employee
					user.setRoles(roles);
					user.setStatus(1);
					user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				}else{
					throw new NotFoundException("Employee with provided email not found");
				}

				try {
					return userRepository.save(user);
				}catch (Exception ex){
					throw new UnexpectedException("Error saving User: "+ex.getLocalizedMessage());
				}
    		


    }

public User updateUser(Integer id,User user) {
	if (Objects.isNull(user)) {
		throw new InvalidParameterException("Please provide user details");
	} else if (!id.equals(user.getId())) {
		throw new InvalidParameterException("Invalid details provided");
	}

	User usr=userRepository.findById(user.getId()).orElseThrow(()->
			new NotFoundException("User with provided id not found"));

	if(checkUniqueUser(user.getUsername(), user.getId())!=null) {
		throw new UsernameAlreadyExistsException("Username '"+user.getUsername()+"' already exists");
	}else {
		try {

			Set<String> strRoles = user.getStrRoles();
			Set<Role> roles = new HashSet<>();
			Set<String>existingRoles=user.getRoles().stream().map(role->role.getName().toString()).collect(Collectors.toSet());
			if (strRoles != null | !strRoles.isEmpty()) {

				strRoles.forEach(role -> {
					switch (role) {
						case "admin":
							Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
									.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(adminRole);

							break;
						case "membership":
							Role stockRole = roleRepository.findByName(ERole.ROLE_MEMBERSHIP)
									.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(stockRole);

							break;
						case "accounts":
							Role cashierRole = roleRepository.findByName(ERole.ROLE_ACCOUNTS)
									.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(cashierRole);

							break;

						default:
							throw new RuntimeException("Error: Role is not found.");

					}
				});
			}


			user.getRoles().addAll(roles);
			user.setPassword((usr.getPassword()));

			if (user.getRoles() != null && !user.getRoles().isEmpty()) {
				return userRepository.save(user);
			} else {
				throw new RuntimeException("User account is not activated, Please add a role to activate");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error saving user.");
		}
	}
}

public User checkUniqueUser(String username, Long id) {
	try {
    TypedQuery<User> query = entityManager.createQuery(
        "SELECT c FROM User c WHERE c.username = :username and c.id<>"+id+"", User.class);
     return query.setParameter("username", username).getSingleResult();
	}catch(Exception ex) {
		return null;
	}
  } 

public User findByUsername(String username) {
		User user;
		try {
			user= userRepository.findByUsername(username);
		}catch(Exception ex){
			throw new UnexpectedException("Error getting user details, please try again");
		}
		if(user!=null){
			return  user;
		}else{
			throw new NotFoundException("User does not exist");
		}
}

public List<User>findAll(){
	return userRepository.findAll();
}
public List<Role>findRoles(){
	return roleRepository.findAll();
}

	public  User findById(Integer id) {
		return userRepository.findById(Long.valueOf(id)).orElseThrow(()->
				new RuntimeException("User not found"));
	}
}
