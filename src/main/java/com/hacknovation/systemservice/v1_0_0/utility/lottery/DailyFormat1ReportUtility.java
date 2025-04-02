package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.MainSaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleReportRS;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 15/02/2022
 * time: 23:48
 */
@Component
@RequiredArgsConstructor
public class DailyFormat1ReportUtility {

    private final DailyFormat2ReportUtility dailyFormat2ReportUtility;

    /**
     *
     * @param reportRQ ReportRQ
     * @param userLevelReportTOS Page<UserLevelReportTO>
     * @param filterMemberTOS List<UserLevelReportTO>
     * @return MainSaleReportRS
     */
    public MainSaleReportRS memberReportResponse(ReportRQ reportRQ, List<UserLevelReportTO> userLevelReportTOS, List<UserLevelReportTO> filterMemberTOS) {
        MainSaleReportRS mainSaleReportRS = dailyFormat2ReportUtility.memberReportResponse(reportRQ, userLevelReportTOS, filterMemberTOS);
        transformDataToFormatOne(mainSaleReportRS.getUnderLineSale());
        transformDataToFormatOne(mainSaleReportRS.getUpperLineSale());

        MainSaleReportRS mainSaleReportFormatOneRS = new MainSaleReportRS();
        mainSaleReportFormatOneRS.setUnderLineSale(mainSaleReportRS.getUnderLineSale());
        mainSaleReportFormatOneRS.setUpperLineSale(mainSaleReportRS.getUpperLineSale());

        return mainSaleReportFormatOneRS;
    }


    /**
     * transform list sale item rs
     * @param saleReportRS SaleReportRS
     */
    private void transformDataToFormatOne(SaleReportRS saleReportRS) {
        List<SaleItemsRS> newSaleItemsRSList = new ArrayList<>();
        List<SaleItemsRS> oldSaleReportRSList = new ArrayList<>(saleReportRS.getItems());
        if (!oldSaleReportRSList.isEmpty()) {
            Map<String, List<SaleItemsRS>> saleItemGroupByDraw = oldSaleReportRSList.stream().collect(Collectors.groupingBy(SaleItemsRS::getDrawCode));
            for (String drawCode : saleItemGroupByDraw.keySet().stream().sorted().collect(Collectors.toList())) {
                List<SaleItemsRS> saleItemsRSList = new ArrayList<>(saleItemGroupByDraw.get(drawCode));
                saleItemsRSList.sort(Comparator.comparing(SaleItemsRS::getUsername));
                newSaleItemsRSList.addAll(saleItemsRSList);
            }
        }
        saleReportRS.setItems(newSaleItemsRSList);

    }

}
