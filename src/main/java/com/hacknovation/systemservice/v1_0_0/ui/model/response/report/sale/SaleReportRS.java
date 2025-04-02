package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.HasLotteryRS;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleReportRS {
    private String lotteryType;
    private String roleCode;
    private String username = LotteryConstant.ALL;
    private String nickname = LotteryConstant.ALL;
    private String date;
    private HasLotteryRS hasLottery;
    private Boolean isShowHasLottery = false;
    private SummaryReportRS summary = new SummaryReportRS();
    private List<SaleItemsRS> items = new ArrayList<>();

    public Boolean getIsShow1DKhr() {
        return this.summary.getCom1DKhr().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow1DUsd() {
        return this.summary.getCom1DUsd().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow4DKhr() {
        return this.summary.getCom4DKhr().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShow4DUsd() {
        return this.summary.getCom4DUsd().compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean getIsShowUsd() {
        return this.summary.getTotalAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.summary.getWinLoseAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.summary.getOldAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.summary.getGiveAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.summary.getBorrowAmountUsd().compareTo(BigDecimal.ZERO) != 0 ||
                this.summary.getProtestAmountUsd().compareTo(BigDecimal.ZERO) != 0;
    }
}
