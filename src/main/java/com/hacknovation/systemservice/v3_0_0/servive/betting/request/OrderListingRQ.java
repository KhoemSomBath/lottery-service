package com.hacknovation.systemservice.v3_0_0.servive.betting.request;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class OrderListingRQ {
    private int page;
    private int size;
    private String keyword;
    private String filterDate;
    private String drawCode;
    private String userCode;
    private String lotteryType;
    private Integer pageNumber;
    private String isWin;

    public OrderListingRQ(HttpServletRequest request) {
        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 10;
        this.keyword = request.getParameter("keyword") != null && !request.getParameter("keyword").isEmpty() ? request.getParameter("keyword") : "all";
        this.isWin = request.getParameter("isWin") != null && !request.getParameter("isWin").isEmpty() ? request.getParameter("isWin") : "all";
        this.filterDate = request.getParameter("filterDate") != null && !request.getParameter("filterDate").isEmpty() ? request.getParameter("filterDate")+" "+"00:03:00" : "00:00 00:00:00";
        this.drawCode = request.getParameter("drawCode") != null && !request.getParameter("drawCode").isEmpty() ? request.getParameter("drawCode") : "all";
        this.lotteryType = request.getParameter("lotteryType") != null && !request.getParameter("lotteryType").isEmpty() ? request.getParameter("lotteryType") : "LEAP";
        String _pageNumber = request.getParameter("pageNumber") != null && !request.getParameter("pageNumber").isEmpty() ? request.getParameter("pageNumber") : "1";
        this.pageNumber = Integer.parseInt(_pageNumber);
        this.userCode = request.getParameter("userCode");
    }

    public OrderListingRQ(BaseBanhJiBettingRQ banhJiBettingRQ) {
        this.drawCode = banhJiBettingRQ.getDrawCode();
        this.userCode = banhJiBettingRQ.getUserCode();
        this.pageNumber = banhJiBettingRQ.getPageNumber();
        this.isWin = "false";
        this.keyword = "false";
    }
}
