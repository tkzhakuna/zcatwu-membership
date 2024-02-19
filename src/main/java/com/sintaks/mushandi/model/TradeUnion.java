package com.sintaks.mushandi.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor	
@AllArgsConstructor
@Setter
@Table(name="trade_union")
public class TradeUnion{


@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="id")
private Long id;
@NotEmpty(message="Trade Union name is required")
@Column(name="tu_name",unique=true)
@Size(min = 5, max = 255, message="Trade union name should have a length between 5 and 255 characters")
private String tuName;
@NotEmpty(message="Trade Union abbreviation is required")
@Column(name="abbreviation",unique=true)
@Size(min = 3, max = 20)
private String abbreviation;
@Column(name="address")
@NotEmpty(message="Trade Union address is required")
private String address;
@Column(name="phone")
@NotEmpty(message="Trade Union phone is required")
private String phone;
@Column(name="logo")
private byte[] logo;
@Column(name="tu_percentage")
@NotNull(message="TU Percentage is required")
private Double tuPercentage;
@Enumerated(EnumType.STRING)
private CurrencyEnum baseCurrency;

}
