package com.hacknovation.systemservice.v1_0_0.ui.model.request.config;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class RebateListRQ {
    private int page;
    private int size;
    private String lotteryType;

    public RebateListRQ(HttpServletRequest request) {
        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 50;
        this.lotteryType = request.getParameter("lotteryType") != null && !request.getParameter("lotteryType").isEmpty() ? request.getParameter("lotteryType") : "ALL";
    }
}
