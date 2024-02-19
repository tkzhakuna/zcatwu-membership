package com.sintaks.mushandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sintaks.mushandi.model.Sector;

public interface SectorRepository extends JpaRepository<Sector, Long> {
	@Query("SELECT s FROM Sector s WHERE s.sectorName = ?1 and s.tradeUnion.id = ?2")
	Sector checkUniqueSector(String sectorName,Long tuId);
	@Query("SELECT s FROM Sector s WHERE s.sectorCode = ?1 and s.tradeUnion.id = ?2")
	Sector checkUniqueSectorCode(String sectorCode,Long tuId);
	@Query("SELECT s FROM Sector s WHERE s.sectorName = ?1 and s.id<>?2 and s.tradeUnion.id = ?3")
	Sector findBySectorNameAndTU(String sectorName,Long sectorId,Long tuId);
	@Query("SELECT s FROM Sector s WHERE s.sectorCode = ?1 and s.id<>?2 and s.tradeUnion.id = ?3")
	Sector findBySectorCodeAndTU(String sectorCode,Long sectorId,Long tuId);
}
