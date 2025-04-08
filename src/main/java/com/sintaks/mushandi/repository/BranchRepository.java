package com.sintaks.mushandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sintaks.mushandi.model.Branch;


public interface BranchRepository extends JpaRepository<Branch, Long> {
	@Query("SELECT b FROM Branch b WHERE b.branchName = ?1 and b.tradeUnion.id = ?2")
	Branch checkUniqueBranch(String branchName,Long tuId);
	@Query("SELECT b FROM Branch b WHERE b.branchName = ?1 and b.id<>?2 and b.tradeUnion.id = ?3")
	Branch findBySectorNameAndTU(String branchName,Long branchId,Long tuId);
}
