package com.hacknovation.systemservice.v1_0_0.ui.model.request.report;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/*
 * author: kangto
 * createdAt: 05/03/2023
 * time: 14:43
 */
@Data
public class SettlementRQ {
    private String filterByStartDate;
    private String filterByEndDate;
    private String filterByLevel;
    private String filterByUserCode;
    private String filterByLotteryType;
    private String filterByUsername;

    public SettlementRQ(HttpServletRequest request) {
        String filterByStartDate = request.getParameter("startDate");
        String filterByEndDate = request.getParameter("endDate");
        String filterByLevel = request.getParameter("filterByLevel");
        String filterByUserCode = request.getParameter("filterByUserCode");
        String filterByLotteryType = request.getParameter("filterByLotteryType");
        if (filterByStartDate == null)
            filterByStartDate = request.getParameter("filterByDate");

        String filterByUsername = request.getParameter("filterByUsername");
        this.filterByLevel = filterByLevel != null && !filterByLevel.isEmpty() ? filterByLevel : LotteryConstant.ALL;
        this.filterByUserCode = filterByUserCode != null && !filterByUserCode.isEmpty() ? filterByUserCode : LotteryConstant.ALL;
        this.filterByLotteryType = filterByLotteryType != null && !filterByLotteryType.isEmpty() ? filterByLotteryType : LotteryConstant.VN1;
        this.filterByStartDate = filterByStartDate != null && !filterByStartDate.isEmpty() ? filterByStartDate : "2020-01-01";
        this.filterByEndDate = filterByEndDate != null && !filterByEndDate.isEmpty() ? filterByEndDate : filterByStartDate;
        this.filterByUsername = filterByUsername != null && !filterByUsername.isEmpty() ? filterByUsername : LotteryConstant.ALL;
    }
}
