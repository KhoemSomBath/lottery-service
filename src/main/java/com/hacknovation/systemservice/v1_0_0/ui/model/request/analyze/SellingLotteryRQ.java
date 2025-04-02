package com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Data
public class SellingLotteryRQ {

    private int page;
    private int size;
    private String filterByDrawCode;
    private String filterByLotteryType;
    private BigDecimal filterByAmount2D;
    private BigDecimal filterByAmount3D;
    private BigDecimal filterByAmount4D;

    public SellingLotteryRQ(HttpServletRequest request) {
        String filterByDrawCode = request.getParameter("filterByDrawCode");
        String filterByLotteryType = request.getParameter("filterByLotteryType");
        String filterByAmount2D = request.getParameter("filterByAmount2D");
        String filterByAmount3D = request.getParameter("filterByAmount3D");
        String filterByAmount4D = request.getParameter("filterByAmount3D");

        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 50;
        this.filterByDrawCode = filterByDrawCode != null && !filterByDrawCode.isEmpty() ? filterByDrawCode : LotteryConstant.ALL;
        this.filterByLotteryType = filterByLotteryType != null && !filterByLotteryType.isEmpty() ? filterByLotteryType : LotteryConstant.VN1;
        this.filterByAmount2D = filterByAmount2D != null ? BigDecimal.valueOf(Long.parseLong(filterByAmount2D)) : BigDecimal.ZERO;
        this.filterByAmount3D = filterByAmount3D != null ? BigDecimal.valueOf(Long.parseLong(filterByAmount3D)) : BigDecimal.ZERO;
        this.filterByAmount4D = filterByAmount4D != null ? BigDecimal.valueOf(Long.parseLong(filterByAmount4D)) : BigDecimal.ZERO;
    }

}
