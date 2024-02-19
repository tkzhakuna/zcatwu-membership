package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.*;
import com.sintaks.mushandi.model.Sector;
import com.sintaks.mushandi.model.TradeUnion;
import com.sintaks.mushandi.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class SectorService implements BaseService<Sector> {

	private final SectorRepository sectorRepository;
	private final TradeUnionService tuService;

	public SectorService(SectorRepository sectorRepository, TradeUnionService tuService) {
		this.sectorRepository = sectorRepository;
		this.tuService = tuService;
	}

	@Override
	public List<Sector> findAll() {

			List<Sector> sectorList;
			try {
			sectorList=sectorRepository.findAll();
			}catch(Exception ex){
				throw new UnexpectedException("Error finding sectors");
			}
		
			if(sectorList!=null) {
				return sectorList;
			}else{
				throw new NotFoundException("No sectors found");
			}
	}


	public Sector getOne(Long id) {
		Sector sector;
		try {
		sector= sectorRepository.getOne(id);
		}catch(Exception ex) {
			throw new UnexpectedException("Error fetching sector: "+ex.getLocalizedMessage());
		}

		if(sector!=null){
			return sector;
		}else{
			throw new NotFoundException("Sector not found");
		}
	}

	@Override
	public Sector save(Sector entity, Principal principal) {

		TradeUnion tu;
		try {
		tu=tuService.findById((long) 1).get();
		}catch(Exception ex){
			throw new UnexpectedException("Error retrieving Trade union details: "+ex.getLocalizedMessage());
		}

		if (entity != null) {
			// if updating
			if (entity.getId() != null && !existsById(entity.getId())) {
				throw new NotFoundException("Sector id not found");
			}

			if (checkUniqueSector(entity, tu.getId())) {
				throw new AlreadyExistsException("Sector name already registerd");
			}
			if (checkUniqueSectorCode(entity, tu.getId())) {
				throw new AlreadyExistsException("Sector code already registerd");
			}
			
			try {
				if (entity.getId() == null)
					entity.setTradeUnion(tu);
				return sectorRepository.save(entity);
			} catch (Exception ex) {
				throw new NotSavedException("Sector not saved.");
			}
		}else {
			throw new NotSavedException("Sector not saved.");
		}
	}

	@Override
	public Sector saveNew(Sector sector) {
		return null;
	}

	@Override
	public Sector update(Sector sector) {
		return null;
	}

	@Override
	public Optional<Sector> findById(Long id) {
		try {
		Optional<Sector>sector=sectorRepository.findById(id);
		return  sector;
		}catch(Exception ex) {
			throw new NotFoundException("Sector not found");
		}
	}


	public boolean existsById(Long id) {
		Boolean bool;
		try {
			bool= sectorRepository.existsById(id);
		}catch(Exception ex){
			throw new UnexpectedException("Error retrieving sector: "+ex.getLocalizedMessage());
		}

		if(bool){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Boolean deleteById(Long id) {
		Boolean bool;
		try {
			bool= sectorRepository.existsById(id);
		}catch(Exception ex){
			throw new UnexpectedException("Error retrieving sector: "+ex.getLocalizedMessage());
		}

		if(bool) {
			try {
				sectorRepository.deleteById(id);
				return true;
			} catch (Exception ex) {
				throw new NotDeletedException("Error deleting sector: "+ex.getLocalizedMessage());
			}
		}else{
			throw new NotFoundException("Sector not found");
		}
	}

	boolean checkUniqueSector(Sector sector, Long tuId) {
		try {
			// check if name already exists before saving new
			if (sector.getId() == null && sectorRepository.checkUniqueSector(sector.getSectorName(), tuId) != null) {
				return true;
			}
			// check if name already exists before updating
			if (sector.getId() != null) {
				if (sectorRepository.findBySectorNameAndTU(sector.getSectorName(), sector.getId(), tuId) != null)
					return true;
			}
		}catch(Exception ex){
			throw new UnexpectedException("Error checking sector details: "+ex.getLocalizedMessage());
		}
		return false;
	}
	boolean checkUniqueSectorCode(Sector sector, Long tuId) {
		try{
		// check if name already exists before saving new
		if (sector.getId() == null && sectorRepository.checkUniqueSectorCode(sector.getSectorCode(), tuId) != null) {
			return true;
		}
		// check if name already exists before updating
		if (sector.getId() != null) {
			if (sectorRepository.findBySectorCodeAndTU(sector.getSectorCode(),sector.getId(), tuId) != null)
				return true;
		}
	}catch(Exception ex){
		throw new UnexpectedException("Error checking sector details: "+ex.getLocalizedMessage());
	}
		return false;
	}


}
