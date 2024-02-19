package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.*;
import com.sintaks.mushandi.model.Grade;
import com.sintaks.mushandi.repository.GradeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class GradeService implements BaseService<Grade> {
private final GradeRepository gradeRepository;




	@Override
	public List<Grade> findAll() {
		List<Grade>grades=null;
		try {
		grades= gradeRepository.findAll();
		}catch(Exception ex) {
			throw new NotFoundException("Error fetching grades: "+ex.getLocalizedMessage());
		}
		if(grades!=null&&!grades.isEmpty()){
			return grades;
		}else{
			throw new NotFoundException("No grades found ");
		}
	}

	@Override
	public Grade save(Grade entity, Principal principal) {
		if(entity!=null) {

			// if updating
			if (entity.getId() != null && !existsById(entity.getId())) {
				throw new NotFoundException("Grade id not found");
			}

			if (checkUniqueGrade(entity, entity.getSector().getTradeUnion().getId())) {
				throw new AlreadyExistsException("Grade name already registerd");
			}
			if (checkUniqueGradeCode(entity, entity.getSector().getTradeUnion().getId())) {
				throw new AlreadyExistsException("Grade code already registerd");
			}

			try {
				return gradeRepository.save(entity);
			}catch(Exception ex) {
				throw new NotSavedException("Grade Not Saved");
			}
		}else {
			throw new NotSavedException("Grade Not Saved");
		}
	}
	@Override
	public Grade saveNew (Grade entity) {
		if(entity!=null) {

			// if updating
			if (entity.getId() != null && !existsById(entity.getId())) {
				throw new NotFoundException("Grade id not found");
			}

			if (checkUniqueGrade(entity, entity.getSector().getTradeUnion().getId())) {
				throw new AlreadyExistsException("Grade name already registerd");
			}
			if (checkUniqueGradeCode(entity, entity.getSector().getTradeUnion().getId())) {
				throw new AlreadyExistsException("Grade code already registerd");
			}

			try {
				return gradeRepository.save(entity);
			}catch(Exception ex) {
				throw new NotSavedException("Grade Not Saved");
			}
		}else {
			throw new NotSavedException("Grade Not Saved");
		}
	}


	@Override
	public Grade update(Grade grade) {
		return null;
	}


	@Override
	public Optional<Grade> findById(Long id) {
		try {
		Optional<Grade>grade=gradeRepository.findById(id);
		if(!grade.isPresent()) {
			throw new NotFoundException("Grade Not Found");
		}
		return gradeRepository.findById(id);
		}catch(Exception ex) {
			throw new NotFoundException("Grade Not Found");
		}
	}

	public boolean existsById(Long id) {
		try {
			return gradeRepository.existsById(id);
		}catch(Exception ex){
			throw new NotFoundException("Grade Not Found");
		}
	}



	@Override
	public Boolean deleteById(Long id) {
		try {
		gradeRepository.deleteById(id);
		return true;
		}catch(Exception ex) {
			throw new NotDeletedException("Grade not deleted");
		}

	}

	
	boolean checkUniqueGrade(Grade grade, Long tuId) {
		try {
			// check if name already exists before saving new
			if (grade.getId() == null && gradeRepository.checkUniqueGrade(grade.getGradeName(), tuId) != null) {
				return true;
			}
			// check if name already exists before updating
			if (grade.getId() != null) {
				if (gradeRepository.findByGradeNameAndTU(grade.getGradeName(), grade.getId(), tuId) != null)
					return true;
			}
		}catch (Exception ex){
			throw new UnexpectedException("Error checking grade: "+ex.getLocalizedMessage());
		}
		return false;
	}
	
	boolean checkUniqueGradeCode(Grade grade, Long tuId) {
		try {
			// check if name already exists before saving new
			if (grade.getId() == null && gradeRepository.checkUniqueGradeCode(grade.getGradeCode(), tuId) != null) {
				return true;
			}
			// check if name already exists before updating
			if (grade.getId() != null) {
				if (gradeRepository.findByGradeCodeAndTU(grade.getGradeCode(), grade.getId(), tuId) != null)
					return true;
			}
		}catch (Exception ex){
			throw new UnexpectedException("Error checking grade: "+ex.getLocalizedMessage());
		}
		return false;
	}


}
