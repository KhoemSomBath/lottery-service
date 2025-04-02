package com.hacknovation.systemservice.v1_0_0.ui.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Sombath
 * create at 11/5/22 3:16 PM
 */

@Data
public class TicketTotalDTO {

    private BigDecimal betAmountKhr = BigDecimal.ZERO;
    private BigDecimal waterAmountKhr = BigDecimal.ZERO;
    private BigDecimal winAmountKhr = BigDecimal.ZERO;
    private BigDecimal winLoseAmountKhr = BigDecimal.ZERO;

    private BigDecimal betAmountUsd = BigDecimal.ZERO;
    private BigDecimal waterAmountUsd = BigDecimal.ZERO;
    private BigDecimal winAmountUsd = BigDecimal.ZERO;
    private BigDecimal winLoseAmountUsd = BigDecimal.ZERO;

}
