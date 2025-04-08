package com.sintaks.mushandi.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IdDateDTO {
   private Long id;
   private LocalDate fromDate;
   private LocalDate toDate;
}
