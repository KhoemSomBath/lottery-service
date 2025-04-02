package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 15:24
 */
@Data
public class MemberSummaryRS {
    private String userCode;
    private String username;
    private String nickname;
    private String commission;
    private String rebate;
    private String filterByDate;
    private List<MemberRowRS> rows = new ArrayList<>();
    private SummaryLotteryRS summaryLottery = new SummaryLotteryRS();
    private List<SettlementItemRS> settlements = new ArrayList<>();
    private CurrencyRS grandTotal = new CurrencyRS();
    private NoteRS noteRS = new NoteRS();

}
