package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.*;
import com.sintaks.mushandi.model.TradeUnion;
import com.sintaks.mushandi.repository.TradeUnionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TradeUnionService {

	private final TradeUnionRepository tradeUnionRepository;
	public TradeUnionService(TradeUnionRepository tradeUnionRepository) {
		this.tradeUnionRepository = tradeUnionRepository;
	}

	public List<TradeUnion> findAll() {
		List<TradeUnion> tuList;
		try {
			tuList=tradeUnionRepository.findAll();
		}catch(Exception ex){
			throw new UnexpectedException("Error retrieving trade union details: "+ex.getLocalizedMessage());
		}

		if (tuList != null && !tuList.isEmpty()) {
			return tuList;
		} else {

			throw new NotFoundException("No trade union found");
		}
	}

	public TradeUnion getOne(Long id) {
		TradeUnion tradeUnion;
		try {
			tradeUnion= tradeUnionRepository.getOne(id);
		}catch(Exception ex){
			throw new UnexpectedException("Error retrieving trade union details: "+ex.getLocalizedMessage());
		}

		if(tradeUnion!=null){
			return tradeUnion;
		}else{
			throw new NotFoundException("Trade union not found");
		}
	}

	@Transactional
	public TradeUnion save(TradeUnion entity) {
		if (entity != null) {
			// if updating
			if (entity.getId() != null && !existsById(entity.getId())) {
				throw new NotFoundException("Trade union id not found");
			}

			if (checkUniqueTU(entity)) {
				throw new AlreadyExistsException("Trade union name already registerd");
			}
			if (checkUniqueAbbreviation(entity)) {
				throw new AlreadyExistsException("Trade union Abbreviation already registerd");
			}

			try {
				return tradeUnionRepository.save(entity);
			} catch (Exception ex) {
				throw new NotSavedException("Trade union details not saved");
			}
		}
		return null;
	}

	public Optional<TradeUnion> findById(Long id) {
		Optional<TradeUnion> tu;
		try {
		tu=tradeUnionRepository.findById(id);
		}catch(Exception ex){
			throw new UnexpectedException("Error retrieving Trade union details: "+ex.getLocalizedMessage());
		}
		if (!tu.isPresent()) {
			throw new NotFoundException("Trade union not found");
		}else{
			return tu;
		}

	}

	public boolean existsById(Long id) {
		try {
			return tradeUnionRepository.existsById(id);
		}catch(Exception ex){
			throw new UnexpectedException("Error retrieving trade union details: "+ex.getLocalizedMessage());
		}
	}

	public void deleteById(Long id) {
		if (!existsById(id)) {
			throw new NotFoundException("Trade union id not found");
		}

		try {
			tradeUnionRepository.deleteById(id);
		} catch (Exception ex) {
			throw new NotDeletedException("Error deleting Trade union: "+ex.getLocalizedMessage());
		}

	}

	public TradeUnion findByTuName(String tuName) {
		try {
			return tradeUnionRepository.findByTuName(tuName);
		}catch(Exception ex){
			throw new UnexpectedException("Error finding trade union: "+ex.getLocalizedMessage());
		}
	}

	public TradeUnion findByAbbreviation(String abbreviation) {
	try {
		return tradeUnionRepository.findByAbbreviation(abbreviation);
	}catch(Exception ex){
		throw new UnexpectedException("Error retrieving trade union details: "+ex.getLocalizedMessage());
	}
	}

	boolean checkUniqueTU(TradeUnion tu) {
		try {
			// check if name already exists before saving new
			if (tu.getId() == null && findByTuName(tu.getTuName()) != null) {
				return true;
			}
			// check if name already exists before updating
			if (tu.getId() != null) {
				if (tradeUnionRepository.findTUByIdAndName(tu.getId(), tu.getTuName()) != null)
					return true;
			}
		}catch(Exception ex){
			throw new UnexpectedException("Error checking unique trade union: "+ex.getLocalizedMessage());
		}
		return false;
	}

	boolean checkUniqueAbbreviation(TradeUnion tu) {
		try {
			// check if abbreviation already exists before saving new
			if (tu.getId() == null && findByAbbreviation(tu.getAbbreviation()) != null) {
				return true;
			}
			// check if abbreviation already exists before updating
			if (tu.getId() != null) {
				if (tradeUnionRepository.findTUByIdAndAbbreviation(tu.getId(), tu.getAbbreviation()) != null)
					return true;
			}
		}catch(Exception ex){
			throw new UnexpectedException("Error checking trade union details: "+ex.getLocalizedMessage());
		}
		return false;
	}

}
