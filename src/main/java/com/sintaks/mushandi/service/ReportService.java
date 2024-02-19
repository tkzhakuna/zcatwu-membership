package com.sintaks.mushandi.service;

import com.sintaks.mushandi.model.dto.ReportDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@Service
public interface ReportService {
    Long findByGender(String gender, String username);

    Long findYouthsByGender(String gender, String username);

    List<ReportDTO> findByBranch(String username);

    List<ReportDTO> findBySector(String username);

    List<ReportDTO> findByInstitution(String username);

    void printSchedule(HttpServletResponse response, Long institutionId, LocalDate fromDate, LocalDate toDate, String userName);

    ResponseEntity<?> downloadSchedule( LocalDate fromDate, LocalDate toDate, String userName);

    void viewMemberReport(HttpServletResponse response, Long institutionId, String username);

    ResponseEntity<?> generateReport(Long branchId);

    void printInvoice(HttpServletResponse response, Long institutionId, String userName);

    void viewArrearsReportForInstitution(HttpServletResponse response, Long institutionId, String name);
}
