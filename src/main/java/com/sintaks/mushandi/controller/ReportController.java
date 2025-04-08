package com.sintaks.mushandi.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sintaks.mushandi.model.dto.ReportDTO;
import com.sintaks.mushandi.service.ReportService;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {

        this.reportService = reportService;
    }

    @GetMapping("/findbygender/{gender}")
    private Long findByGender(@PathVariable String gender, Principal principal) {
        return reportService.findByGender(gender, principal.getName());
    }

    @GetMapping("/findyouthsbygender/{gender}")
    Long findYouthsByGender(@PathVariable String gender, Principal principal) {
        return reportService.findYouthsByGender(gender, principal.getName());
    }

    @GetMapping("/findbybranch")
    List<ReportDTO> findByBranch(Principal principal) {
        return reportService.findByBranch(principal.getName());
    }

    @GetMapping("/findbysector")
    List<ReportDTO> findBySector(Principal principal) {
        return reportService.findBySector(principal.getName());
    }

    @GetMapping("/findbyinstitution")
    List<ReportDTO> findByInstitution(Principal principal) {
        return reportService.findByInstitution(principal.getName());
    }

    @GetMapping("/download-so-schedule/{companyId}/{fromDate}/{toDate}")
    public void downloadSo(HttpServletResponse response, @PathVariable Long companyId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                           @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate, Principal principal) {
        reportService.printSchedule(response, companyId, fromDate, toDate, principal.getName());
    }
    @GetMapping("/download-trade-union-report/{companyId}/{fromDate}/{toDate}")
    public void downloadTradeUnionReport(HttpServletResponse response, @PathVariable Long companyId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                           @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate, Principal principal) {
        reportService.printTradeUnionReport(response, companyId, fromDate, toDate, principal.getName());
    }

    @GetMapping("/download-recruitment-schedule/{fromDate}/{toDate}")
    public ResponseEntity<?> downloadSoSchedule(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate, Principal principal) {
        return reportService.downloadSchedule(fromDate, toDate, principal.getName());
    }

    @GetMapping("/print-invoice/{companyId}")
    public void downloadInvoice(HttpServletResponse response, @PathVariable Long companyId, Principal principal) {
        reportService.printInvoice(response, companyId, principal.getName());
    }

    @GetMapping("/download-by-institution/{companyId}")
    public void downloadByInstitution(HttpServletResponse response, @PathVariable Long companyId, Principal principal) {
        reportService.viewMemberReport(response, companyId, principal.getName());
    }

    @GetMapping(path = "/csv/branch-id/{branchId}", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<?> getCSV(@PathVariable Long branchId) {
        return reportService.generateReport(branchId);
    }

}
