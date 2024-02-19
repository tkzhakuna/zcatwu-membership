package com.sintaks.mushandi.model.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sintaks.mushandi.model.CurrencyEnum;
import lombok.Data;
@Data
@Entity
public class ReportDTO {
@Id
private Integer id;
private String label;
private Long counts;
private Double amount;
private Long male;
private Long female;
private String fullname;
private String nationalid;
private String grade;
private String institution;
private String gender;
private CurrencyEnum currency;

public ReportDTO() {
}

public ReportDTO(String label, Double amount) {
	
	this.label = label;
	this.amount = amount;
}


public ReportDTO(String label, Long counts) {
	
	this.label = label;
	this.counts = counts;
}


public ReportDTO( Long male, Long female) {

	this.male = male;
	this.female = female;
}

public ReportDTO(String fullname, String nationalid, String grade, String institution,CurrencyEnum currency, String gender) {
	super();
	this.fullname = fullname;
	this.nationalid = nationalid;
	this.grade = grade;
	this.institution = institution;
	this.gender = gender;
	this.currency=currency;
}




}
