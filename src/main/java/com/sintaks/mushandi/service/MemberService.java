package com.sintaks.mushandi.service;


import com.sintaks.mushandi.exceptions.*;
import com.sintaks.mushandi.model.Member;
import com.sintaks.mushandi.model.StopOrder;
import com.sintaks.mushandi.model.TradeUnion;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.model.dto.BatchListDTO;
import com.sintaks.mushandi.model.dto.ReportDTO;
import com.sintaks.mushandi.model.projections.MemberView;
import com.sintaks.mushandi.repository.InstitutionRepository;
import com.sintaks.mushandi.repository.MemberRepository;
import com.sintaks.mushandi.repository.StopOrderRepository;
import com.sintaks.mushandi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberService implements BaseService<Member> {

    private final TradeUnionService tuService;
    private final MemberRepository memberRep;
    private final StopOrderRepository soRep;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public MemberService(TradeUnionService tuService, MemberRepository memberRep, StopOrderRepository soRep,
                         UserRepository userRepository, InstitutionRepository institutionRepository) {

        this.tuService = tuService;
        this.memberRep = memberRep;
        this.soRep = soRep;
        this.userRepository = userRepository;
        this.institutionRepository = institutionRepository;
    }

    @Override
    public List<Member> findAll() {
        try {
            return null;
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error has occurred while saving member: " + ex.getLocalizedMessage());
        }

    }


    public List<MemberView> findAllActive(Long tuId) {
        List<MemberView> list;
        try {
            list = memberRep.activeMemberList(tuId);

        } catch (Exception ex) {
            throw new UnexpectedException("Error Listing stoporders: " + ex.getLocalizedMessage());
        }
        return list;

    }

    @Cacheable(value = "membersCache")
    public Page<Member> findAll(Pageable pageable) {
        return memberRep.findAll(pageable);
    }

    @Override
    @Transactional
    public Member save(@NotNull Member entity, Principal principal) {

        if (entity != null) {
            TradeUnion tu;
            try {
                tu = tuService.findById((long) 1).get();
            } catch (Exception ex) {
                throw new UnexpectedException("Error retrieving trade union information: " + ex.getLocalizedMessage());
            }
            // if updating
            if (entity.getId() != null && !existsById(entity.getId())) {
                throw new NotFoundException("Member id not found");
            }

            if (checkUniqueMember(entity, tu.getId())) {
                throw new AlreadyExistsException("Member national id already registerd");
            }

            try {
                log.info("Saving ===============================");
                entity.getStoporders().stream()
                        .forEach(st -> entity.addStoporder(st));
                Member member = memberRep.save(entity);
                log.info("Finsiehed Saving ===============================");
                return member;
            } catch (Exception ex) {

                throw new NotSavedException("Member not saved: " + ex.getLocalizedMessage());
            }

        } else {
            throw new NotSavedException("Member not saved");
        }
    }

    @Override
    @Transactional
    public Member saveNew(@NotNull Member entity) {

        if (entity != null) {
            TradeUnion tu;
            try {
                tu = tuService.findById((long) 1).get();
            } catch (Exception ex) {
                throw new UnexpectedException("Error retrieving trade union information: " + ex.getLocalizedMessage());
            }
            // if updating
            if (entity.getId() != null && !existsById(entity.getId())) {
                throw new NotFoundException("Member id not found");
            }

            if (checkUniqueMember(entity, tu.getId())) {
                throw new AlreadyExistsException("Member national id already registerd");
            }

            try {
                log.info("Saving ===============================");
                final var institution = institutionRepository.findByInstitutionName("Temp");
                entity.getStoporders().stream()
                        .forEach(st -> {
                            if (Objects.isNull(st.getStopOrderNumber())) {
                                int stopOrderNumber = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmSS")));
                                st.setStopOrderNumber(stopOrderNumber);
                                if (Objects.isNull(st.getInstitution())) {
                                    st.setInstitution(institution);
                                }
                                st.setSource("WEB");
                            }
                            entity.addStoporder(st);
                            entity.setStatus("New");
                        });
                Member member = memberRep.save(entity);
                log.info("Finsiehed Saving ===============================");
                return member;
            } catch (Exception ex) {

                throw new NotSavedException("Member not saved: " + ex.getLocalizedMessage());
            }

        } else {
            throw new NotSavedException("Member not saved");
        }
    }


    @Override
    public Member update(Member member) {
        return null;
    }

    @Override
    public Optional<Member> findById(Long id) {
        try {
            return memberRep.findById(id);
        } catch (Exception ex) {
            throw new NotFoundException("Member not found: " + ex.getLocalizedMessage());
        }
    }


    public boolean existsById(Long id) {
        try {
            return memberRep.existsById(id);
        } catch (Exception ex) {
            throw new UnexpectedException("Error fetching member: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            memberRep.deleteById(id);
            return true;
        } catch (Exception ex) {
            throw new NotDeletedException("Member deletion failed: " + ex.getLocalizedMessage());
        }

    }

    boolean checkUniqueMember(Member member, Long tuId) {
        // check if name already exists before saving new
        try {
            if (member.getId() == null && memberRep.checkUniqueNationalId(member.getNationalId(), tuId) > 0) {

                return true;
            }
            // check if name already exists before updating
            if (member.getId() != null) {
                if (memberRep.findByNationalIdAndTU(member.getNationalId(), member.getId(), tuId) > 0)
                    return true;
            }
        } catch (Exception ex) {
            throw new UnexpectedException("Error checking member: " + ex.getLocalizedMessage());
        }
        return false;
    }


    public List<StopOrder> findByInstitution(Long institutionId) {
        List<StopOrder> stoporders;
        try {
            stoporders = soRep.findByInstitution(institutionId);
        } catch (Exception ex) {
            throw new UnexpectedException("Error retrieving stoporder: " + ex.getLocalizedMessage());
        }

        if (stoporders == null) {

        }
        return stoporders;
    }

    public List<BatchListDTO> batchList(Long institutionId) {
        List<StopOrder> stoporders;
        try {
            stoporders = findByInstitution(institutionId);
        } catch (Exception ex) {
            throw new UnexpectedException("Error retrieving institution information: " + ex.getLocalizedMessage());
        }

        List<BatchListDTO> batchList = stoporders.stream().map(s -> {
            BatchListDTO dto = new BatchListDTO();
            dto.setId(s.getMember().getId());
            Double basic = s.getMember().getGrade().getBasicWage();
            Double amount = s.getInstitution().getTuPercentage() != null ?
                    s.getInstitution().getTuPercentage() / 100 * basic :
                    s.getMember().getGrade().getSector().getTradeUnion().getTuPercentage() / 100 * basic;

            dto.setAmount(BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).doubleValue());
            dto.setSurname(s.getMember().getSurname());
            dto.setFirstname(s.getMember().getFirstname());

            return dto;
        }).collect(Collectors.toList());

        return batchList;
    }

    /**
     * This code needs to be refactored and written properly
     */

    public HttpServletResponse viewAll(HttpServletResponse response, String username) {
        User user = userRepository.findByUsername(username);

        List<ReportDTO> members = soRep.listAll(user.getEmployee().getTradeUnion().getId());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("abbreviation", user.getEmployee().getTradeUnion().getAbbreviation());
        parameters.put("tradeunion", user.getEmployee().getTradeUnion().getTuName());
        parameters.put("printedby", user.getFullName());
        parameters.put("heading", "All Members");
        parameters.put("realPath", new ClassPathResource("/images/logo.png").getPath());

        try {
            return downloadPDF(response, "/reports/membership_report.jrxml", parameters, members);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public HttpServletResponse viewByBranch(HttpServletResponse response, String username) {
        User user = userRepository.findByUsername(username);
        LocalDate youthDate = LocalDate.now().minusYears(35);
        List<ReportDTO> members = soRep.ListByBranch(youthDate, user.getEmployee().getTradeUnion().getId());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("abbreviation", user.getEmployee().getTradeUnion().getAbbreviation());
        parameters.put("tradeunion", user.getEmployee().getTradeUnion().getTuName());
        parameters.put("printedby", user.getFullName());
        parameters.put("heading", "All Youths");
        parameters.put("realPath", new ClassPathResource("/images/logo.png").getPath());

        try {
            return downloadPDF(response, "/reports/membership_report.jrxml", parameters, members);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
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


    public Long getArrearsStats(Long tuId, Boolean inArrears) {
        TradeUnion tu = tuService.getOne(tuId);
        if (inArrears) {

            Long arrears = memberRep.findByStatusAndTradeUnionId("Active", tuId).stream()
                    .filter(member -> member.getArrears() > member.getGrade().getBasicWage() * tu.getTuPercentage() / 100)
                    .count();

            return arrears;
        } else {

            Long notInArrears = memberRep.findByStatusAndTradeUnionId("Active", tuId).stream()
                    .filter(member -> member.getArrears() < member.getGrade().getBasicWage() * tu.getTuPercentage() / 100)
                    .count();

            return notInArrears;
        }
    }


}
