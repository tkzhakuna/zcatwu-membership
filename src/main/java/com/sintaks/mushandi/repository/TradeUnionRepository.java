package com.sintaks.mushandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sintaks.mushandi.model.TradeUnion;

public interface TradeUnionRepository extends JpaRepository<TradeUnion, Long> {
	TradeUnion findByTuName(String tuName);
	TradeUnion findByAbbreviation(String abbreviation);
	

@Query("SELECT t FROM TradeUnion t WHERE t.id <> ?1 and t.tuName = ?2")
TradeUnion findTUByIdAndName(Long id, String name);
@Query("SELECT t FROM TradeUnion t WHERE t.id <> ?1 and t.abbreviation = ?2")
TradeUnion findTUByIdAndAbbreviation(Long id, String abbreviation);
}
