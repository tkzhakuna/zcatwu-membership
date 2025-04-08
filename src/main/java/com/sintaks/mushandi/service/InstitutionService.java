package com.sintaks.mushandi.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import com.sintaks.mushandi.exceptions.*;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.sintaks.mushandi.model.Institution;
import com.sintaks.mushandi.repository.InstitutionRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class InstitutionService implements BaseService<Institution> {

	private final InstitutionRepository ir;
	private final UserRepository userRepository;

	@Override
	public List<Institution> findAll() {
		List<Institution>institutions;
		try {
			institutions= ir.findAll();
			return institutions;
		} catch (Exception ex) {
		throw new UnexpectedException("Error fetching institutions: "+ex.getLocalizedMessage());
		}

	}

	@Override
	public Institution save(Institution entity, Principal principal) {
		if (entity != null) {
			
			// if updating
			if (entity.getId() != null && !existsById(entity.getId())) {
				throw new NotFoundException("Institution id not found");
			}

			if (checkUniqueInstitution(entity, entity.getSector().getTradeUnion().getId())) {
				throw new AlreadyExistsException("Institution name already registerd");
			}

			try {

				return ir.save(entity);
			} catch (Exception ex) {
				throw new NotSavedException("Institution Not Saved: "+ex.getLocalizedMessage());
			}
		} else {
			throw new NotSavedException("Institution Not Saved");
		}
	}

	@Override
	public Institution saveNew(Institution institution) {
		return null;
	}

	@Override
	public Institution update(Institution entity) {
		return null;
	}

	@Override
	public Optional<Institution> findById(Long id) {
		try {
			return ir.findById(id);
		} catch (Exception ex) {
			throw new NotFoundException("Institution not found");
		}
	}


	public boolean existsById(Long id) {
		try {
			return ir.existsById(id);
		}catch(Exception ex){
			log.error("Error finding institution: "+ex.getLocalizedMessage());
			return false;
		}

	}

	@Override
	public Boolean deleteById(Long id) {
		try {
		ir.deleteById(id);
		return true;
		}catch (Exception ex) {
			throw new NotDeletedException("Institution not deleted");
		}

	}



	boolean checkUniqueInstitution(Institution institution, Long tuId) {
		try {
			// check if name already exists before saving new
			if (institution.getId() == null && ir.checkUniqueInstitution(institution.getInstitutionName(), tuId) != null) {
				return true;
			}
			// check if name already exists before updating
			if (institution.getId() != null) {
				if (ir.findByInstitutionNameAndTU(institution.getInstitutionName(), institution.getId(), tuId) != null)
					return true;
			}
		}catch (Exception ex){
			throw new UnexpectedException("Error checking Institution: "+ex.getLocalizedMessage());
		}
		return false;
	}
}
