package com.sintaks.mushandi.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor	
@AllArgsConstructor
@Setter
@Table(name="branch")
public class Branch {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@NotEmpty(message="Enter Branch Name")
	@Column(name="branch_name",unique=true)
	private String branchName;
	@Column(name="branch_address")
	private String branchAddress;
	@Email(message="Enter valid email")
	private String email;
	@Column(name="branch_phone")
	private String branchPhone;
	@Column(name="branch_town")
	private String branchTown;
	@ManyToOne
	@NotNull(message="Trade union is required")
	@JoinColumn(name="trade_union")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private TradeUnion tradeUnion;
	
}
