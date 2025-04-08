package com.sintaks.mushandi.model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor	
@AllArgsConstructor
@Setter

@Table(name="receipt_batch")
public class ReceiptBatch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	//@JsonFormat(pattern="yyyy-MM-dd", timezone="CAT")
	@Column(name="batch_date")
	private LocalDate batchDate;
	@NotNull(message="Cycle Date is required")
	@Column(name="cycle_date")
	private LocalDate cycleDate;
	@Column(name="cycle_number")
	private Integer cycleNumber;
	@NotNull(message="Amount is required")
	@Column(name="total_amount")
	private Double totalAmount;
	@Column(name="receipt_number")
	private String receiptNumber;
	private String description;
	@NotNull(message="Number of males paid for is required")
	private Integer male;
	@NotNull(message="Number of females paid for is required")
	private Integer female;
	@NotNull(message="Currency is required")
	@Enumerated(EnumType.STRING)
	private CurrencyEnum currency;
	@ManyToOne
	@JoinColumn(name="rate")
	private ExchangeRate rate;
	@JsonIgnore
	private Integer members;
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="receiptBatch")
	private List<Receipt>receiptList=new ArrayList<>();
	@Transient
	private Boolean uploaded;
	
	@OneToOne
	@JoinColumn(name="institution")
	private Institution institution;
	@JsonIgnore
	@OneToOne
	private User user;
	
	@PrePersist
	protected void onSart() {
		this.batchDate=LocalDate.now();
	}

@PreUpdate
protected void onUpdate() {
	members=male+female;

}

	public Integer getCycleNumber() {
		int cycleNo=0;
		if(cycleDate!=null) {
			int year=cycleDate.getYear();
			int month=cycleDate.getMonthValue();
			String cycle=""+month+year;
			try {
				cycleNo=Integer.parseInt(cycle);
			}catch(Exception ex){
				return cycleNo;
			}
		}
		return cycleNo;
	}
	
	public Boolean getUploaded() {
		return this.getReceiptList().size()>0;
	}
}
