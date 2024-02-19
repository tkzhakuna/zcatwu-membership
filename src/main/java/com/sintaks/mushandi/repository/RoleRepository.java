package com.sintaks.mushandi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sintaks.mushandi.model.ERole;
import com.sintaks.mushandi.model.Role;



public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}
