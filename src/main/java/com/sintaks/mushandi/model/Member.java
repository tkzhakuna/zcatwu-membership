package com.sintaks.mushandi.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.model.dto.ReceiptStatementDTO;
import com.sintaks.mushandi.model.dto.TransactionsDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Table(name="member")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@NotEmpty(message="Firstname is required")
	@Column(name="firstname")
	private String firstname;
	@Column(name="surname")
	@NotEmpty(message="Surname is required")
	private String surname;
	@Column(name="national_id")
	//should be unique in 1 trade union
	@NotEmpty(message="National Id cannot be empty")
	private String nationalId;
	@Column(name="gender")
	private String gender;
	@Column(name="marital_status")
	private String maritalStatus;
	@Column(name="dob")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	@Column(name="cell_number")
	private String cellNumber;
	@Email(message="Enter valid email")
	@Column(name="email")
	private String email;
	@Column(name="town")
	private String town;
	@Column(name="next_of_kin")
	private String nextOfKin;
	@Column(name="nok_phone")
	private String nokPhone;
	@Column(name="relationship")
	private String relationship;
	@ManyToOne
	@JoinColumn(name="grade")
	@NotNull(message="Select grade")
	private Grade grade;
	@Column(name="job_title")
    private String jobTitle;
    @ManyToOne
    @JoinColumn(name="current_branch")
    private Branch currentBranch;
    private String status;
    @Transient
    private double arrears;
    @Transient
	ReceiptStatementDTO statement;
    @Transient
	private int age;
    @JsonIgnore
    @OneToMany(mappedBy="member",fetch=FetchType.LAZY)
    private List<Receipt>receipts=new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy="member",fetch=FetchType.LAZY)
    private List<Bill>bills=new ArrayList<>();
    @CreatedDate
    private LocalDate createdAt;
    @OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="member", orphanRemoval = true)
    private List<StopOrder>stoporders = new ArrayList<>();
    

    public double getArrears() {
    	receipts.stream().filter(receipt -> receipt.getAmount() != null);
    	bills.stream().filter(bill -> bill.getAmount() != null);
    	
    	Double rec = receipts.stream().mapToDouble(Receipt::getAmount).sum();
    	Double bl=bills.stream().mapToDouble(Bill::getAmount).sum();
    	
    	return bl-rec;
    	
    }

	public int getAge() {
    	try {
			return LocalDate.now().getYear() - dob.getYear();
		}catch(Exception ex){return 0;}
	}

	public ReceiptStatementDTO getStatement() {

		ReceiptStatementDTO dto=new ReceiptStatementDTO();

			List<TransactionsDTO>transactions;
			transactions=getReceipts().stream().map(receipt->{
				TransactionsDTO transDto=new TransactionsDTO();
				transDto.setAmount(0-receipt.getAmount());
				transDto.setDate(receipt.getReceiptDate());
				transDto.setDescription(receipt.getNotes());
				transDto.setId(receipt.getId());
				return transDto;
			}).collect(Collectors.toList());



			transactions.addAll(getBills().stream().map(bill->{
				TransactionsDTO transDto=new TransactionsDTO();
				transDto.setAmount(bill.getAmount());
				transDto.setDate(bill.getBillDate());
				transDto.setDescription(bill.getNotes());
				transDto.setId(bill.getId());
				return transDto;
			}).collect(Collectors.toList()));


			dto.setTransactions(transactions);

		return dto;
		}

public void addStoporder(final StopOrder stopOrder){
    	stopOrder.setMember(this);
}


}
