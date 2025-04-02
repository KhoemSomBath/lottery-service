package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * author: kangto
 * createdAt: 06/04/2023
 * time: 15:56
 */
@Data
public class AnalyzeV2RQ {
    @NotBlank(message = "Please provide lotteryType")
    private String lotteryType;
    @NotBlank(message = "Please provide drawCode")
    private String drawCode;
    private Date drawAt;
    private RebateV2RQ mainRebate = new RebateV2RQ();
    @Size(min = 1, message = "Please provide filterPosts")
    private List<RebateV2RQ> filterPosts = new ArrayList<>();
}
