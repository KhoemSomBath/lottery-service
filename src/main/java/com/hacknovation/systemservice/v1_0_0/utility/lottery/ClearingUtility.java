package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.SettlementConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.clearing.ClearingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.clearing.ClearingTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.clearing.TempClearingNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.settlement.SettlementTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ClearingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing.*;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import com.hacknovation.systemservice.v1_0_0.utility.user.UserReferralUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 07/02/2022
 * time: 10:02
 */
@Component
@RequiredArgsConstructor
public class ClearingUtility {

    private final UserRP userRP;
    private final ClearingNQ clearingNQ;
    private final TempClearingNQ tempClearingNQ;

    private final GeneralUtility generalUtility;
    private final UserReferralUtility userReferralUtility;
    private final SettlementUtility settlementUtility;
    private final UserHasLotteryUtility userHasLotteryUtility;
    private final UpperUserCodeUtility upperUserCodeUtility;
    private final OldBalanceUtility oldBalanceUtility;


    public MainMemberSummaryRS vn1ClearingReport(ClearingRQ clearingRQ) {
        MainMemberSummaryRS mainMemberSummaryRS = new MainMemberSummaryRS();
        MemberSummaryRS memberSummaryRS = new MemberSummaryRS();
        UserEntity userEntity = userRP.findByCode(clearingRQ.getFilterByUserCode());
        if (userEntity != null) {
            String upperCode = upperUserCodeUtility.upperUserCode(userEntity);
            List<String> memberCodes = new ArrayList<>();
            memberCodes.add(userEntity.getCode());
            if (!UserConstant.MEMBER.equalsIgnoreCase(userEntity.getRoleCode())) {
                memberCodes = userReferralUtility.userReferralByLevel(clearingRQ.getFilterByUserCode(), UserConstant.MEMBER);
            }

            /*
             * get settlement items
             */
            Map<String, Map<String, SettlementTO>> settlementItemsMapByUserCodeAndType = settlementUtility.getSumSettlementItemEqualFilterDateByTypeMap(LotteryConstant.VN1, List.of(userEntity.getCode()), clearingRQ.getFilterByDate());

            /*
             * get initial balance
             */
            Map<String, CurrencyRS> oldBalanceMapByUserCode = oldBalanceUtility.getUserOldBalanceMap(LotteryConstant.VN1, List.of(userEntity.getCode()), clearingRQ.getFilterByDate());

            /*
             * get user has lottery
             */
            Map<String, List<UserHasLotteryEntity>> userHasLotteryMapByUserCode = userHasLotteryUtility.getUserHasLotteryGroupByUser(LotteryConstant.VN1, List.of(userEntity.getCode(), upperCode));

            /*
             * sum total sale from order item
             */
            List<ClearingTO> clearingTOList;
            if (generalUtility.isTempTable(clearingRQ.getFilterByDate())) {
                clearingTOList = tempClearingNQ.vn1Clearing(memberCodes, clearingRQ.getFilterByDate());
            } else {
                clearingTOList = clearingNQ.vn1Clearing(memberCodes, clearingRQ.getFilterByDate());
            }
            Map<String, List<ClearingTO>> clearTOGroupByUserCode = clearingTOList.stream().collect(Collectors.groupingBy(ClearingTO::getUserCode, Collectors.toList()));

            /*
             * get member summary rs map by user code
             */
            Map<String, MemberSummaryRS> memberSummaryRSMapUserCode = new HashMap<>();
            getMemberSummeryRSMap(memberSummaryRSMapUserCode, clearTOGroupByUserCode, settlementItemsMapByUserCodeAndType, oldBalanceMapByUserCode.get(userEntity.getCode()), userHasLotteryMapByUserCode.get(userEntity.getCode()), clearingRQ.getFilterByDate());

            /*
             * if userEntity is member show all if not remove pageNumber and re-calculate commission & win
             */
            if (UserConstant.MEMBER.equalsIgnoreCase(userEntity.getRoleCode())) {
                if (memberSummaryRSMapUserCode.containsKey(userEntity.getCode())) {
                    BeanUtils.copyProperties(memberSummaryRSMapUserCode.get(userEntity.getCode()), memberSummaryRS);
                }
            } else {
                List<MemberRowRS> newMemberRows = new ArrayList<>();
                memberSummaryRSMapUserCode.forEach((code, memberRS) -> {
                    newMemberRows.addAll(memberRS.getRows());
                });
                MemberSummaryRS memberSummaryRSDiffMember = transformMemberSummaryRSNotMemberRole(newMemberRows, settlementItemsMapByUserCodeAndType.get(userEntity.getCode()), oldBalanceMapByUserCode.get(userEntity.getCode()), userHasLotteryMapByUserCode.get(userEntity.getCode()));
                BeanUtils.copyProperties(memberSummaryRSDiffMember, memberSummaryRS);
            }
            if (clearTOGroupByUserCode.isEmpty()) {
                List<SettlementItemRS> settlementItemRSList = new ArrayList<>();
                addSettlementItems(settlementItemRSList, settlementItemsMapByUserCodeAndType.get(userEntity.getCode()), oldBalanceMapByUserCode.get(userEntity.getCode()));
                memberSummaryRS.setSettlements(settlementItemRSList);
                calculateGrandTotal(memberSummaryRS);
            }
            memberSummaryRS.setUserCode(userEntity.getCode());
            memberSummaryRS.setNickname(userEntity.getNickname());
            memberSummaryRS.setUsername(userEntity.getUsername());
            memberSummaryRS.setFilterByDate(clearingRQ.getFilterByDate());

            MemberSummaryRS upperSummaryRS = new MemberSummaryRS();
            BeanUtils.copyProperties(memberSummaryRS, upperSummaryRS);


            mainMemberSummaryRS.setUpperLine(upperSummaryRS);
            mainMemberSummaryRS.setUnderLine(memberSummaryRS);
            /*
             * update commission and reward of upperLine by
             * user has lottery upperCode
             */
            updateMemberSummaryRSForUpper(mainMemberSummaryRS.getUpperLine(), userHasLotteryMapByUserCode.get(upperCode));
        }

        return mainMemberSummaryRS;
    }

