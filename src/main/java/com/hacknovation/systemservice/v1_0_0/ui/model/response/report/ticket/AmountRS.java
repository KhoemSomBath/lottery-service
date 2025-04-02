package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.ticket;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.TicketTotalDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountRS {
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal waterAmount = BigDecimal.ZERO;
    private BigDecimal winAmount = BigDecimal.ZERO;
    private BigDecimal winLoseAmount = BigDecimal.ZERO;

    public AmountRS() {
    }

    public AmountRS(TicketTotalDTO ticketTotalDTO, String currency) {
        if (LotteryConstant.CURRENCY_KHR.equalsIgnoreCase(currency)) {
            this.betAmount = ticketTotalDTO.getBetAmountKhr();
            this.waterAmount = ticketTotalDTO.getWaterAmountKhr();
            this.winAmount = ticketTotalDTO.getWinAmountKhr();
            this.winLoseAmount = ticketTotalDTO.getWinLoseAmountKhr();
        } else {
            this.betAmount = ticketTotalDTO.getBetAmountUsd();
            this.waterAmount = ticketTotalDTO.getWaterAmountUsd();
            this.winAmount = ticketTotalDTO.getWinAmountUsd();
            this.winLoseAmount = ticketTotalDTO.getWinLoseAmountUsd();
        }
    }

    public void addFromAmountRS(AmountRS amountRS) {
        this.betAmount = this.betAmount.add(amountRS.getBetAmount());
        this.waterAmount = this.waterAmount.add(amountRS.getWaterAmount());
        this.winAmount = this.winAmount.add(amountRS.getWinAmount());
        this.winLoseAmount = this.winLoseAmount.add(amountRS.getWinLoseAmount());
    }
}
