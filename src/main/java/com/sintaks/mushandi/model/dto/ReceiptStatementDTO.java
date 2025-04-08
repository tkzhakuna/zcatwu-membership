package com.sintaks.mushandi.model.dto;

import com.sintaks.mushandi.model.Receipt;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReceiptStatementDTO {

    private List<TransactionsDTO> transactions=new ArrayList<>();

}
