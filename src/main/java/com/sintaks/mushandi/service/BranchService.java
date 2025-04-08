package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.AlreadyExistsException;
import com.sintaks.mushandi.exceptions.NotDeletedException;
import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.exceptions.NotSavedException;
import com.sintaks.mushandi.model.Branch;
import com.sintaks.mushandi.model.TradeUnion;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.BranchRepository;
import com.sintaks.mushandi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class BranchService implements BaseService<Branch> {
	private final BranchRepository branchRepository;
	private final TradeUnionService tuService;
	private final UserRepository userRepository;


	@Override
	public Branch save(Branch entity, Principal principal) {
		if (entity != null) {
			User user=userRepository.findByUsername(principal.getName());
			TradeUnion tu=user.getEmployee().getTradeUnion();

			// if updating
			if (entity.getId() != null && !findById(entity.getId()).isPresent()) {
				throw new NotFoundException("Branch id not found");
			}

			if (checkUniqueBranch(entity, entity.getTradeUnion().getId())) {
				throw new AlreadyExistsException("Branch name already registerd");
			}

			try {
				if (entity.getId() == null) {
					entity.setTradeUnion(tu);
				}
				return branchRepository.save(entity);
			} catch (Exception ex) {
				throw new NotSavedException("Branch details not saved");
			}
		}else {
			throw new NotSavedException("Branch details not saved");
		}
	}
	public Branch saveNew(Branch entity) {
		if (entity != null) {
			User user=userRepository.findByUsername(entity.getEmail());
			TradeUnion tu=user.getEmployee().getTradeUnion();

			// if updating
			if (entity.getId() != null && !findById(entity.getId()).isPresent()) {
				throw new NotFoundException("Branch id not found");
			}

			if (checkUniqueBranch(entity, entity.getTradeUnion().getId())) {
				throw new AlreadyExistsException("Branch name already registerd");
			}

			try {
				if (entity.getId() == null) {
					entity.setTradeUnion(tu);
				}
				return branchRepository.save(entity);
			} catch (Exception ex) {
				throw new NotSavedException("Branch details not saved");
			}
		}else {
			throw new NotSavedException("Branch details not saved");
		}
	}
	@Override
	public Branch update(Branch branch) {
		return null;
	}

	@Override
	public List<Branch> findAll() {
		List<Branch> branchList=null;
		try {
			branchList = branchRepository.findAll();
		}catch (Exception ex) {
			throw new NotFoundException("Error fetching branches: "+ex.getLocalizedMessage());
		}
		if(branchList!=null&&!branchList.isEmpty()){
			return  branchList;
		}else {
			throw new NotFoundException("No branch found");
		}
	}

	@Override
	public Optional<Branch> findById(Long id) {
		Optional<Branch>branches=null;

		try {
			branches= branchRepository.findById(id);
		}catch (Exception ex){
			throw  new NotFoundException("Error getting memmber: "+ex.getLocalizedMessage());
		}

		if(branches==null||!branches.isPresent()){
			throw  new NotFoundException("Branch not found");
		}else{
			return branches;
		}
	}


	@Override
	public Boolean deleteById(Long id) {
		try {
			branchRepository.deleteById(id);
			return true;
		}catch (Exception ex) {
			throw new NotDeletedException("Branch not deleted: "+ex.getLocalizedMessage());
		}

	}

	boolean checkUniqueBranch(Branch branch, Long tuId) {
		// check if name already exists before saving new
		if (branch.getId() == null && branchRepository.checkUniqueBranch(branch.getBranchName(), tuId) != null) {
			return true;
		}
		// check if name already exists before updating
		if (branch.getId() != null) {
			if (branchRepository.findBySectorNameAndTU(branch.getBranchName(), branch.getId(), tuId) != null)
				return true;
		}
		return false;
	}

}
