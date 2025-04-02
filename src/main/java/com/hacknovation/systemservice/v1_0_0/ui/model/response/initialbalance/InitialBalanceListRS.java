package com.hacknovation.systemservice.v1_0_0.ui.model.response.initialbalance;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementSummaryRS;
import lombok.Data;

/**
 * @author KHOEM Sombath
 * Date: 6/23/2021
 * Time: 5:24 PM
 */

@Data
public class InitialBalanceListRS {
    private String userCode;
    private String nickname;
    private String username;
    private SettlementSummaryRS leap;
    private SettlementSummaryRS mt;
    private SettlementSummaryRS vn1;
    private SettlementSummaryRS kh;
    private SettlementSummaryRS tn;
    private SettlementSummaryRS sc;
    private SettlementSummaryRS total = new SettlementSummaryRS();
}
