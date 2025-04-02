package com.hacknovation.systemservice.v1_0_0.ui.model.request.initialbalance;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author KHOEM Sombath
 * Date: 6/8/2021
 * Time: 10:43 AM
 */

@Data
public class EditInitialBalanceRQ {
    private String userCode;
    private Long itemId;
    @NotNull
    private BigDecimal amountKhr;
    @NotNull
    private BigDecimal amountUsd;

    private String date;
    private String type;
}
