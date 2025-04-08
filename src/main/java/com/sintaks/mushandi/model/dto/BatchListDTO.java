package com.sintaks.mushandi.model.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
@Data
@Entity
public class BatchListDTO {
@Id
private Long id;
private Double amount;
private String surname;
private String firstname;

}
