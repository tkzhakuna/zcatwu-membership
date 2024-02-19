package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.model.*;
import com.sintaks.mushandi.model.dto.ReportDTO;
import com.sintaks.mushandi.model.projections.MemberView;
import com.sintaks.mushandi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final StopOrderRepository stoporderRepository;
    private final InstitutionRepository institutionRepository;
    private final CSVWriter csvWriter;
    private final BranchRepository branchRepository;


    @Override
    public Long findByGender(String gender, String username) {
        User user;
        Long count;
        try {
            user = userRepository.findByUsername(username);
        } catch (Exception ex) {
            throw new UnexpectedException("Error getting user details: " + ex.getLocalizedMessage());
        }

        if (user != null) {
            TradeUnion tu = user.getEmployee().getTradeUnion();
            try {
                count = memberRepository.findByGender(gender, tu.getId());
            } catch (Exception ex) {
                throw new NotFoundException("Error counting members by gender: " + ex.getLocalizedMessage());
            }
        } else {
            throw new NotFoundException("User not found");
        }

        if (count != null) {

            return count;
        } else {
            throw new NotFoundException("No gender statistics found");
        }
    }

    @Override
    public Long findYouthsByGender(String gender, String username) {

        User user;
        Long count;
        try {
            user = userRepository.findByUsername(username);
        } catch (Exception ex) {
            throw new UnexpectedException("Error getting user details: " + ex.getLocalizedMessage());
        }

        if (user != null) {
            try {
                TradeUnion tu = user.getEmployee().getTradeUnion();
                LocalDate youthDate = LocalDate.now().minusYears(35);
                count = memberRepository.findYouthsByGender(youthDate, gender, tu.getId());

            } catch (Exception ex) {
                throw new UnexpectedException("Error finding members by gender: " + ex.getLocalizedMessage());
            }
        } else {
            throw new NotFoundException("User details not found");
        }

        if (count != null) {

            return count;
        } else {
            throw new NotFoundException("Member statistics not available");
        }
    }

    @Override
    public List<ReportDTO> findByBranch(String username) {
        User user;
        List<ReportDTO> reports;
        try {
            user = userRepository.findByUsername(username);
        } catch (Exception ex) {
            throw new UnexpectedException("Error getting user details: " + ex.getLocalizedMessage());
        }

        if (user != null) {
            try {
                TradeUnion tu = user.getEmployee().getTradeUnion();
                reports = memberRepository.findByBranch(tu.getId());
            } catch (Exception ex) {
                throw new UnexpectedException("Error finding members: " + ex.getLocalizedMessage());
            }
        } else {
            throw new NotFoundException("User details not found");
        }

        if (reports != null) {
            return reports;
        } else {
            throw new NotFoundException("Member details not found");
        }

    }

    @Override
    public List<ReportDTO> findByInstitution(String username) {
        User user;
        List<ReportDTO> reports;
        try {
            user = userRepository.findByUsername(username);
        } catch (Exception ex) {
            throw new UnexpectedException("Error getting user details: " + ex.getLocalizedMessage());
        }

        if (user != null) {
            try {
                TradeUnion tu = user.getEmployee().getTradeUnion();
                reports = stoporderRepository.countByInstitution(tu.getId());
            } catch (Exception ex) {
                throw new NotFoundException("Error retrieving member details: " + ex.getLocalizedMessage());
            }
        } else {
            throw new NotFoundException("User details not found");
        }

        if (reports != null) {
            return reports;
        } else {
            throw new NotFoundException("No details found");
        }
    }


    @Override
    public List<ReportDTO> findBySector(String username) {
        User user;
        List<ReportDTO> reports;
        try {
            user = userRepository.findByUsername(username);
        } catch (Exception ex) {
            throw new UnexpectedException("Error getting user details: " + ex.getLocalizedMessage());
        }

        if (user != null) {
            try {
                TradeUnion tu = user.getEmployee().getTradeUnion();
                reports = memberRepository.findBySector(tu.getId());
            } catch (Exception ex) {
                throw new UnexpectedException("Error retrieving details: " + ex.getLocalizedMessage());
            }
        } else {
            throw new NotFoundException("User details not found");
        }

        if (reports != null) {
            return reports;
        } else {
            throw new NotFoundException("No details found");
        }
    }


    public ReportDTO getByGender(String username) {

        Long male = findByGender("M", username);
        Long female = findByGender("F", username);

        ReportDTO dto = new ReportDTO();
        dto.setMale(male);
        dto.setFemale(female);
        return dto;

    }


    public void viewMemberReport(HttpServletResponse response, Long institutionId, String username) {

        int male = 0, female = 0, total = 0;

        Institution institution = institutionRepository.findById(Long.valueOf(institutionId)).orElseThrow(() ->
                new RuntimeException("Institution with provided id not found"));


        try {
            List<StopOrder> lst = stoporderRepository.getByInstitution(institutionId);
            if (lst != null && !lst.isEmpty()) {
                for (StopOrder s : lst) {
                    if ("M".equals(s.getMember().getGender())) {
                        male += 1;
                    } else if ("F".equals(s.getMember().getGender())) {
                        female += 1;
                    }
                }

                total = male + female;
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("realPath", new ClassPathResource("/images/logo.png").getPath());

                parameters.put("male", male);
                parameters.put("female", female);
                parameters.put("total", total);
                parameters.put("printedby", username);
                parameters.put("institutionName", institution.getSector().getTradeUnion().getTuName());
                parameters.put("heading", "All Members for " + institution.getInstitutionName());

                downloadPDF(response, "/reports/memberRPT.jrxml", parameters, lst);


            } else {
                throw new RuntimeException("There are no records to show");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error downloading report: " + ex.getLocalizedMessage());
        }
    }


    public void printSchedule(HttpServletResponse response, Long institutionId, LocalDate fromDate, LocalDate toDate, String userName) {

        int male = 0, female = 0, total = 0;

        Institution institution = institutionRepository.findById(Long.valueOf(institutionId)).orElseThrow(() ->
                new RuntimeException("Institution with provided id not found"));

        if (fromDate == null || toDate == null) {
            throw new RuntimeException("Enter valid dates");
        } else if (fromDate.compareTo(toDate) > 0) {
            throw new RuntimeException("Enter valid date range");
        } else {
            try {
                List<StopOrder> lstbyC = stoporderRepository.getSchedule(institutionId, fromDate, toDate);
                if (lstbyC != null && !lstbyC.isEmpty()) {
                    for (StopOrder s : lstbyC) {
                        if ("M".equals(s.getMember().getGender())) {
                            male += 1;

                        } else if ("F".equals(s.getMember().getGender())) {
                            female += 1;

                        }
                    }

                    total = male + female;
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("realPath", new ClassPathResource("/images/logo.png").getPath());
                    parameters.put("male", male);
                    parameters.put("female", female);
                    parameters.put("total", total);
                    parameters.put("printedby", userName);
                    parameters.put("institutionName", institution.getSector().getTradeUnion().getTuName());
                    parameters.put("heading", " All Members For " + institution.getInstitutionName() + " Recruited Between " + fromDate.toString() +
                            " And " + toDate.toString());

                    downloadPDF(response, "/reports/SOSchedule.jrxml", parameters, lstbyC);


                } else {
                    throw new RuntimeException("There are no records to show");
                }
            } catch (Exception ex) {
                throw new RuntimeException("Error downloading schedule: " + ex.getLocalizedMessage());
            }
        }
    }


    @Override
    public void printInvoice(HttpServletResponse response, Long institutionId, String userName) {

        int male = 0, female = 0, total = 0;

        Institution institution = institutionRepository.findById(Long.valueOf(institutionId)).orElseThrow(() ->
                new RuntimeException("Institution with provided id not found"));


        try {
            List<StopOrder> lstbyC = stoporderRepository.getInvoice(institutionId);
            if (lstbyC != null && !lstbyC.isEmpty()) {
                for (StopOrder s : lstbyC) {
                    if ("M".equals(s.getMember().getGender())) {
                        male += 1;

                    } else if ("F".equals(s.getMember().getGender())) {
                        female += 1;

                    }
                }

                total = male + female;
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("realPath", new ClassPathResource("/images/logo.png").getPath());
                parameters.put("male", male);
                parameters.put("female", female);
                parameters.put("total", total);
                parameters.put("printedby", userName);
                parameters.put("institutionName", institution.getSector().getTradeUnion().getTuName());
                parameters.put("heading", "Subscriptions for " + institution.getInstitutionName() + " (" + institution.getCurrency().toString() + ") - " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM-YYYY")));

                downloadPDF(response, "/reports/invoice.jrxml", parameters, lstbyC);


            } else {
                throw new RuntimeException("There are no records to show");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error downloading schedule: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public void viewArrearsReportForInstitution(HttpServletResponse response, Long institutionId, String name) {

        Institution institution = institutionRepository.findById(Long.valueOf(institutionId)).orElseThrow(() ->
                new RuntimeException("Institution with provided id not found"));

        List<Member> members = memberRepository.findByInstitution(institutionId);
        if(members==null||members.isEmpty()){
                new RuntimeException("No members found for provided Institution id");
        }



        try {

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("realPath", new ClassPathResource("/images/logo.png").getPath());
                parameters.put("printedby", name);
                parameters.put("institutionName", institution.getSector().getTradeUnion().getTuName());
                parameters.put("heading", "Arears for " + institution.getInstitutionName() + " (" + institution.getCurrency().toString() + ") - " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM-YYYY")));

                downloadPDF(response, "/reports/invoice.jrxml", parameters, members);

        } catch (Exception ex) {
            throw new RuntimeException("Error downloading arrears: " + ex.getLocalizedMessage());
        }
    }


    public javax.servlet.http.HttpServletResponse downloadPDF(HttpServletResponse response, String filename, Map<String, Object> parameters, List<?> lst) {

        try {
            InputStream input = new ClassPathResource(filename).getInputStream();
            JasperDesign jasperDesign = JRXmlLoader.load(input);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(lst));

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=invoice.pdf");

            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error downloading schedule");
        }
    }

    public ResponseEntity<?> generateReport(Long branchId) {
        List<MemberView> list;
        list = stoporderRepository.findByBranch(branchId);
        Branch branch = branchRepository.findById(branchId).orElseThrow(() ->
                new NotFoundException("Branch with given id not found"));

        if (list != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/csv;");
            headers.setContentDispositionFormData("fileName" + ".csv", "fileName" + ".csv");
            return new ResponseEntity<Object>(csvWriter.convertListToByteArray(list, "All clients for " + branch.getBranchName() + " branch"), headers, HttpStatus.OK);
        } else {
            throw new NotFoundException("No members found in current branch");
        }

    }

    @Override
    public ResponseEntity<?> downloadSchedule(LocalDate fromDate, LocalDate toDate, String userName) {
        List<MemberView> list;

        list = stoporderRepository.findRecruitments(fromDate, toDate);

        if (list != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/csv;");
            headers.setContentDispositionFormData("Recruitments" + ".csv", "Recruitments" + ".csv");
            return new ResponseEntity<Object>(csvWriter.convertListToByteArray(list, "All recruitments from " + fromDate + " to " + toDate), headers, HttpStatus.OK);
        } else {
            throw new NotFoundException("No members found in current period");
        }
    }
}
