package com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/*
 * author: kangto
 * createdAt: 14/02/2022
 * time: 09:47
 */
@Data
public class ListUserHasPostponeRQ {
    private String filterByLotteryType;
    private String filterByDrawCode;
    private String filterByUserCode;

    public ListUserHasPostponeRQ(HttpServletRequest request) {
        String lotteryType = request.getParameter("filterByLotteryType");
        String userCode = request.getParameter("filterByUserCode");
        this.filterByLotteryType = lotteryType != null && !lotteryType.isEmpty() ? lotteryType : LotteryConstant.VN1;
        this.filterByUserCode = userCode;
        this.filterByDrawCode = request.getParameter("filterByDrawCode");
    }
}
