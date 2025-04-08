package com.sintaks.mushandi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.sintaks.mushandi.model.Institution;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
	@Query("SELECT i FROM Institution i WHERE i.institutionName = ?1 and i.sector.tradeUnion.id = ?2")
	Institution checkUniqueInstitution(String institutionName,Long tuId);
	@Query("SELECT i FROM Institution i WHERE i.institutionName = ?1 and i.id<>?2 and i.sector.tradeUnion.id = ?3")
	Institution findByInstitutionNameAndTU(String institutionName,Long institutionId,Long tuId);

    Institution findByInstitutionName(String temp);
}
