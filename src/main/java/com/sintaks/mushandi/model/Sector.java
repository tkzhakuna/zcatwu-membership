package com.sintaks.mushandi.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

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
@Table(name="sector")

public class Sector {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	@Column(name="sector_code",unique=true)
	@NotEmpty(message="Sector Code is required")
	private String sectorCode;
	@NotEmpty(message="Enter Name of Sector")
	@Column(name="sector_name")
	public String sectorName;
	@ManyToOne
	@JoinColumn(name="trade_union")
	
	private TradeUnion tradeUnion;
}
