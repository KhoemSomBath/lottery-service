package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.daily.DailyReportNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.daily.DailyReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.daily.TempDailyReportNQ;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserHasLotteryRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.CommissionDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.HasLotteryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.MainSaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SummaryReportRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DailyReportUtility {

    private final GeneralUtility generalUtility;
    private final DailyReportNQ dailyReportNQ;
    private final TempDailyReportNQ tempDailyReportNQ;
    private final UserHasLotteryRP userHasLotteryRP;
    private final UserRP userRP;
    private final UpperUserCodeUtility upperUserCodeUtility;
    private final OldBalanceUtility oldBalanceUtility;
    private final SettlementUtility settlementUtility;

    /**
     * get data sale leap
     * @param memberCodes List<String>
     * @param reportRQ ReportRQ
     * @return List<DailyReportTO>
     */
    public List<DailyReportTO> getDataLeap(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempDailyReportNQ.leapSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate()
                );
        } else {
            return dailyReportNQ.leapSaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate()
            );
        }
    }

    /**
     * get data sale vn1
     * @param memberCodes List<String>
     * @param reportRQ ReportRQ
     * @return List<DailyReportTO>
     */
    public List<DailyReportTO> getDataVn1(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempDailyReportNQ.vn1SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate()
            );
        } else {
            return dailyReportNQ.vn1SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate()
            );
        }
    }

    /**
     * get data sale vn2
     * @param memberCodes List<String>
     * @param reportRQ ReportRQ
     * @return List<DailyReportTO>
     */
    public List<DailyReportTO> getDataVn2(List<String> memberCodes, ReportRQ reportRQ) {
        if (generalUtility.isTempTable(reportRQ.getFilterByStartDate())) {
            return tempDailyReportNQ.vn2SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate()
            );
        } else {
            return dailyReportNQ.vn2SaleReport(
                    memberCodes,
                    reportRQ.getFilterByStartDate()
            );
        }
    }

    /**
     * get main sale report rs
     * @param reportRQ ReportRQ
     * @param userLevelReportTOS Page<UserLevelReportTO>
     * @param filterByOriginLevel String
     * @param memberCodes List<String>
     * @return MainSaleReportRS
     */
    public MainSaleReportRS dailyResponse(ReportRQ reportRQ, Page<UserLevelReportTO> userLevelReportTOS, String filterByOriginLevel, List<String> memberCodes) {

        UserEntity userEntity;
        List<CommissionDTO> upperLineHasCommission;
        HasLotteryRS hasLotteryRS = new HasLotteryRS();
        if (UserConstant.COMPANY.equalsIgnoreCase(reportRQ.getFilterByUserCode())) {
            userEntity = new UserEntity();
            userEntity.setCode("168");
            userEntity.setUsername(UserConstant.COMPANY);
            userEntity.setNickname(UserConstant.COMPANY);
            userEntity.setRoleCode(UserConstant.COMPANY);
            generalUtility.getDefaultCompanyHasLotteryRS(hasLotteryRS);
        } else {
            userEntity = userRP.findByCode(reportRQ.getFilterByUserCode());
            /*
             * Get user commission
             */
            upperLineHasCommission = userHasLotteryRP.commission(Collections.singletonList(reportRQ.getFilterByUserCode()));
            generalUtility.setUserHasLottery(upperLineHasCommission, reportRQ.getFilterByLotteryType(), hasLotteryRS);
        }

        List<String> userCodes = userLevelReportTOS.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());

        List<CommissionDTO> commissionUnderLine = userHasLotteryRP.commission(userCodes);
        Map<String, List<CommissionDTO>> underLineHasCommission = commissionUnderLine.stream().collect(Collectors.groupingBy(CommissionDTO::getUserCode, Collectors.toList()));

        /*
         * Data models
         */
        List<DailyReportTO> leapData = new ArrayList<>();
        List<DailyReportTO> vn1Data = new ArrayList<>();
        List<DailyReportTO> vn2Data = new ArrayList<>();
        switch (reportRQ.getFilterByLotteryType()) {
            case LotteryConstant.LEAP:
                leapData = getDataLeap(memberCodes, reportRQ);
                break;
            case LotteryConstant.VN1:
                vn1Data = getDataVn1(memberCodes, reportRQ);
                break;
            case LotteryConstant.VN2:
                vn2Data = getDataVn2(memberCodes, reportRQ);
                break;
            default:
                break;
        }

        /*
         * Underline Sale Report
         */
        SummaryReportRS summaryUnderLine = new SummaryReportRS();
        List<SaleItemsRS> underLineItemsRS = new ArrayList<>();

        /*
         * get summary report rs by userLevel and dailyReportTO
         */
        getSummaryReportRS(summaryUnderLine, underLineItemsRS, userLevelReportTOS, leapData, vn1Data, vn2Data, underLineHasCommission, reportRQ);

        SaleReportRS underLineReport = new SaleReportRS();
        underLineReport.setLotteryType(reportRQ.getFilterByLotteryType());
        underLineReport.setRoleCode(reportRQ.getFilterByLevel());
        underLineReport.setNickname(reportRQ.getFilterByLotteryType());
        underLineReport.setUsername(upperUserCodeUtility.underLevelRole(userEntity.getRoleCode()));
        underLineReport.setDate(reportRQ.getFilterByStartDate());
        underLineReport.setHasLottery(hasLotteryRS);
        underLineReport.setSummary(summaryUnderLine);
        underLineReport.setItems(underLineItemsRS);

        /*
         * copy data from under and update commission for upper level
         */
        SaleReportRS upperLineReport = new SaleReportRS();
        BeanUtils.copyProperties(underLineReport, upperLineReport);
        upperLineReport.setRoleCode(filterByOriginLevel.toUpperCase());
        upperLineReport.setUsername(userEntity.getUsername());
        upperLineReport.setNickname(userEntity.getNickname());

