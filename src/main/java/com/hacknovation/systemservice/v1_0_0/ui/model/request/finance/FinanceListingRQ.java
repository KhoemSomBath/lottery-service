
package com.hacknovation.systemservice.v1_0_0.ui.model.request.finance;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class FinanceListingRQ {
    private String languageCode;
    private String keyword;
    private String transactionType;
    private int page;
    private int size;

    public FinanceListingRQ(HttpServletRequest request) {
        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 50;
        this.keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "all";
        this.transactionType = request.getParameter("transactionType") != null ? request.getParameter("transactionType") : "ALL";
        this.languageCode = request.getHeader("X-Localization") != null ? request.getHeader("X-Localization") : "en";
    }
}
