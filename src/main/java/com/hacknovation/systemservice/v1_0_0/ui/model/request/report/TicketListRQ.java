package com.hacknovation.systemservice.v1_0_0.ui.model.request.report;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class TicketListRQ {
    private int page;
    private int size;
    private String keyword;
    private String filterByDate;
    private String filterByLotteryType;
    private String drawCode;
    private String isWin;
    private String isCancel;
    public TicketListRQ(HttpServletRequest request) {
        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 50;
        this.keyword = request.getParameter("keyword") != null && !request.getParameter("keyword").isEmpty() ? request.getParameter("keyword") : LotteryConstant.ALL;
        this.filterByDate = request.getParameter("filterByDate") != null && !request.getParameter("filterByDate").isEmpty() ? request.getParameter("filterByDate") : "00:00 00:00:00";
        this.drawCode = request.getParameter("drawCode") != null && !request.getParameter("drawCode").isEmpty() ? request.getParameter("drawCode") : LotteryConstant.ALL;
        this.filterByLotteryType = request.getParameter("filterByLotteryType") != null && !request.getParameter("filterByLotteryType").isEmpty() ? request.getParameter("filterByLotteryType") : LotteryConstant.ALL;
        this.isCancel = request.getParameter("isCancel") != null && !request.getParameter("isCancel").isEmpty() ? request.getParameter("isCancel") : "false";
        this.isWin = request.getParameter("isWin") != null && !request.getParameter("isWin").isEmpty() ? request.getParameter("isWin") : "false";
    }

}
