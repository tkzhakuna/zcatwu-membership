package com.sintaks.mushandi.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	try {
    		
        User user = userRepository.findByUsername(username);
        if(user!=null) 
        return user.build(user);
    	}catch(UsernameNotFoundException unf) {
    		throw new UsernameNotFoundException("Invalid login");
    		
    	}catch(Exception ex) {
    		throw new RuntimeException("User not found: "+ex.getLocalizedMessage());
    	}
    	return null;
    }


//    @Transactional
//    public User loadUserById(Long id){
//        User user = userRepository.getById(id);
//        if(user==null) throw new UsernameNotFoundException("User not found");
//        return user.build(user);
//
//    }
}