    /**
     * transform member summaryRs from list of member rows
     * @param memberRowList List<MemberRowRS>
     * @param settlementTOMap Map<String, SettlementTO>
     * @param initialBalance CurrencyRS
     * @param userHasLotteryEntities List<UserHasLotteryEntity>
     * @return MemberSummaryRS
     */
    private MemberSummaryRS transformMemberSummaryRSNotMemberRole(List<MemberRowRS> memberRowList, Map<String, SettlementTO> settlementTOMap, CurrencyRS initialBalance, List<UserHasLotteryEntity> userHasLotteryEntities) {
        MemberSummaryRS notMemberSummaryRS = new MemberSummaryRS();
        List<MemberRowRS> memberRowRSList = new ArrayList<>(memberRowList);
        Map<Date, List<MemberRowRS>> memberRowsGroupByDrawAt = memberRowRSList.stream().collect(Collectors.groupingBy(MemberRowRS::getDrawAt, Collectors.toList()));
        List<MemberRowRS> newMemberRowRSList = new ArrayList<>();
        for (Date drawAt : memberRowsGroupByDrawAt.keySet()) {
            MemberRowRS memberRowRS = new MemberRowRS();
            memberRowRS.setDrawAt(drawAt);
            memberRowsGroupByDrawAt.get(drawAt).forEach(item -> {
                memberRowRS.getTwoDAmount().addFromCurrencyRS(item.getTwoDAmount());
                memberRowRS.getThreeDAmount().addFromCurrencyRS(item.getThreeDAmount());
                memberRowRS.getFourDAmount().addFromCurrencyRS(item.getFourDAmount());
                memberRowRS.getWinTwoDAmount().addFromCurrencyRS(item.getWinTwoDAmount());
                memberRowRS.getWinThreeDAmount().addFromCurrencyRS(item.getWinThreeDAmount());
                memberRowRS.getWinFourDAmount().addFromCurrencyRS(item.getWinFourDAmount());
            });
            newMemberRowRSList.add(memberRowRS);
        }
        List<SettlementItemRS> settlementItemRSList = new ArrayList<>();
        addSettlementItems(settlementItemRSList, settlementTOMap, initialBalance);
        notMemberSummaryRS.setSettlements(settlementItemRSList);
        notMemberSummaryRS.setRows(newMemberRowRSList);
        calculateSummaryLotteryRS(notMemberSummaryRS, userHasLotteryEntities);
        calculateGrandTotal(notMemberSummaryRS);

        return notMemberSummaryRS;
    }

