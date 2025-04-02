package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class UserReferralRQ {
    private String languageCode;
    private String keyword;
    private String referralUserCode;
    private String status;
    private int page;
    private int size;

    public UserReferralRQ(HttpServletRequest request) {
        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 50;
        this.keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "all";
        this.referralUserCode = request.getParameter("referralUserCode") != null ? request.getParameter("referralUserCode") : "all";
        this.status = request.getParameter("status") != null ? request.getParameter("status") : "all";
        this.languageCode = request.getHeader("X-Localization") != null ? request.getHeader("X-Localization") : "en";
    }

}
