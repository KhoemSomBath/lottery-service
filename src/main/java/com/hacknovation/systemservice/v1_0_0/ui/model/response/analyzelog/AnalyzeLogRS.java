package com.hacknovation.systemservice.v1_0_0.ui.model.response.analyzelog;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Sombath
 * create at 2/9/22 2:29 PM
 */

@Data
public class AnalyzeLogRS {

    private List<AnalyseLogItemRS> items = new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal totalRewardAmount = BigDecimal.ZERO;
    private BigDecimal winLoseAmount = BigDecimal.ZERO;
    private Boolean isNight = Boolean.FALSE;
    private String drawCode;
    private Date drawAt;

    public void addTotalPostAmount(BigDecimal amount) {
        totalAmount = totalAmount.add(amount);
    }

    public void addTotalRewardAmount(BigDecimal amount) {
        totalRewardAmount = totalRewardAmount.add(amount);
    }

    public void addWinLoseAmount(BigDecimal amount) {
        winLoseAmount = winLoseAmount.add(amount);
    }
}
