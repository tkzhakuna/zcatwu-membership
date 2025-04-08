package com.sintaks.mushandi.model;

import java.time.LocalDate;

import javax.persistence.*;
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
@Table(name="billing")
public class Bill {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@ManyToOne
	@JoinColumn(name="member")
	private Member member;
	@Transient
	private String nationalId;
	@Column(name="bill_date")
	@NotNull(message="Bill date is required")
	private LocalDate billDate;
	@Column(name="cycle_no")
	private Integer cycleNumber;
	@Column(name="amount")
	@NotNull(message="Amount is required")
	private Double amount;
	@Column(name="notes")
	@NotNull(message="Description is required")
	private String notes;
	@OneToOne
	private User user;
	@NotNull(message="Currency is required")
	@Enumerated(EnumType.STRING)
	private CurrencyEnum currency;
	
	
}
