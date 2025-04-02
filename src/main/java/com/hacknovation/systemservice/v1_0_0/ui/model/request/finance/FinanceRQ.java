package com.hacknovation.systemservice.v1_0_0.ui.model.request.finance;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class FinanceRQ {
    private Long userId;
    private String proceedByUserCode;
    @NotEmpty(message = "Please provide a user code for proceed")
    private String proceedForUserCode;
    private String proceederCode;

    @NotEmpty(message = "Please provide a user transaction type")
    private String transactionType;

    @Min(1)
    @NotNull(message = "Please provide a amount")
    private BigDecimal amount;
    private String type;
    private String currencyCode;
    private String remark;
}