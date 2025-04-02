package com.hacknovation.systemservice.v1_0_0.ui.model.request.result;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

/**
 * author : phokkinnky
 * date : 8/6/21
 */
@Data
public class GetResultRQ {

    private String lotteryType;
    private String drawCode;
    private String filterByDate;

    public GetResultRQ(HttpServletRequest request) {
        String drawCode = request.getParameter("drawCode");
        String filterByDate = request.getParameter("filterByDate");

        this.drawCode = drawCode != null && !drawCode.isEmpty() ? drawCode : "";
        this.filterByDate = filterByDate != null && !filterByDate.isEmpty() ? filterByDate : "";
    }

}
