package com.hacknovation.systemservice.v1_0_0.ui.model.request.probability;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/*
 * author: kangto
 * createdAt: 26/08/2022
 * time: 11:43
 */
@Data
public class ProbabilityListRQ {
    private String lotteryType;
    private String shiftCode;

    public ProbabilityListRQ(HttpServletRequest request) {
        String filterByLotteryType = request.getParameter("filterByLotteryType");
        String filterByShiftCode = request.getParameter("filterByShiftCode");

        lotteryType = filterByLotteryType != null && !filterByLotteryType.isBlank() ? filterByLotteryType: "ALL";
        shiftCode = filterByShiftCode != null && !filterByShiftCode.isBlank() ? filterByShiftCode: "ALL";
    }

}
