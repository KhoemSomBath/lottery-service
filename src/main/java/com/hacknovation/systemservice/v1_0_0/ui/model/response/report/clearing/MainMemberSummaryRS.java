package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 08/02/2022
 * time: 00:22
 */
@Data
public class MainMemberSummaryRS {
    private MemberSummaryRS upperLine;
    private MemberSummaryRS underLine;
}
