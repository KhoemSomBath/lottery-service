package com.hacknovation.systemservice.v1_0_0.ui.model.request.page;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class PagingListRQ {

    private String lotteryType;
    private String drawCode;
    private String filterByDate;
    private String filterByUserCode;

    public PagingListRQ(HttpServletRequest request) {
        this.lotteryType = request.getParameter("lotteryType");
        this.drawCode = request.getParameter("drawCode");
        this.filterByDate = request.getParameter("filterByDate");
        this.filterByUserCode = request.getParameter("filterByUserCode");
    }
}
