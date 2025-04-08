package com.sintaks.mushandi.model;

import java.time.LocalDate;

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
@Table(name="receipt")
public class Receipt {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

	@ManyToOne
	@JoinColumn(name="member")
	private Member member;
	@Transient
	private String nationalId;
	@Column(name="receipt_date")
	@NotNull(message="Receipt date is required")
	private LocalDate receiptDate;
	@Column(name="cycle_no")
	private Integer cycleNumber;
	@Column(name="receipt_number")
	@NotEmpty(message="Receipt number is required")
	private String receiptNumber;
	@Column(name="amount")
	@NotNull(message="Amount is required")
	private Double amount;
	@Column(name="notes")
	@NotEmpty(message="Description is required")
	private String notes;
	@ManyToOne
	@JoinColumn(name="batch")
	private ReceiptBatch receiptBatch;
	@OneToOne
	private User user;
	@NotNull(message="Currency is required")
	@Enumerated(EnumType.STRING)
	private CurrencyEnum currency;
	@ManyToOne
	@JoinColumn(name="rate")
	private ExchangeRate rate;
	
	
	
}
