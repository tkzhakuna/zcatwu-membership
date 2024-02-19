package com.sintaks.mushandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sintaks.mushandi.model.Grade;


public interface GradeRepository extends JpaRepository<Grade, Long> {
	@Query("SELECT g FROM Grade g WHERE g.gradeName = ?1 and g.sector.tradeUnion.id = ?2")
	Grade checkUniqueGrade(String gradeName,Long tuId);
	@Query("SELECT g FROM Grade g WHERE g.gradeCode = ?1 and g.sector.tradeUnion.id = ?2")
	Grade checkUniqueGradeCode(String gradeCode,Long tuId);
	
	@Query("SELECT g FROM Grade g WHERE g.gradeName = ?1 and g.id<>?2 and g.sector.tradeUnion.id = ?3")
	Grade findByGradeNameAndTU(String gradeName,Long gradeId,Long tuId);
	@Query("SELECT g FROM Grade g WHERE g.gradeCode = ?1 and g.id<>?2 and g.sector.tradeUnion.id = ?3")
	Grade findByGradeCodeAndTU(String gradeCode,Long gradeId,Long tuId);
}
