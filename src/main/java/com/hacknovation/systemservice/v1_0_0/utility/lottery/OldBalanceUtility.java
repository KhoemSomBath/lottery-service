package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SummaryReportRS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * author: kangto
 * createdAt: 03/02/2022
 * time: 21:32
 */
@Component
@RequiredArgsConstructor
public class OldBalanceUtility {

    private final InitialBalanceUtility initialBalanceUtility;
    private final SettlementUtility settlementUtility;
    private final SummaryDailyUtility summaryDailyUtility;


    /**
     * get old balance map by lottery, and filter date
     * @param lotteryType String
     * @param userCodes List<String>
     * @param filterByDate String
     * @return Map<String, CurrencyRS>
     */
    public Map<String, CurrencyRS> getUserOldBalanceMap(String lotteryType, List<String> userCodes, String filterByDate) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();

        Map<String, CurrencyRS> initialBalanceMap = initialBalanceUtility.getInitialBalanceMap(lotteryType, userCodes);
        Map<String, CurrencyRS> settlementMap = settlementUtility.getSumSettlementItemLessThanFilterDateMap(lotteryType, userCodes, filterByDate);
        Map<String, CurrencyRS> summaryDailyMap = summaryDailyUtility.getSummaryDailyLessThanDateMap(lotteryType, userCodes, filterByDate);

        for (String userCode : userCodes) {
            CurrencyRS currencyRS = new CurrencyRS();

            if (initialBalanceMap.containsKey(userCode)) {
                currencyRS.addFromCurrencyRS(initialBalanceMap.get(userCode));
            }

            if (settlementMap.containsKey(userCode)) {
                currencyRS.addFromCurrencyRS(settlementMap.get(userCode));
            }

            if (summaryDailyMap.containsKey(userCode)) {
                currencyRS.addFromCurrencyRS(summaryDailyMap.get(userCode));
            }

            currencyRSMap.put(userCode, currencyRS);
        }

        return currencyRSMap;
    }

    public Map<String, CurrencyRS> getUserOldBalanceMapMix(List<String> userCodes, String filterByDate) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();

        Map<String, CurrencyRS> initialBalanceMap = initialBalanceUtility.getInitialBalanceMapMix(userCodes);
        Map<String, CurrencyRS> settlementMap = settlementUtility.getSumSettlementItemLessThanFilterDateMapMix(userCodes, filterByDate);
        Map<String, CurrencyRS> summaryDailyMap = summaryDailyUtility.getSummaryDailyLessThanDateMapMix(userCodes, filterByDate);

        for (String userCode : userCodes) {
            CurrencyRS currencyRS = new CurrencyRS();

            if (initialBalanceMap.containsKey(userCode)) {
                currencyRS.addFromCurrencyRS(initialBalanceMap.get(userCode));
            }

            if (settlementMap.containsKey(userCode)) {
                currencyRS.addFromCurrencyRS(settlementMap.get(userCode));
            }

            if (summaryDailyMap.containsKey(userCode)) {
                currencyRS.addFromCurrencyRS(summaryDailyMap.get(userCode));
            }

            currencyRSMap.put(userCode, currencyRS);
        }

        return currencyRSMap;
    }

    /**
     * update old balance in sale report rs
     * @param saleReportRS SaleReportRS
     * @param userCodes List<String>
     * @param reportRQ ReportRQ
     */
    public void updateOldBalance(SaleReportRS saleReportRS, List<String> userCodes, ReportRQ reportRQ) {
        Map<String, CurrencyRS> oldBalanceMapByUserCode = getUserOldBalanceMap(reportRQ.getFilterByLotteryType(), userCodes, reportRQ.getFilterByEndDate());
        CurrencyRS oldBalance = new CurrencyRS();
        oldBalanceMapByUserCode.forEach((code, item) -> oldBalance.addFromCurrencyRS(item));
        SummaryReportRS summaryReportRS = saleReportRS.getSummary();
        summaryReportRS.setOldAmountKhr(oldBalance.getAmountKhr());
        summaryReportRS.setOldAmountUsd(oldBalance.getAmountUsd());
    }


}