    /**
     * update member upper summary rs by re-calculate commission and grand total
     * @param upperSummaryRS MemberSummaryRS
     * @param userHasLotteryEntities List<UserHasLotteryEntity>
     */
    private void updateMemberSummaryRSForUpper(MemberSummaryRS upperSummaryRS, List<UserHasLotteryEntity> userHasLotteryEntities) {
        calculateSummaryLotteryRS(upperSummaryRS, userHasLotteryEntities);
        calculateGrandTotal(upperSummaryRS);
    }

    /**
     * get member summery rs map by member code
     * @param memberSummaryRSMapUserCode Map<String, MemberSummaryRS>
     * @param clearTOGroupByUserCode Map<String, List<ClearingTO>>
     * @param settlementItemsMapByUserCodeAndType Map<String, Map<String, SettlementTO>>
     * @param initialBalance CurrencyRS
     * @param userHasLotteryEntities List<UserHasLotteryEntity>
     * @param filterByDate String
     */
    private void getMemberSummeryRSMap(Map<String, MemberSummaryRS> memberSummaryRSMapUserCode, Map<String, List<ClearingTO>> clearTOGroupByUserCode, Map<String, Map<String, SettlementTO>> settlementItemsMapByUserCodeAndType, CurrencyRS initialBalance, List<UserHasLotteryEntity> userHasLotteryEntities, String filterByDate) {
        for (String userCode : clearTOGroupByUserCode.keySet()) {
            MemberSummaryRS memberSummaryRS = new MemberSummaryRS();
            List<MemberRowRS> memberRowRSList = new ArrayList<>();

            List<SettlementItemRS> settlementItemRSList = new ArrayList<>();
            addSettlementItems(settlementItemRSList, settlementItemsMapByUserCodeAndType.get(userCode), initialBalance);

            memberSummaryRS.setUserCode(userCode);
            memberSummaryRS.setFilterByDate(filterByDate);
            memberSummaryRS.setSettlements(settlementItemRSList);

            List<ClearingTO> clearingTOList1 = new ArrayList<>(clearTOGroupByUserCode.get(userCode));
            Map<Date, List<ClearingTO>> clearTOGroupByDrawAt = clearingTOList1.stream().collect(Collectors.groupingBy(ClearingTO::getDrawAt, Collectors.toList()));
            /*
             * add member row list by each draw at
             */
            addMemberRowsListByDraw(memberRowRSList, clearTOGroupByDrawAt);
            memberSummaryRS.setRows(memberRowRSList);

            /*
             * calculate commission and rebate to set summaryLotteryRS
             */
            calculateSummaryLotteryRS(memberSummaryRS, userHasLotteryEntities);

            /*
             * calculate grandTotal
             */
            calculateGrandTotal(memberSummaryRS);

            /*
             * adding note rs
             */

            memberSummaryRSMapUserCode.put(userCode, memberSummaryRS);
        }
    }

    /**
     *
     * @param memberSummaryRS MemberSummaryRS
     */
    private void calculateGrandTotal(MemberSummaryRS memberSummaryRS) {
        CurrencyRS currencyRS = new CurrencyRS();
        currencyRS.setAmountKhr(memberSummaryRS.getSummaryLottery().getWinLose().getAmountKhr());
        currencyRS.setAmountUsd(memberSummaryRS.getSummaryLottery().getWinLose().getAmountUsd());
        memberSummaryRS.getSettlements().forEach(item -> currencyRS.addFromCurrencyRS(item.getValue()));

        memberSummaryRS.setGrandTotal(currencyRS);
    }

