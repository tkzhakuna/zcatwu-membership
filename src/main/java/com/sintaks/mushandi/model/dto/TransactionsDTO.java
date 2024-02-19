package com.sintaks.mushandi.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class TransactionsDTO {
    private Long id;
    private LocalDate date;
    private String description;
    private Double amount;
}
