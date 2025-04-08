package com.sintaks.mushandi.model;
import javax.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor	
@AllArgsConstructor
@Setter
@Table(name="grade")
public class Grade {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name="grade_code",unique=true)
	@NotEmpty(message="Grade Code is required")
	private String gradeCode;
	@NotEmpty(message="Enter Grade Name")
	@Column(name="grade_name")
	private String gradeName;
	@OneToOne
	@JoinColumn(name="sector")
	@NotNull(message="Sector is required")
	private Sector sector;
	@Column(name="weekly_wage")
	private Double weeklyWage;
	@Column(name="fortnightly_wage")
	private Double fortnightlyWage;
	@Column(name="basic_wage")
	@NotNull(message="Basic Wage is required")
	private Double basicWage;
	@NotNull(message="Currency is required")
	@Enumerated(EnumType.STRING)
	private CurrencyEnum currency;
}
