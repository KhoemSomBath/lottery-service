package com.hacknovation.systemservice.v1_0_0.ui.model.request.betting;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/*
 * author: kangto
 * createdAt: 18/02/2022
 * time: 10:04
 */
@Data
public class BettingListRQ {
    private String lotteryType;
    private String drawCode;
    private String pageNumber;
    private String filterByDate;
    private String filterByUserCode;
    private String isWin;
    private String isCancel;

    public BettingListRQ(HttpServletRequest request) {
        this.drawCode = request.getParameter("drawCode") != null && !request.getParameter("drawCode").isEmpty() ? request.getParameter("drawCode") : "all";
        this.lotteryType = request.getParameter("lotteryType") != null && !request.getParameter("lotteryType").isEmpty() ? request.getParameter("lotteryType") : "VN1";
        this.pageNumber = request.getParameter("pageNumber") != null && !request.getParameter("pageNumber").isEmpty() ? request.getParameter("pageNumber") : "1";
        this.filterByDate = request.getParameter("filterByDate") != null && !request.getParameter("filterByDate").isEmpty() ? request.getParameter("filterByDate") : "0000-00-00";
        this.filterByUserCode = request.getParameter("filterByUserCode") != null && !request.getParameter("filterByUserCode").isEmpty() ? request.getParameter("filterByUserCode") : "ALL";
        this.isWin = request.getParameter("isWin") != null && !request.getParameter("isWin").isEmpty() ? request.getParameter("isWin") : "false";
        this.isCancel = request.getParameter("isCancel") != null && !request.getParameter("isCancel").isEmpty() ? request.getParameter("isCancel") : "false";
    }
}
