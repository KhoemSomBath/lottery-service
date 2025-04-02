package com.hacknovation.systemservice.v1_0_0.ui.model.request.report;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/*
 * author: kangto
 * createdAt: 07/02/2022
 * time: 09:46
 */
@Data
public class ClearingRQ {
    private String filterByDate;
    private String filterByUserCode;
    private String filterByLotteryType;

    public ClearingRQ(HttpServletRequest request) {
        this.filterByDate = request.getParameter("filterByDate") != null && !request.getParameter("filterByDate").isEmpty() ? request.getParameter("filterByDate") : "00:00 00:00:00";
        this.filterByUserCode = request.getParameter("filterByUserCode") != null && !request.getParameter("filterByUserCode").isEmpty() ? request.getParameter("filterByUserCode") : LotteryConstant.ALL;
        this.filterByLotteryType = request.getParameter("filterByLotteryType") != null && !request.getParameter("filterByLotteryType").isEmpty() ? request.getParameter("filterByLotteryType") : LotteryConstant.VN1;
    }
}