    /**
     * calculate summary lottery rs
     * @param memberSummaryRS MemberSummaryRS
     * @param userHasLotteryEntities List<UserHasLotteryEntity>
     */
    private void calculateSummaryLotteryRS(MemberSummaryRS memberSummaryRS, List<UserHasLotteryEntity> userHasLotteryEntities) {
        Map<String, UserHasLotteryEntity> userHasLotteryMap = userHasLotteryEntities.stream().collect(Collectors.toMap(UserHasLotteryEntity::getRebateCode, Function.identity()));
        UserHasLotteryEntity userHas2D = userHasLotteryUtility.getRebateAndCommission(userHasLotteryMap, LotteryConstant.REBATE2D);
        UserHasLotteryEntity userHas3D = userHasLotteryUtility.getRebateAndCommission(userHasLotteryMap, LotteryConstant.REBATE3D);
        UserHasLotteryEntity userHas4D = userHasLotteryUtility.getRebateAndCommission(userHasLotteryMap, LotteryConstant.REBATE4D);

        String rateString = String.valueOf(userHas2D.getRebateRate()).replaceAll("\\.0+$", "");
        String commissionString = String.valueOf(userHas2D.getCommission()).replaceAll("\\.0+$", "");

        rateString = rateString.concat("/").concat(String.valueOf(userHas3D.getRebateRate()).replaceAll("\\.0+$", ""));
        commissionString = commissionString.concat("/").concat(String.valueOf(userHas3D.getCommission()).replaceAll("\\.0+$", ""));

        if (userHas4D.getRebateRate().compareTo(BigDecimal.ZERO) > 0) {
            rateString = rateString.concat("/").concat(String.valueOf(userHas4D.getRebateRate()).replaceAll("\\.0+$", ""));
            commissionString = commissionString.concat("/").concat(String.valueOf(userHas4D.getCommission()).replaceAll("\\.0+$", ""));
        }

        SummaryLotteryRS summaryLotteryRS = new SummaryLotteryRS();
        memberSummaryRS.getRows().forEach(row -> {
            /*
             * Commission
             */
            sumCommission(summaryLotteryRS.getCommissionTwoD(), row.getTwoDAmount(), userHas2D.getCommission(), LotteryConstant.REBATE2D);
            sumCommission(summaryLotteryRS.getCommissionThreeD(), row.getThreeDAmount(), userHas3D.getCommission(), LotteryConstant.REBATE3D);
            if (userHas4D.getCommission().compareTo(BigDecimal.ZERO) > 0) {
                sumCommission(summaryLotteryRS.getCommissionFourD(), row.getFourDAmount(), userHas4D.getCommission(), LotteryConstant.REBATE4D);
            }

            /*
             * Reward
             */
            sumWinAmount(summaryLotteryRS.getWinTwoD(), row.getWinTwoDAmount(), userHas2D.getRebateRate(), LotteryConstant.REBATE2D);
            sumWinAmount(summaryLotteryRS.getWinThreeD(), row.getWinThreeDAmount(), userHas3D.getRebateRate(), LotteryConstant.REBATE3D);
            if (userHas4D.getRebateRate().compareTo(BigDecimal.ZERO) > 0) {
                sumWinAmount(summaryLotteryRS.getWinFourD(), row.getWinFourDAmount(), userHas4D.getRebateRate(), LotteryConstant.REBATE4D);
            }

        });

        CurrencyRS winLose = new CurrencyRS();
        winLose.setAmountKhr(getCommissionKhr(summaryLotteryRS).subtract(getWinKhr(summaryLotteryRS)));
        winLose.setAmountUsd(getCommissionUsd(summaryLotteryRS).subtract(getWinUsd(summaryLotteryRS)));

        summaryLotteryRS.setWinLose(winLose);

        memberSummaryRS.setSummaryLottery(summaryLotteryRS);
        memberSummaryRS.setRebate(rateString);
        memberSummaryRS.setCommission(commissionString);
    }

