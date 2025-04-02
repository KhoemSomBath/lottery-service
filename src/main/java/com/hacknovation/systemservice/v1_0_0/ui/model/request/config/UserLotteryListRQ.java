package com.hacknovation.systemservice.v1_0_0.ui.model.request.config;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class UserLotteryListRQ {
    private int page;
    private int size;
    private String lotteryType;
    private String userCode;

    public UserLotteryListRQ(HttpServletRequest request) {
        String lotteryType = request.getParameter("lotteryType");
        String userCode = request.getParameter("userCode");
        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 50;
        this.lotteryType = lotteryType != null && !lotteryType.isEmpty() ? lotteryType : "all";
        this.userCode = userCode != null && !userCode.isEmpty() ? userCode : "ALL";
    }
}
