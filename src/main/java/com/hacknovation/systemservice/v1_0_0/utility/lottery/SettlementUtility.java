package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.SettlementConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.SettlementItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.settlement.SettlementNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.settlement.SettlementTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.SettlementRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.TotalReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SummaryReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementSummaryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementTotalSummeryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementsItemListRS;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SettlementUtility {

    private final SettlementNQ settlementNQ;
    private final UpperUserCodeUtility upperUserCodeUtility;

    public void setSettlementItem(List<SettlementItemsEntity> settlementItemsEntities, SettlementTotalSummeryRS totalSummeryRS, SettlementsItemListRS itemListRS1) {
        if (settlementItemsEntities.size() > 0) {

            List<SettlementItemsEntity> borrows = settlementItemsEntities.stream().filter(item -> String.valueOf(item.getType()).equals(LotteryConstant.BORROW)).collect(Collectors.toList());
            SettlementSummaryRS borrowAmount = new SettlementSummaryRS();
            for (SettlementItemsEntity borrowEntity : borrows) {
                SettlementSummaryRS borrow = setAmount(borrowEntity);
                borrowAmount.setItemId(borrowEntity.getId());
                borrowAmount.addFromSettlementSummaryRS(borrow);
            }
            itemListRS1.setBorrow(borrowAmount);

            List<SettlementItemsEntity> gives = settlementItemsEntities.stream().filter(item -> String.valueOf(item.getType()).equals(LotteryConstant.GIVE)).collect(Collectors.toList());
            SettlementSummaryRS giveAmount = new SettlementSummaryRS();
            for (SettlementItemsEntity giveEntity : gives) {
                SettlementSummaryRS give = setAmount(giveEntity);
                giveAmount.setItemId(giveEntity.getId());
                giveAmount.addFromSettlementSummaryRS(give);
            }
            itemListRS1.setGive(giveAmount);

            List<SettlementItemsEntity> protestAmounts = settlementItemsEntities.stream().filter(item -> String.valueOf(item.getType()).equals(LotteryConstant.PROTEST_AMOUNT)).collect(Collectors.toList());
            SettlementSummaryRS protestAmount = new SettlementSummaryRS();
            for (SettlementItemsEntity protestAmountEntity : protestAmounts) {
                protestAmount.setItemId(protestAmountEntity.getId());
                SettlementSummaryRS upperProtest = setAmount(protestAmountEntity);
                protestAmount.addFromSettlementSummaryRS(upperProtest);
            }
            itemListRS1.setProtestAmount(protestAmount);

            setTotalAmount(borrowAmount, totalSummeryRS.getBorrow());
            setTotalAmount(giveAmount, totalSummeryRS.getGive());
            setTotalAmount(protestAmount, totalSummeryRS.getProtestAmount());

        }
    }

    private void setTotalAmount(SettlementSummaryRS summaryRS, SettlementSummaryRS oldTotalSummeryRS) {
        oldTotalSummeryRS.setAmountKhr(oldTotalSummeryRS.getAmountKhr().add(summaryRS.getAmountKhr()));
        oldTotalSummeryRS.setAmountUsd(oldTotalSummeryRS.getAmountUsd().add(summaryRS.getAmountUsd()));
    }

    private SettlementSummaryRS setAmount(SettlementItemsEntity item) {
        SettlementSummaryRS amount = new SettlementSummaryRS();
        amount.setAmountKhr(item.getAmountKhr());
        amount.setAmountUsd(item.getAmountUsd());
        amount.setItemId(item.getId());
        return amount;
    }

    private SettlementSummaryRS setZero() {
        SettlementSummaryRS amount = new SettlementSummaryRS();
        amount.setAmountKhr(BigDecimal.ZERO);
        amount.setAmountUsd(BigDecimal.ZERO);
        return amount;
    }

    public void setSettlementSale(SettlementsItemListRS itemListRS1, List<SaleItemsRS> saleItemsRSList) {
        for (SaleItemsRS item : saleItemsRSList) {

            itemListRS1.setCom1DKhr(itemListRS1.getCom1DKhr().add(item.getCom1DKhr()));
            itemListRS1.setCom2DKhr(itemListRS1.getCom2DKhr().add(item.getCom2DKhr()));
            itemListRS1.setCom3DKhr(itemListRS1.getCom3DKhr().add(item.getCom3DKhr()));
            itemListRS1.setCom4DKhr(itemListRS1.getCom4DKhr().add(item.getCom4DKhr()));

            itemListRS1.setCom1DUsd(itemListRS1.getCom1DUsd().add(item.getCom1DUsd()));
            itemListRS1.setCom2DUsd(itemListRS1.getCom2DUsd().add(item.getCom2DUsd()));
            itemListRS1.setCom3DUsd(itemListRS1.getCom3DUsd().add(item.getCom3DUsd()));
            itemListRS1.setCom4DUsd(itemListRS1.getCom4DUsd().add(item.getCom4DUsd()));

            itemListRS1.setBetAmount1DKhr(itemListRS1.getBetAmount1DKhr().add(item.getBetAmount1DKhr()));
            itemListRS1.setBetAmount2DKhr(itemListRS1.getBetAmount2DKhr().add(item.getBetAmount2DKhr()));
            itemListRS1.setBetAmount3DKhr(itemListRS1.getBetAmount3DKhr().add(item.getBetAmount3DKhr()));
            itemListRS1.setBetAmount4DKhr(itemListRS1.getBetAmount4DKhr().add(item.getBetAmount4DKhr()));

            itemListRS1.setBetAmount1DUsd(itemListRS1.getBetAmount1DUsd().add(item.getBetAmount1DUsd()));
            itemListRS1.setBetAmount2DUsd(itemListRS1.getBetAmount2DUsd().add(item.getBetAmount2DUsd()));
            itemListRS1.setBetAmount3DUsd(itemListRS1.getBetAmount3DUsd().add(item.getBetAmount3DUsd()));
            itemListRS1.setBetAmount4DUsd(itemListRS1.getBetAmount4DUsd().add(item.getBetAmount4DUsd()));

            itemListRS1.setWinAmount1DKhr(itemListRS1.getWinAmount1DKhr().add(item.getWinAmount1DKhr()));
            itemListRS1.setWinAmount2DKhr(itemListRS1.getWinAmount2DKhr().add(item.getWinAmount2DKhr()));
            itemListRS1.setWinAmount3DKhr(itemListRS1.getWinAmount3DKhr().add(item.getWinAmount3DKhr()));
            itemListRS1.setWinAmount4DKhr(itemListRS1.getWinAmount4DKhr().add(item.getWinAmount4DKhr()));

            itemListRS1.setWinAmount1DUsd(itemListRS1.getWinAmount1DUsd().add(item.getWinAmount1DUsd()));
            itemListRS1.setWinAmount2DUsd(itemListRS1.getWinAmount2DUsd().add(item.getWinAmount2DUsd()));
            itemListRS1.setWinAmount3DUsd(itemListRS1.getWinAmount3DUsd().add(item.getWinAmount3DUsd()));
            itemListRS1.setWinAmount4DUsd(itemListRS1.getWinAmount4DUsd().add(item.getWinAmount4DUsd()));

            itemListRS1.setRewardAmount1DKhr(itemListRS1.getRewardAmount1DKhr().add(item.getRewardAmount1DKhr()));
            itemListRS1.setRewardAmount2DKhr(itemListRS1.getRewardAmount2DKhr().add(item.getRewardAmount2DKhr()));
            itemListRS1.setRewardAmount3DKhr(itemListRS1.getRewardAmount3DKhr().add(item.getRewardAmount3DKhr()));
            itemListRS1.setRewardAmount4DKhr(itemListRS1.getRewardAmount4DKhr().add(item.getRewardAmount4DKhr()));

            itemListRS1.setRewardAmount1DUsd(itemListRS1.getRewardAmount1DUsd().add(item.getRewardAmount1DUsd()));
            itemListRS1.setRewardAmount2DUsd(itemListRS1.getRewardAmount2DUsd().add(item.getRewardAmount2DUsd()));
            itemListRS1.setRewardAmount3DUsd(itemListRS1.getRewardAmount3DUsd().add(item.getRewardAmount3DUsd()));
            itemListRS1.setRewardAmount4DUsd(itemListRS1.getRewardAmount4DUsd().add(item.getRewardAmount4DUsd()));

        }
        itemListRS1.setWinLoseAmountKhr(itemListRS1.getCommissionKhr().subtract(itemListRS1.getRewardAmountKhr()));
        itemListRS1.setWinLoseAmountUsd(itemListRS1.getCommissionUsd().subtract(itemListRS1.getRewardAmountUsd()));

        itemListRS1.setTotalAmountKhr(itemListRS1.getOldAmountKhr().add(itemListRS1.getWinLoseAmountKhr()).add(itemListRS1.getGive().getAmountKhr()).add(itemListRS1.getBorrow().getAmountKhr()).add(itemListRS1.getProtestAmount().getAmountKhr()));
        itemListRS1.setTotalAmountUsd(itemListRS1.getOldAmountUsd().add(itemListRS1.getWinLoseAmountUsd()).add(itemListRS1.getGive().getAmountUsd()).add(itemListRS1.getBorrow().getAmountUsd()).add(itemListRS1.getProtestAmount().getAmountUsd()));
    }

    /**
     * get settlement item by lottery type, user code and less than filter date
     * @param lotteryType String
     * @param userCodes List<String>
     * @param filterByDate String
     * @return Map<String, CurrencyRS>
     */
    public Map<String, CurrencyRS> getSumSettlementItemLessThanFilterDateMap(String lotteryType, List<String> userCodes, String filterByDate) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();
        List<SettlementTO> settlementTOList = settlementNQ.sumSettlementItemsLessThanDate(lotteryType, userCodes, filterByDate);
        settlementTOList.forEach(item -> {
            CurrencyRS currencyRS = new CurrencyRS();
            BeanUtils.copyProperties(item, currencyRS);
            currencyRSMap.put(item.getUserCode(), currencyRS);
        });

        return currencyRSMap;
    }

    public Map<String, CurrencyRS> getSumSettlementItemLessThanFilterDateMapMix(List<String> userCodes, String filterByDate) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();
        List<SettlementTO> settlementTOList = settlementNQ.sumSettlementItemsLessThanDateMix(userCodes, filterByDate);
        settlementTOList.forEach(item -> {
            CurrencyRS currencyRS = new CurrencyRS();
            BeanUtils.copyProperties(item, currencyRS);
            currencyRSMap.put(item.getUserCode(), currencyRS);
        });

        return currencyRSMap;
    }

    /**
     * get map settlement item by type and by map user code
     * @param lotteryType String
     * @param userCodes List<String>
     * @param filterByDate String
     * @return Map<String, Map<String, SettlementTO>
     */
    public Map<String, Map<String, SettlementTO>> getSumSettlementItemEqualFilterDateByTypeMap(String lotteryType, List<String> userCodes, String filterByDate) {
        Map<String, Map<String, SettlementTO>> settlementTOMapByUserCode = new HashMap<>();
        List<SettlementTO> settlementTOList = settlementNQ.sumSettlementItemEqualDateGroupByType(lotteryType, userCodes, filterByDate);
        if (!settlementTOList.isEmpty()) {
            Map<String, List<SettlementTO>> listMapByUserCode = settlementTOList.stream().collect(Collectors.groupingBy(SettlementTO::getUserCode, Collectors.toList()));
            listMapByUserCode.forEach((userCode, settlements) -> {
                settlementTOMapByUserCode.put(userCode, settlements.stream().collect(Collectors.toMap(SettlementTO::getType, Function.identity())));
            });
        }
        return settlementTOMapByUserCode;
    }

    /**
     * update settlement item in sale report rs
     * @param saleReportRS SaleReportRS
     * @param userCodes List<String>
     * @param reportRQ ReportRQ
     */
    public void updateSettlementItemSummaryRS(SaleReportRS saleReportRS, List<String> userCodes, ReportRQ reportRQ) {
        Map<String, Map<String, SettlementTO>> settlementTOMapByUserCode = getSumSettlementItemEqualFilterDateByTypeMap(reportRQ.getFilterByLotteryType(), userCodes, reportRQ.getFilterByStartDate());
        SummaryReportRS summaryReportRS = saleReportRS.getSummary();
        CurrencyRS borrow = new CurrencyRS();
        CurrencyRS give = new CurrencyRS();
        CurrencyRS protestAmount = new CurrencyRS();
        settlementTOMapByUserCode.forEach((code, settlementMap) -> {
            settlementMap.forEach((type, item)-> {
                if (SettlementConstant.BORROW.equalsIgnoreCase(item.getType())) {
                    borrow.setAmountKhr(borrow.getAmountKhr().add(item.getAmountKhr()));
                    borrow.setAmountUsd(borrow.getAmountUsd().add(item.getAmountUsd()));
                }
                if (SettlementConstant.GIVE.equalsIgnoreCase(item.getType())) {
                    give.setAmountKhr(give.getAmountKhr().add(item.getAmountKhr()));
                    give.setAmountUsd(give.getAmountUsd().add(item.getAmountUsd()));
                }
                if (SettlementConstant.PROTEST_AMOUNT.equalsIgnoreCase(item.getType())) {
                    protestAmount.setAmountKhr(protestAmount.getAmountKhr().add(item.getAmountKhr()));
                    protestAmount.setAmountUsd(protestAmount.getAmountUsd().add(item.getAmountUsd()));
                }
            });
        });

        summaryReportRS.setBorrowAmountKhr(borrow.getAmountKhr());
        summaryReportRS.setBorrowAmountUsd(borrow.getAmountUsd());
        summaryReportRS.setGiveAmountKhr(give.getAmountKhr());
        summaryReportRS.setGiveAmountUsd(give.getAmountUsd());
        summaryReportRS.setProtestAmountKhr(protestAmount.getAmountKhr());
        summaryReportRS.setProtestAmountUsd(protestAmount.getAmountUsd());
    }

    /**
     * get is editable settlement
     * @param userToken UserToken
     * @param reportRQ ReportRQ
     * @return boolean
     */
    public boolean getIsEditableSettlement(UserToken userToken, ReportRQ reportRQ) {
        boolean isEdit = true;
        if (!UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType())) {
            String roleCode = userToken.getUserRole();
            if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(roleCode)) {
                return false;
            }
            String underLevelOfCurrentUser = upperUserCodeUtility.underLevelRole(roleCode);
            if (!reportRQ.getFilterByLevel().equalsIgnoreCase(underLevelOfCurrentUser))
                isEdit = false;
        }
        if (!reportRQ.getFilterByStartDate().equals(reportRQ.getFilterByEndDate()))
            isEdit = false;

        return isEdit;
    }

    public boolean getIsEditableSettlement(UserToken userToken, TotalReportRQ reportRQ) {
        boolean isEdit = true;
        if (!UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType())) {
            String roleCode = userToken.getUserRole();
            if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(roleCode)) {
                return false;
            }
            String underLevelOfCurrentUser = upperUserCodeUtility.underLevelRole(roleCode);
            if (!reportRQ.getFilterByLevel().equalsIgnoreCase(underLevelOfCurrentUser))
                isEdit = false;
        }
        if (!reportRQ.getFilterByStartDate().equals(reportRQ.getFilterByEndDate()))
            isEdit = false;

        return isEdit;
    }

    public boolean getIsEditableSettlement(UserToken userToken, SettlementRQ reportRQ) {
        boolean isEdit = true;
        if (!UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType())) {
            String roleCode = userToken.getUserRole();
            if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(roleCode)) {
                return false;
            }
            String underLevelOfCurrentUser = upperUserCodeUtility.underLevelRole(roleCode);
            if (!reportRQ.getFilterByLevel().equalsIgnoreCase(underLevelOfCurrentUser))
                isEdit = false;
        }
        if (!reportRQ.getFilterByStartDate().equals(reportRQ.getFilterByEndDate()))
            isEdit = false;

        return isEdit;
    }
}
