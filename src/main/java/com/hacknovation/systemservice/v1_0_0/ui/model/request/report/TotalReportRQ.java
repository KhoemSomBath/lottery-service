package com.hacknovation.systemservice.v1_0_0.ui.model.request.report;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class TotalReportRQ {

    private int page;
    private int size;
    private String filterByStartDate;
    private String filterByEndDate;
    private String filterByLevel;
    private String filterByUserCode;
    private String filterByLotteryType;
    private String filterByReportType;
    private String filterByOnSale;
    private String filterByTwoDRebateRate;
    private String filterByThreeDRebateRate;
    private String filterByUsername;

    public TotalReportRQ(HttpServletRequest request) {
        String filterByStartDate = request.getParameter("startDate");
        String filterByEndDate = request.getParameter("endDate");
        String filterByLevel = request.getParameter("filterByLevel");
        String filterByUserCode = request.getParameter("filterByUserCode");
        String filterByLotteryType = request.getParameter("filterByLotteryType");
        String filterByReportType = request.getParameter("filterByReportType");
        String filterByOnSale = request.getParameter("filterByOnSale");
        String filterByTwoDRebateRate = request.getParameter("filterByTwoDRebateRate");
        String filterByThreeDRebateRate = request.getParameter("filterByThreeDRebateRate");
        if (filterByStartDate == null)
            filterByStartDate = request.getParameter("filterByDate");

        String filterByUsername = request.getParameter("filterByUsername");

        this.page = request.getParameter("page") != null && !request.getParameter("page").isEmpty() ? ( Integer.parseInt(request.getParameter("page") ) - 1 ) : 0;
        this.size = request.getParameter("size") != null && !request.getParameter("size").isEmpty() ? Integer.parseInt(request.getParameter("size")) : 100;
        this.filterByLevel = filterByLevel != null && !filterByLevel.isEmpty() ? filterByLevel : LotteryConstant.ALL;
        this.filterByUserCode = filterByUserCode != null && !filterByUserCode.isEmpty() ? filterByUserCode : LotteryConstant.ALL;
        this.filterByLotteryType = filterByLotteryType != null && !filterByLotteryType.isEmpty() ? filterByLotteryType : LotteryConstant.VN1;
        this.filterByReportType = filterByReportType != null && !filterByReportType.isEmpty() ? filterByReportType : LotteryConstant.ONE;
        this.filterByStartDate = filterByStartDate != null && !filterByStartDate.isEmpty() ? filterByStartDate : "2023-01-01";
        this.filterByEndDate = filterByEndDate != null && !filterByEndDate.isEmpty() ? filterByEndDate : filterByStartDate;
        this.filterByOnSale = filterByOnSale != null && !filterByOnSale.isEmpty() ? filterByOnSale : LotteryConstant.NO;
        this.filterByTwoDRebateRate = filterByTwoDRebateRate != null ? filterByTwoDRebateRate : "";
        this.filterByThreeDRebateRate = filterByThreeDRebateRate != null ? filterByThreeDRebateRate : "";
        this.filterByUsername = filterByUsername != null && !filterByUsername.isEmpty() ? filterByUsername : LotteryConstant.ALL;
    }

}
