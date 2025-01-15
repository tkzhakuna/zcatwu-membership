package com.sintaks.mushandi.repository;

import java.time.LocalDate;
import java.util.List;

import com.sintaks.mushandi.model.projections.MemberView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sintaks.mushandi.model.StopOrder;
import com.sintaks.mushandi.model.dto.ReportDTO;

public interface StopOrderRepository extends JpaRepository<StopOrder, Long> {
    @Query(value = "FROM StopOrder s WHERE s.institution.sector.tradeUnion.id=:tuId AND"
            + " s.dateTerminated is null GROUP BY s.id")
//    @Query(value = "SELECT member.id as id,member.firstname as firstname,member.surname as surname, member.national_id as nationalId," +
//            "member.cell_number as cellNumber,grade.grade_name as gradeName,institution.institution_name as institutionName,member.status as status" +
//            " FROM member,grade,institution,stop_order WHERE stop_order.institution.sector.tradeUnion.id=:tuId AND"
//            + " s.dateTerminated is null GROUP BY s.id")
    List<StopOrder> findAllStopOrders(@Param("tuId") Long tuId);

    @Query(value = " SELECT new ReportDTO(concat(s.member.firstname,' ',s.member.surname) as fullname,s.member.nationalId as nationalid,"
            + "s.member.grade.gradeName as grade, s.institution.institutionName as institution,s.institution.currency as currency,s.member.gender as gender) "
            + "FROM StopOrder s WHERE s.institution.id<>894 AND s.institution.sector.tradeUnion.id=:tuId AND"
            + " s.dateTerminated is null GROUP BY s.id")
    List<ReportDTO> listAll(@Param("tuId") Long tuId);

    @Query(value = "FROM StopOrder s WHERE s.institution.id<>894 AND s.institution.sector.tradeUnion.id=:tuId AND"
            + " s.dateTerminated is null AND s.member.status='Active'")
    List<StopOrder> selectActive(@Param("tuId") Long tuId);


//    @Query("SELECT s FROM StopOrder s WHERE s.member.status='Active' and  s.institution.id=:institutionId and s.dateTerminated is null ")
//    List<StopOrder> findByInstitution(@Param("institutionId") Long institutionId);
@Query("SELECT s FROM StopOrder s WHERE s.institution.id<>894 AND  s.institution.id=:institutionId and s.dateTerminated is null ")
List<StopOrder> findByInstitution(@Param("institutionId") Long institutionId);

    @Query("SELECT new ReportDTO((s.institution.institutionName) as label,COUNT(s.id) as counts) FROM StopOrder s WHERE s.institution.id<>894 AND s.member.status='Active' AND s.member.grade.sector.tradeUnion.id =:tuId AND s.dateTerminated is null group by s.institution.institutionName ")
    List<ReportDTO> countByInstitution(@Param("tuId") Long tuId);

    @Query(value = " SELECT new ReportDTO(concat(s.member.firstname,' ',s.member.surname) as fullname,s.member.nationalId as nationalid,"
            + "s.member.grade.gradeName as grade, s.institution.institutionName as institution,s.institution.currency as currency,s.member.gender as gender) "
            + "FROM StopOrder s WHERE s.institution.id<>894 AND s.institution.sector.tradeUnion.id=:tuId AND"
            + " s.dateTerminated is null  AND s.member.dob>:date GROUP BY s.id")
    List<ReportDTO> ListByBranch(@Param("date") LocalDate date, @Param("tuId") Long tuId);

    @Query(value = "FROM StopOrder s WHERE s.institution.id=:institutionId AND"
            + " s.dateJoined>=:fromDate AND s.dateJoined<=:toDate AND s.dateTerminated is null ")
    List<StopOrder> getSchedule(Long institutionId,LocalDate fromDate,LocalDate toDate);
    @Query(value = "FROM StopOrder s WHERE s.institution.id<>894 AND s.institution.id=:institutionId AND"
            + " s.member.status='Active' AND s.dateTerminated is null ")
    List<StopOrder> getInvoice(Long institutionId);

    @Query(value = "FROM StopOrder s WHERE s.institution.id<>894 AND s.institution.id=:institutionId  AND s.dateTerminated is null ")
    List<StopOrder>getByInstitution(Long institutionId);

    //@Query("SELECT s FROM StopOrder s WHERE s.member.status='Active' and  s.recruitmentBranch.id=:branchId and s.dateTerminated is null ")
    @Query(value = "SELECT distinct(member.id) as id,dob as dob,stop_order.date_joined as dateRecruited, surname as surname,firstname as firstname,gender as gender,national_id as nationalId,cell_number as cellNumber,member.email as email,"+
            "town as town,branch.branch_name as branch, grade.grade_name as grade, status as status,institution.institution_name as institution, grade.weekly_wage*trade_union.tu_percentage/100 as weeklyDeduction" +
            " FROM member,grade,branch,institution,stop_order,trade_union WHERE trade_union.id=1  AND s.institution.id<>894 AND  stop_order.institution=institution.id AND stop_order.member=member.id AND " +
            " stop_order.date_terminated is null" +
            " AND member.grade=grade.id AND " +
            "member.current_branch=branch.id AND member.current_branch=:branchId group by member.id,stop_order.id",nativeQuery = true)

    List<MemberView>findByBranch(Long branchId);

    @Query(value = "SELECT distinct(member.id) as id,dob as dob,stop_order.date_joined as dateRecruited,surname as surname,firstname as firstname,gender as gender,national_id as nationalId,cell_number as cellNumber,member.email as email,"+
            "town as town,branch.branch_name as branch, grade.grade_name as grade, status as status,institution.institution_name as institution, grade.weekly_wage*trade_union.tu_percentage/100 as weeklyDeduction" +
            " FROM member,grade,branch,institution,stop_order,trade_union WHERE trade_union.id=1 AND s.institution.id<>894 AND  stop_order.institution=institution.id AND stop_order.member=member.id AND " +
            " stop_order.date_terminated is null AND stop_order.date_joined>=:startDate AND stop_order.date_joined<=:endDate" +
            " AND member.grade=grade.id AND " +
            "member.current_branch=branch.id group by member.id,stop_order.id",nativeQuery = true)

    List<MemberView>findRecruitments(LocalDate startDate,LocalDate endDate);




}
