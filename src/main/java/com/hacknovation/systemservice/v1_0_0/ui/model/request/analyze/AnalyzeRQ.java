package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:29
 */
@Data
public class AnalyzeRQ {
    @NotBlank(message = "Please provide lotteryType")
    private String lotteryType;
    private String analyzeType = "NORMAL";
    @NotBlank(message = "Please provide drawCode")
    private String drawCode;
    private Date drawAt;
    private RebateRQ mainRebate = new RebateRQ();
    private RebateRQ rebateTransfer = new RebateRQ();
    @Size(min = 1, message = "Please provide filterPosts")
    private List<RebateRQ> filterPosts = new ArrayList<>();
    private String postGroup = "POST";
    private String transferType = "percentage";

    public void setDefaultMoney() {
        mainRebate.setDefaultMoney();
    }
}
