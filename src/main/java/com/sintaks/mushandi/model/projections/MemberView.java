package com.sintaks.mushandi.model.projections;

import java.time.LocalDate;

public interface MemberView {
    Long getId();
    String getSurname();
    String getFirstname();
    String getGender();
    String getNationalId();
    String getCellNumber();
    String getEmail();
    String getTown();
    String getGrade();
    String getStatus();
    String getInstitution();
    String getWeeklyDeduction();
    LocalDate getDateRecruited();
    LocalDate getDob();
    String getBranch();

}
