package com.sintaks.mushandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sintaks.mushandi.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
    User getById(Long id);

}
