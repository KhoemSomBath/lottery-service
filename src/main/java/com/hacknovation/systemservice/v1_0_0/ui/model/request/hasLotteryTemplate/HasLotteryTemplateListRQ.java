package com.hacknovation.systemservice.v1_0_0.ui.model.request.hasLotteryTemplate;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/*
 * author: kangto
 * createdAt: 30/11/2022
 * time: 11:58
 */
@Data
public class HasLotteryTemplateListRQ {
    private String filterByLotteryType;
    private String filterByDayOfWeek;
    private String filterByUserCode;

    public HasLotteryTemplateListRQ(HttpServletRequest request) {
        this.filterByLotteryType = getValueRQ(request.getParameter("filterByLotteryType"));
        this.filterByDayOfWeek = getValueRQ(request.getParameter("filterByDayOfWeek"));
        this.filterByUserCode = getValueRQ(request.getParameter("filterByUserCode"));
    }

    private String getValueRQ(String value) {
        return value != null && !value.isBlank() ? value : "ALL";
    }
}
