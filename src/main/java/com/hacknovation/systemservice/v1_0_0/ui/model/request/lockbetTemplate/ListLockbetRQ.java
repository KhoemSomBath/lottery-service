package com.hacknovation.systemservice.v1_0_0.ui.model.request.lockbetTemplate;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 15:00
 */
@Data
public class ListLockbetRQ {
    private String filterByLotteryType;
    private String filterByShiftCode;
    private String filterByUserCode;

    public ListLockbetRQ(HttpServletRequest request) {
        this.filterByLotteryType = request.getParameter("filterByLotteryType");
        this.filterByShiftCode = request.getParameter("filterByShiftCode");
        this.filterByUserCode = request.getParameter("filterByUserCode");
    }
}
