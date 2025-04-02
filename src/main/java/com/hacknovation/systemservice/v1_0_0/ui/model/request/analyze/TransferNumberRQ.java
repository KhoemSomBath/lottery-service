package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class TransferNumberRQ {

    private int page;
    private int size;
    private String filterByDrawCode;

    public TransferNumberRQ(HttpServletRequest request) {
        String filterByDrawCode = request.getParameter("filterByDrawCode");
        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 50;
        this.filterByDrawCode = filterByDrawCode != null && !filterByDrawCode.isEmpty() ? filterByDrawCode : LotteryConstant.ALL;
    }

}
