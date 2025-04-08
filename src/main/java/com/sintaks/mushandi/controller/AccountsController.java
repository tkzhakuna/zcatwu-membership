package com.sintaks.mushandi.controller;


import com.sintaks.mushandi.service.ReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountsController {
    private final ReportServiceImpl reportService;

    @GetMapping("/arrears-by-institution/{institutionId}")
    public void downloadArrearsByInstitution(HttpServletResponse response, @PathVariable Long institutionId, Principal principal) {
        reportService.viewArrearsReportForInstitution(response, institutionId, principal.getName());
    }


}