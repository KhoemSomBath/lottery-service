package com.hacknovation.systemservice.v1_0_0.ui.model.request.settlement;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class EditSettlementRQ {
    @NotBlank(message = "please provide a lottery type")
    private String lotteryType;
    private String userCode;
    private Long itemId;
    @NotNull
    private BigDecimal amountKhr;
    @NotNull
    private BigDecimal amountUsd;

    private String date;
    private String type;
}
