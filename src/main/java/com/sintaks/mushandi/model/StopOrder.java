package com.sintaks.mushandi.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "stop_order")
public class StopOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "stop_order_number")
    @NotNull(message = "Stoporder number is required")
    private Integer stopOrderNumber;
    @ManyToOne
    @JsonIgnoreProperties({"stoporders"})
    @JoinColumn(name = "member")
    private Member member;
    @Column(name = "date_employed")
    private LocalDate dateEmployed;
    @Column(name = "date_joined")
    private LocalDate dateJoined;
    @OneToOne
    @JoinColumn(name = "recruitment_branch")
    private Branch recruitmentBranch;
    @ManyToOne
    @JoinColumn(name = "institution")
    private Institution institution;
    @Column(name = "company")
    private String company;
    @Column(name = "date_terminated")
    private LocalDate dateTerminated;
    @ManyToOne
    @JoinColumn(name = "site")
    @NotNull(message = "Select Site")
    private Site site;
    @Column(name = "source")
    private String source;

    @Transient
    private double weeklySubs;
    @Transient
    private double monthlySubs;

    public double getWeeklySubs() {
        try {
            return BigDecimal.valueOf(member.getGrade().getWeeklyWage() * ((institution.getTuPercentage() != null && institution.getTuPercentage() > 0) ? institution.getTuPercentage() :
                    institution.getSector().getTradeUnion().getTuPercentage()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception ex) {
            log.error("Error getting weekly subs " + ex.getLocalizedMessage());
        }

        return 0;
    }

    public double getMonthlySubs() {
        try {

            return BigDecimal.valueOf(member.getGrade().getBasicWage() * ((institution.getTuPercentage() != null && institution.getTuPercentage() > 0) ? institution.getTuPercentage() :
                    institution.getSector().getTradeUnion().getTuPercentage()) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception ex) {
            log.error("Error getting monthly subs " + ex.getLocalizedMessage());
        }
        return 0;
    }
}