    /**
     * sum commission amount
     * @param summaryItemRS SummaryItemRS
     * @param currencyRS CurrencyRS
     * @param rate BigDecimal
     * @param rebateCode String
     */
    private void sumCommission(SummaryItemRS summaryItemRS, CurrencyRS currencyRS, BigDecimal rate, String rebateCode) {
        summaryItemRS.setAmountKhr(summaryItemRS.getAmountKhr().add(currencyRS.getAmountKhr()));
        summaryItemRS.setSubTotalKhr(summaryItemRS.getSubTotalKhr().add(generalUtility.commissionAmount(currencyRS.getAmountKhr(), rate)));

        summaryItemRS.setAmountUsd(summaryItemRS.getAmountUsd().add(currencyRS.getAmountUsd()));
        summaryItemRS.setSubTotalUsd(summaryItemRS.getSubTotalUsd().add(generalUtility.commissionAmount(currencyRS.getAmountUsd(), rate)));

        summaryItemRS.setRate(rate.divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN));
        summaryItemRS.setRateString(rebateCode.concat("x").concat(String.valueOf(rate)));
    }

    /**
     * sum win amount
     * @param summaryItemRS SummaryItemRS
     * @param currencyRS CurrencyRS
     * @param rate BigDecimal
     * @param rebateCode String
     */
    private void sumWinAmount(SummaryItemRS summaryItemRS, CurrencyRS currencyRS, BigDecimal rate, String rebateCode) {
        summaryItemRS.setAmountKhr(summaryItemRS.getAmountKhr().add(currencyRS.getAmountKhr()));
        summaryItemRS.setSubTotalKhr(summaryItemRS.getSubTotalKhr().add(currencyRS.getAmountKhr().multiply(rate)));

        summaryItemRS.setAmountUsd(summaryItemRS.getAmountUsd().add(currencyRS.getAmountUsd()));
        summaryItemRS.setSubTotalUsd(summaryItemRS.getSubTotalUsd().add(currencyRS.getAmountUsd().multiply(rate)));

        summaryItemRS.setRate(rate);
        summaryItemRS.setRateString(rebateCode.concat("x").concat(String.valueOf(rate)));
    }

    /**
     * set value member row
     * @param memberRowRS MemberRowRS
     * @param clearingTO ClearingTO
     */
    private void setValueMemberRowRS(MemberRowRS memberRowRS, ClearingTO clearingTO) {
        if (LotteryConstant.REBATE2D.equals(clearingTO.getRebateCode())) {
            increaseAmountAndWinCurrencyRS(memberRowRS.getTwoDAmount(), memberRowRS.getWinTwoDAmount(), clearingTO);
        }
        if (LotteryConstant.REBATE3D.equals(clearingTO.getRebateCode())) {
            increaseAmountAndWinCurrencyRS(memberRowRS.getThreeDAmount(), memberRowRS.getWinThreeDAmount(), clearingTO);
        }
        if (LotteryConstant.REBATE4D.equals(clearingTO.getRebateCode())) {
            increaseAmountAndWinCurrencyRS(memberRowRS.getFourDAmount(), memberRowRS.getWinFourDAmount(), clearingTO);
        }
    }

    /**
     * increase value of bet amount and win amount khr and usd
     * @param betAmount CurrencyRS
     * @param winAmount CurrencyRS
     * @param clearingTO ClearingTO
     */
    private void increaseAmountAndWinCurrencyRS(CurrencyRS betAmount, CurrencyRS winAmount, ClearingTO clearingTO) {
        betAmount.setAmountKhr(betAmount.getAmountKhr().add(clearingTO.getBetAmountKhr()));
        betAmount.setAmountUsd(betAmount.getAmountUsd().add(clearingTO.getBetAmountUsd()));

        winAmount.setAmountKhr(winAmount.getAmountKhr().add(clearingTO.getWinAmountKhr()));
        winAmount.setAmountUsd(winAmount.getAmountUsd().add(clearingTO.getWinAmountUsd()));
    }

    /**
     * add member row list from list mapping by draw At
     * @param memberRowRSList List<MemberRowRS>
     * @param clearTOGroupByDrawAt Map<Date, List<ClearingTO>>
     */
    private void addMemberRowsListByDraw(List<MemberRowRS> memberRowRSList, Map<Date, List<ClearingTO>> clearTOGroupByDrawAt) {
        for (Date drawAt : clearTOGroupByDrawAt.keySet()) {
            List<ClearingTO> clearingTOList2 = new ArrayList<>(clearTOGroupByDrawAt.get(drawAt));
            Map<Integer, List<ClearingTO>> clearTOGroupByPageNumber = clearingTOList2.stream().collect(Collectors.groupingBy(ClearingTO::getPageNumber, Collectors.toList()));

            /*
             * add member row list page by page
             */
            addMemberRowsList(memberRowRSList, clearTOGroupByPageNumber, drawAt);
        }
    }

    /**
     * add member row list from list mapping by page number
     * @param memberRowRSList List<MemberRowRS>
     * @param clearTOGroupByPageNumber Map<Integer, List<ClearingTO>>
     * @param drawAt Date
     */
    private void addMemberRowsList(List<MemberRowRS> memberRowRSList, Map<Integer, List<ClearingTO>> clearTOGroupByPageNumber, Date drawAt) {
        for (Integer pageNumber : clearTOGroupByPageNumber.keySet()) {
            MemberRowRS memberRowRS = new MemberRowRS();
            memberRowRS.setDrawAt(drawAt);
            memberRowRS.setPageNumber(pageNumber);
            clearTOGroupByPageNumber.get(pageNumber).forEach(item -> setValueMemberRowRS(memberRowRS, item));
            memberRowRSList.add(memberRowRS);
        }
    }

    /**
     * add settlement item to settlement item list
     * @param settlementItemRSList List<SettlementItemRS>
     * @param settlementTOMap Map<String, SettlementTO>
     * @param initialBalance CurrencyRS
     */
    private void addSettlementItems(List<SettlementItemRS> settlementItemRSList, Map<String, SettlementTO> settlementTOMap, CurrencyRS initialBalance) {
        SettlementItemRS item;
        CurrencyRS currencyRS;
        int index = 1;
        for (String type : SettlementConstant.MAIN_SETTLEMENT_ITEMS) {
            item = new SettlementItemRS();
            currencyRS = new CurrencyRS();
            if (settlementTOMap != null && settlementTOMap.containsKey(type)) {
                BeanUtils.copyProperties(settlementTOMap.get(type), currencyRS);
            }
            item.setSort(index);
            item.setType(type);
            item.setValue(currencyRS);
            settlementItemRSList.add(item);
            index++;
        }

        if (settlementTOMap != null) {
            for (String type : settlementTOMap.keySet()) {
                if (SettlementConstant.MAIN_SETTLEMENT_ITEMS.contains(type)) continue;
                if (settlementTOMap.containsKey(type)) {
                    item = new SettlementItemRS();
                    currencyRS = new CurrencyRS();
                    BeanUtils.copyProperties(settlementTOMap.get(type), currencyRS);
                    item.setSort(index);
                    item.setType(type);
                    item.setValue(currencyRS);
                    settlementItemRSList.add(item);
                    index++;
                }
            }
        }

        /*
         * get initial balance
         */
        SettlementItemRS settlementItemInitialRS = new SettlementItemRS();
        settlementItemInitialRS.setSort(0);
        settlementItemInitialRS.setType(SettlementConstant.INITIAL_BALANCE);
        if (initialBalance != null) {
            settlementItemInitialRS.setValue(initialBalance);
        }
        settlementItemRSList.add(0, settlementItemInitialRS);

    }

    /**
     * get commission khr
     * @param item SummaryLotteryRS
     * @return BigDecimal
     */
    public BigDecimal getCommissionKhr(SummaryLotteryRS item) {
        return item.getCommissionTwoD().getSubTotalKhr()
                .add(item.getCommissionThreeD().getSubTotalKhr())
                .add(item.getCommissionFourD().getSubTotalKhr());
    }

    /**
     * get commission usd
     * @param item SummaryLotteryRS
     * @return BigDecimal
     */
    public BigDecimal getCommissionUsd(SummaryLotteryRS item) {
        return item.getCommissionTwoD().getSubTotalUsd()
                .add(item.getCommissionThreeD().getSubTotalUsd())
                .add(item.getCommissionFourD().getSubTotalUsd());
    }

    /**
     * get win khr
     * @param item SummaryLotteryRS
     * @return BigDecimal
     */
    public BigDecimal getWinKhr(SummaryLotteryRS item) {
        return item.getWinTwoD().getSubTotalKhr()
                .add(item.getWinThreeD().getSubTotalKhr())
                .add(item.getWinFourD().getSubTotalKhr());
    }

    /**
     * get win usd
     * @param item SummaryLotteryRS
     * @return BigDecimal
     */
    public BigDecimal getWinUsd(SummaryLotteryRS item) {
        return item.getWinTwoD().getSubTotalUsd()
                .add(item.getWinThreeD().getSubTotalUsd())
                .add(item.getWinFourD().getSubTotalUsd());
    }
}