//        generalUtility.transformSaleReportRSForUpperLevel(upperLineReport);

        /*
         * update old balance for underLine & upperLine
         */
        oldBalanceUtility.updateOldBalance(underLineReport, memberCodes, reportRQ);
        oldBalanceUtility.updateOldBalance(upperLineReport, List.of(userEntity.getCode()), reportRQ);

        /*
         * update settlement items give and borrow
         */
        settlementUtility.updateSettlementItemSummaryRS(underLineReport, memberCodes, reportRQ);
        settlementUtility.updateSettlementItemSummaryRS(upperLineReport, List.of(userEntity.getCode()), reportRQ);

        /*
         * Main sale report
         */
        MainSaleReportRS mainSaleReportRS = new MainSaleReportRS();
        mainSaleReportRS.setUnderLineSale(underLineReport);
        mainSaleReportRS.setUpperLineSale(upperLineReport);



        return mainSaleReportRS;
    }

    /**
     * get summary report rs
     * @param summaryUnderLine SummaryReportRS
     * @param underLineItemsRS List<SaleItemsRS>
     * @param userLevelReportTOS Page<UserLevelReportTO>
     * @param leapData List<DailyReportTO>
     * @param vn1Data List<DailyReportTO>
     * @param vn2Data List<DailyReportTO>
     * @param underLineHasCommission Map<String, List<CommissionDTO>>
     * @param reportRQ ReportRQ
     */
    private void getSummaryReportRS(SummaryReportRS summaryUnderLine, List<SaleItemsRS> underLineItemsRS, Page<UserLevelReportTO> userLevelReportTOS, List<DailyReportTO> leapData, List<DailyReportTO> vn1Data, List<DailyReportTO> vn2Data, Map<String, List<CommissionDTO>> underLineHasCommission, ReportRQ reportRQ) {
        for (UserLevelReportTO user : userLevelReportTOS) {
            SaleItemsRS underLineItemsRS1 = new SaleItemsRS();
            BeanUtils.copyProperties(user, underLineItemsRS1);
            HasLotteryRS hasLotteryRSByUser = new HasLotteryRS();
            generalUtility.setUserHasLottery(underLineHasCommission.get(user.getUserCode()), reportRQ.getFilterByLotteryType(), hasLotteryRSByUser);
            if (leapData.size() > 0) {
                leapData.forEach(item -> setCommissionSaleAndSummary(hasLotteryRSByUser, item, underLineItemsRS1));
                /*
                 * Sum summary
                 */
                sumSummaryReportRS(underLineItemsRS1, summaryUnderLine);
            }
            if (vn1Data.size() > 0) {
                vn1Data.forEach(item -> setCommissionSaleAndSummary(hasLotteryRSByUser, item, underLineItemsRS1));
                /*
                 * Sum summary
                 */
                sumSummaryReportRS(underLineItemsRS1, summaryUnderLine);
            }
            if (vn2Data.size() > 0) {
                vn2Data.forEach(item -> setCommissionSaleAndSummary(hasLotteryRSByUser, item, underLineItemsRS1));
                /*
                 * Sum summary
                 */
                sumSummaryReportRS(underLineItemsRS1, summaryUnderLine);
            }
            underLineItemsRS.add(underLineItemsRS1);
        }
    }


    /**
     * set commission and reward
     * @param hasLotteryRS HasLotteryRS
     * @param item DailyReportTO
     * @param saleItemsRS1 SaleItemsRS
     */
    private void setCommissionSaleAndSummary(HasLotteryRS hasLotteryRS, DailyReportTO item, SaleItemsRS saleItemsRS1) {
        List<String> codes = List.of(item.getSuperSeniorCode(), item.getSeniorCode(), item.getMasterCode(), item.getAgentCode(), item.getUserCode());
        if (codes.contains(saleItemsRS1.getUserCode())) {
            if (LotteryConstant.REBATE2D.equalsIgnoreCase(item.getRebateCode())) {
                if (UserConstant.MEMBER.equalsIgnoreCase(saleItemsRS1.getRoleCode())) {
                    saleItemsRS1.setCom2DKhr(saleItemsRS1.getCom2DKhr().add(generalUtility.commissionAmount(item.getBetAmountKhr(), item.getMemberCommission())));
                    saleItemsRS1.setCom2DUsd(saleItemsRS1.getCom2DUsd().add(generalUtility.commissionAmount(item.getBetAmountUsd(), item.getMemberCommission())));

                    saleItemsRS1.setRewardAmount2DKhr(saleItemsRS1.getRewardAmount2DKhr().add(item.getWinAmountKhr().multiply(item.getMemberRebateRate())));
                    saleItemsRS1.setRewardAmount2DUsd(saleItemsRS1.getRewardAmount2DUsd().add(item.getWinAmountUsd().multiply(item.getMemberRebateRate())));
                } else {
                    saleItemsRS1.setCom2DKhr(saleItemsRS1.getCom2DKhr().add(generalUtility.commissionAmount(item.getBetAmountKhr(), hasLotteryRS.getCom2D())));
                    saleItemsRS1.setCom2DUsd(saleItemsRS1.getCom2DUsd().add(generalUtility.commissionAmount(item.getBetAmountUsd(), hasLotteryRS.getCom2D())));

                    saleItemsRS1.setRewardAmount2DKhr(saleItemsRS1.getRewardAmount2DKhr().add(item.getWinAmountKhr().multiply(hasLotteryRS.getRebateRate2D())));
                    saleItemsRS1.setRewardAmount2DUsd(saleItemsRS1.getRewardAmount2DUsd().add(item.getWinAmountUsd().multiply(hasLotteryRS.getRebateRate2D())));
                }

                saleItemsRS1.setBetAmount2DKhr(saleItemsRS1.getBetAmount2DKhr().add(item.getBetAmountKhr()));
                saleItemsRS1.setBetAmount2DUsd(saleItemsRS1.getBetAmount2DUsd().add(item.getBetAmountUsd()));

                saleItemsRS1.setWinAmount2DKhr(saleItemsRS1.getWinAmount2DKhr().add(item.getWinAmountKhr()));
                saleItemsRS1.setWinAmount2DUsd(saleItemsRS1.getWinAmount2DUsd().add(item.getWinAmountUsd()));
            }

            if (LotteryConstant.REBATE3D.equalsIgnoreCase(item.getRebateCode())) {
                if (UserConstant.MEMBER.equalsIgnoreCase(saleItemsRS1.getRoleCode())) {
                    saleItemsRS1.setCom3DKhr(saleItemsRS1.getCom3DKhr().add(generalUtility.commissionAmount(item.getBetAmountKhr(), item.getMemberCommission())));
                    saleItemsRS1.setCom3DUsd(saleItemsRS1.getCom3DUsd().add(generalUtility.commissionAmount(item.getBetAmountUsd(), item.getMemberCommission())));

                    saleItemsRS1.setRewardAmount3DKhr(saleItemsRS1.getRewardAmount3DKhr().add(item.getWinAmountKhr().multiply(item.getMemberRebateRate())));
                    saleItemsRS1.setRewardAmount3DUsd(saleItemsRS1.getRewardAmount3DUsd().add(item.getWinAmountUsd().multiply(item.getMemberRebateRate())));
                } else {
                    saleItemsRS1.setCom3DKhr(saleItemsRS1.getCom3DKhr().add(generalUtility.commissionAmount(item.getBetAmountKhr(), hasLotteryRS.getCom3D())));
                    saleItemsRS1.setCom3DUsd(saleItemsRS1.getCom3DUsd().add(generalUtility.commissionAmount(item.getBetAmountUsd(), hasLotteryRS.getCom3D())));

                    saleItemsRS1.setRewardAmount3DKhr(saleItemsRS1.getRewardAmount3DKhr().add(item.getWinAmountKhr().multiply(hasLotteryRS.getRebateRate3D())));
                    saleItemsRS1.setRewardAmount3DUsd(saleItemsRS1.getRewardAmount3DUsd().add(item.getWinAmountUsd().multiply(hasLotteryRS.getRebateRate3D())));
                }

                saleItemsRS1.setBetAmount3DKhr(saleItemsRS1.getBetAmount3DKhr().add(item.getBetAmountKhr()));
                saleItemsRS1.setBetAmount3DUsd(saleItemsRS1.getBetAmount3DUsd().add(item.getBetAmountUsd()));

                saleItemsRS1.setWinAmount3DKhr(saleItemsRS1.getWinAmount3DKhr().add(item.getWinAmountKhr()));
                saleItemsRS1.setWinAmount3DUsd(saleItemsRS1.getWinAmount3DUsd().add(item.getWinAmountUsd()));
            }

            if (LotteryConstant.REBATE4D.equalsIgnoreCase(item.getRebateCode())) {
                if (UserConstant.MEMBER.equalsIgnoreCase(saleItemsRS1.getRoleCode())) {
                    saleItemsRS1.setCom4DKhr(saleItemsRS1.getCom4DKhr().add(generalUtility.commissionAmount(item.getBetAmountKhr(), item.getMemberCommission())));
                    saleItemsRS1.setCom4DUsd(saleItemsRS1.getCom4DUsd().add(generalUtility.commissionAmount(item.getBetAmountUsd(), item.getMemberCommission())));

                    saleItemsRS1.setRewardAmount4DKhr(saleItemsRS1.getRewardAmount4DKhr().add(item.getWinAmountKhr().multiply(item.getMemberRebateRate())));
                    saleItemsRS1.setRewardAmount4DUsd(saleItemsRS1.getRewardAmount4DUsd().add(item.getWinAmountUsd().multiply(item.getMemberRebateRate())));
                } else {
                    saleItemsRS1.setCom4DKhr(saleItemsRS1.getCom4DKhr().add(generalUtility.commissionAmount(item.getBetAmountKhr(), hasLotteryRS.getCom4D())));
                    saleItemsRS1.setCom4DUsd(saleItemsRS1.getCom4DUsd().add(generalUtility.commissionAmount(item.getBetAmountUsd(), hasLotteryRS.getCom4D())));

                    saleItemsRS1.setRewardAmount4DKhr(saleItemsRS1.getRewardAmount4DKhr().add(item.getWinAmountKhr().multiply(hasLotteryRS.getRebateRate4D())));
                    saleItemsRS1.setRewardAmount4DUsd(saleItemsRS1.getRewardAmount4DUsd().add(item.getWinAmountUsd().multiply(hasLotteryRS.getRebateRate4D())));
                }

                saleItemsRS1.setBetAmount4DKhr(saleItemsRS1.getBetAmount4DKhr().add(item.getBetAmountKhr()));
                saleItemsRS1.setBetAmount4DUsd(saleItemsRS1.getBetAmount4DUsd().add(item.getBetAmountUsd()));

                saleItemsRS1.setWinAmount4DKhr(saleItemsRS1.getWinAmount4DKhr().add(item.getWinAmountKhr()));
                saleItemsRS1.setWinAmount4DUsd(saleItemsRS1.getWinAmount4DUsd().add(item.getWinAmountUsd()));

            }

            saleItemsRS1.setWinLoseAmountKhr(saleItemsRS1.getCommissionKhr().subtract(saleItemsRS1.getRewardAmountKhr()));
            saleItemsRS1.setWinLoseAmountUsd(saleItemsRS1.getCommissionUsd().subtract(saleItemsRS1.getRewardAmountUsd()));

        }
    }

    /**
     * sum summaryReportRS
     * @param saleItemsRS1 SaleItemsRS
     * @param summaryReportRS SummaryReportRS
     */
    private void sumSummaryReportRS(SaleItemsRS saleItemsRS1, SummaryReportRS summaryReportRS) {

        summaryReportRS.setCom2DKhr(summaryReportRS.getCom2DKhr().add(saleItemsRS1.getCom2DKhr()));
        summaryReportRS.setCom2DUsd(summaryReportRS.getCom2DUsd().add(saleItemsRS1.getCom2DUsd()));

        summaryReportRS.setBetAmount2DKhr(summaryReportRS.getBetAmount2DKhr().add(saleItemsRS1.getBetAmount2DKhr()));
        summaryReportRS.setBetAmount2DUsd(summaryReportRS.getBetAmount2DUsd().add(saleItemsRS1.getBetAmount2DUsd()));

        summaryReportRS.setWinAmount2DKhr(summaryReportRS.getWinAmount2DKhr().add(saleItemsRS1.getWinAmount2DKhr()));
        summaryReportRS.setWinAmount2DUsd(summaryReportRS.getWinAmount2DUsd().add(saleItemsRS1.getWinAmount2DUsd()));

        summaryReportRS.setRewardAmount2DKhr(summaryReportRS.getRewardAmount2DKhr().add(saleItemsRS1.getRewardAmount2DKhr()));
        summaryReportRS.setRewardAmount2DUsd(summaryReportRS.getRewardAmount2DUsd().add(saleItemsRS1.getRewardAmount2DUsd()));

        summaryReportRS.setCom3DKhr(summaryReportRS.getCom3DKhr().add(saleItemsRS1.getCom3DKhr()));
        summaryReportRS.setCom3DUsd(summaryReportRS.getCom3DUsd().add(saleItemsRS1.getCom3DUsd()));

        summaryReportRS.setBetAmount3DKhr(summaryReportRS.getBetAmount3DKhr().add(saleItemsRS1.getBetAmount3DKhr()));
        summaryReportRS.setBetAmount3DUsd(summaryReportRS.getBetAmount3DUsd().add(saleItemsRS1.getBetAmount3DUsd()));

        summaryReportRS.setWinAmount3DKhr(summaryReportRS.getWinAmount3DKhr().add(saleItemsRS1.getWinAmount3DKhr()));
        summaryReportRS.setWinAmount3DUsd(summaryReportRS.getWinAmount3DUsd().add(saleItemsRS1.getWinAmount3DUsd()));

        summaryReportRS.setRewardAmount3DKhr(summaryReportRS.getRewardAmount3DKhr().add(saleItemsRS1.getRewardAmount3DKhr()));
        summaryReportRS.setRewardAmount3DUsd(summaryReportRS.getRewardAmount3DUsd().add(saleItemsRS1.getRewardAmount3DUsd()));

        summaryReportRS.setCom4DKhr(summaryReportRS.getCom4DKhr().add(saleItemsRS1.getCom4DKhr()));
        summaryReportRS.setCom4DUsd(summaryReportRS.getCom4DUsd().add(saleItemsRS1.getCom4DUsd()));

        summaryReportRS.setBetAmount4DKhr(summaryReportRS.getBetAmount4DKhr().add(saleItemsRS1.getBetAmount4DKhr()));
        summaryReportRS.setBetAmount4DUsd(summaryReportRS.getBetAmount4DUsd().add(saleItemsRS1.getBetAmount4DUsd()));

        summaryReportRS.setWinAmount4DKhr(summaryReportRS.getWinAmount4DKhr().add(saleItemsRS1.getWinAmount4DKhr()));
        summaryReportRS.setWinAmount4DUsd(summaryReportRS.getWinAmount4DUsd().add(saleItemsRS1.getWinAmount4DUsd()));

        summaryReportRS.setRewardAmount4DKhr(summaryReportRS.getRewardAmount4DKhr().add(saleItemsRS1.getRewardAmount4DKhr()));
        summaryReportRS.setRewardAmount4DUsd(summaryReportRS.getRewardAmount4DUsd().add(saleItemsRS1.getRewardAmount4DUsd()));

    summaryReportRS.setWinLoseAmountKhr(summaryReportRS.getWinLoseAmountKhr().add(saleItemsRS1.getWinLoseAmountKhr()));
    summaryReportRS.setWinLoseAmountUsd(summaryReportRS.getWinLoseAmountUsd().add(saleItemsRS1.getWinLoseAmountUsd()));

    }

}
