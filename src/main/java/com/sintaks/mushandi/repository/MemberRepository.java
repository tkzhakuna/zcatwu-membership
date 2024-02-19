package com.sintaks.mushandi.repository;

import java.time.LocalDate;
import java.util.List;

import com.sintaks.mushandi.model.projections.MemberView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sintaks.mushandi.model.Member;

import com.sintaks.mushandi.model.dto.ReportDTO;


public interface MemberRepository extends JpaRepository<Member, Long> {
	@Query("SELECT COUNT(m.id) FROM Member m WHERE m.nationalId = ?1 and m.grade.sector.tradeUnion.id = ?2 ")
	long checkUniqueNationalId(String nationalId,Long tuId);
	@Query("SELECT COUNT(m.id) FROM Member m WHERE m.nationalId = ?1 and m.id<>?2 and m.grade.sector.tradeUnion.id = ?3")
	long findByNationalIdAndTU(String nationalId,Long memberId,Long tuId);
	@Query("FROM Member m WHERE m.nationalId = ?1  and m.grade.sector.tradeUnion.id = ?2")
	Member findMemberByNationalIdAndTU(String nationalId,Long tuId);
	
	@Query("SELECT COUNT(m.id) FROM Member m WHERE m.gender =:gender  and m.status='Active' AND m.grade.sector.tradeUnion.id =:tuId ")
	Long findByGender(@Param("gender")String gender,@Param("tuId")Long tuId);
	
	@Query("SELECT COUNT(m.id) FROM Member m WHERE m.dob>:date AND m.gender =:gender  and m.status='Active' AND m.grade.sector.tradeUnion.id =:tuId ")
	Long findYouthsByGender(@Param("date")LocalDate date, @Param("gender")String gender,@Param("tuId")Long tuId); 
	
	@Query("SELECT new ReportDTO( (m.currentBranch.branchName) as label, count(m.id) as counts) from Member m where m.status='Active' AND m.grade.sector.tradeUnion.id =:tuId  group by m.currentBranch.branchName")
	List<ReportDTO> findByBranch(@Param("tuId")Long tuId);
	
	@Query("from Member m where m.status=:status AND m.grade.sector.tradeUnion.id =:tuId ")
	List<Member>findByStatusAndTradeUnionId(@Param("status")String status,@Param("tuId")Long tuId);
	
	@Query("SELECT new ReportDTO((m.grade.sector.sectorName) as label, count(m.id) as counts) from Member m where m.status='Active' AND m.grade.sector.tradeUnion.id =:tuId  group by m.grade.sector.sectorName")
	List<ReportDTO> findBySector(@Param("tuId")Long tuId);

	@Query(value = "SELECT distinct(member.id) as id,surname as surname,firstname as firstname,gender as gender,national_id as nationalId,cell_number as cellNumber,member.email as email,"+
	"town as town, grade.grade_name as grade, status as status,institution.institution_name as institution " +
			"FROM member,grade,sector,institution,stop_order WHERE stop_order.institution=institution.id AND stop_order.member=member.id AND " +
			" member.grade=grade.id AND " +
			"sector.trade_union=:tuId",nativeQuery = true)
	List<MemberView>activeMemberList(Long tuId);

	@Query("SELECT s.member FROM StopOrder s WHERE s.member.id=:id AND s.institution.id=:institutionId")
	Member findByIdAndInstitution(Long id,Long institutionId);

	@Query("SELECT s.member FROM StopOrder s WHERE s.institution.id=:institutionId")
    List<Member> findByInstitution(Long institutionId);
}
