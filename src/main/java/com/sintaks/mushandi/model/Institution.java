package com.sintaks.mushandi.model;
import javax.persistence.*;

import javax.validation.constraints.Email;
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
@Table(name="institution")
public class Institution {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@NotEmpty(message="Institution Name is required")
	@Column(name="institution_name")
	private String institutionName;
	@Column(name="address")
	private String address;
	@Column(name="phone")
	@NotEmpty(message="Phone number is required")
	private String phone;
	@Email(message="Enter valid email")
	@Column(name="email")
	private String email;
	@OneToOne
	@JoinColumn(name="sector")
	@NotNull(message="Sector is required")
	private Sector sector;
	@Column(name="tu_percentage")
	private Double tuPercentage;
	@OneToOne
	@JoinColumn(name="branch_id")
	private Branch branch;
	@NotNull(message="Currency is required")
	@Enumerated(EnumType.STRING)
	private CurrencyEnum currency;

	@Override
	public String toString() {
		return "Institution{" +
				"id=" + id +
				", institutionName='" + institutionName + '\'' +
				'}';
	}
}
