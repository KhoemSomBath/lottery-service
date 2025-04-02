package com.hacknovation.systemservice.v2_0_0.component.totalreport;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.SettlementItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.SummeryDailyEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.SettlementItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.SummeryDailyRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.TotalReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary.SummaryItemRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.summary.SummaryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementTotalSummeryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementsItemListRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.OldBalanceUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.SettlementUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.SummaryDailyUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sombath
 * create at 26/1/23 2:01 PM
 */

@Service
@RequiredArgsConstructor
public class AllTotalReportService extends BaseServiceIP {

    private final UserRP userRP;
    private final JwtToken jwtToken;
    private final SettlementItemRP settlementItemRP;
    private final SettlementUtility settlementUtility;
    private final SummaryDailyUtility summaryDailyUtility;
    private final HttpServletRequest request;
    private final OldBalanceUtility oldBalanceUtility;
    private final GeneralUtility generalUtility;
    private final SummeryDailyRP summeryDailyRP;

    public StructureRS getReport() {
        TotalReportRQ reportRQ = new TotalReportRQ(request);
        UserToken userToken = jwtToken.getUserToken();

        if (!reportRQ.getFilterByUserCode().equals(LotteryConstant.ALL))
            return getSelectedUserReport(reportRQ, userToken);

        try {

            String userCode = userToken.getUserCode();
            String userType = userToken.getUserType().toLowerCase();

            if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
                userCode = userToken.getParentCode();
            }

            boolean isCanSeeUserOnline = true;
            if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
                isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
                if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                    isCanSeeUserOnline = true;
            }

            List<UserEntity> userEntities = userRP.getUserByFilter(userType, userCode, reportRQ.getFilterByLevel(), reportRQ.getFilterByUsername(), isCanSeeUserOnline);

            List<String> userCodes = userEntities.stream().map(UserEntity::getCode).collect(Collectors.toList());
            List<SummeryDailyEntity> summeryDailyEntities = summeryDailyRP.findAllByUserCodeIn(userCodes, reportRQ.getFilterByStartDate(), reportRQ.getFilterByEndDate());
            List<SettlementItemsEntity> settlementItemsEntities = settlementItemRP.getAllSettlement(userCodes, reportRQ.getFilterByStartDate(), reportRQ.getFilterByEndDate());
            Map<String, CurrencyRS> oldBalanceMap = oldBalanceUtility.getUserOldBalanceMapMix(userCodes, reportRQ.getFilterByEndDate());

            SettlementListRS settlementListRS = new SettlementListRS();

            settlementListRS.setIsEditable(settlementUtility.getIsEditableSettlement(userToken, reportRQ));

            SettlementTotalSummeryRS summery = new SettlementTotalSummeryRS();

            for (UserEntity userEntity : userEntities) {

                SettlementsItemListRS item = new SettlementsItemListRS();
                CurrencyRS summaryDaily = new CurrencyRS();

                item.setNickname(userEntity.getNickname());
                item.setUsername(userEntity.getUsername());
                item.setUserCode(userEntity.getCode());
                item.setRoleCode(userEntity.getRoleCode());

                for (SummeryDailyEntity summeryDailyEntity : summeryDailyEntities.stream().filter(it -> it.getUserCode().equals(userEntity.getCode())).collect(Collectors.toList())) {

                    if(summeryDailyEntity.getDetail() != null) {

                        SummaryRS summaryRS = summaryDailyUtility.getSummaryRSFromEntity(summeryDailyEntity);
                        List<BigDecimal> shared = summaryDailyUtility.getSharedBalanceFromSummeryDaily(summaryRS);

                        setUpDataFromSummeryDaily(summaryRS, item);

                        summaryDaily.setAmountKhr(summaryDaily.getAmountKhr().add(shared.get(0)));
                        summaryDaily.setAmountUsd(summaryDaily.getAmountUsd().add(shared.get(1)));

                    } else {

                        BigDecimal amountKhr = summeryDailyEntity.getTotalTurnoverKhr().subtract(summeryDailyEntity.getTotalRewardKhr());
                        BigDecimal amountUsd = summeryDailyEntity.getTotalTurnoverUsd().subtract(summeryDailyEntity.getTotalRewardUsd());

                        summaryDaily.setAmountKhr(summaryDaily.getAmountKhr().add(amountKhr));
                        summaryDaily.setAmountUsd(summaryDaily.getAmountUsd().add(amountUsd));

                    }

                }

                updateOldBalance(item, oldBalanceMap.get(userEntity.getCode()), summaryDaily, summery);

                settlementUtility.setSettlementItem(settlementItemsEntities.stream().filter(it -> it.getMemberCode().equals(userEntity.getCode())).collect(Collectors.toList()), summery, item);

                if (LotteryConstant.TOTAL.equalsIgnoreCase(reportRQ.getFilterByReportType())) {
                    item.setTotalAmountKhr(item.getOldAmountKhr().add(item.getShareKhr()).add(item.getGive().getAmountKhr()).add(item.getBorrow().getAmountKhr()).add(item.getProtestAmount().getAmountKhr()));
                    item.setTotalAmountUsd(item.getOldAmountUsd().add(item.getShareUsd()).add(item.getGive().getAmountUsd()).add(item.getBorrow().getAmountUsd()).add(item.getProtestAmount().getAmountUsd()));
                } else {
                    item.setTotalAmountKhr(item.getTotalAmountKhr().subtract(item.getGive().getAmountKhr()).subtract(item.getBorrow().getAmountKhr()).subtract(item.getProtestAmount().getAmountKhr()));
                    item.setTotalAmountUsd(item.getTotalAmountUsd().subtract(item.getGive().getAmountUsd()).subtract(item.getBorrow().getAmountUsd()).subtract(item.getProtestAmount().getAmountUsd()));
                }


                summery.addValueFromSettlementItemListRS(item);
                settlementListRS.getSettlements().add(item);
            }

            settlementListRS.setSummery(summery);

            return responseBodyWithSuccessMessage(settlementListRS);
        } catch (Exception exception) {
            exception.printStackTrace();
            return responseBodyWithBadRequest(MessageConstant.BAD_REQUEST, MessageConstant.BAD_REQUEST_KEY);
        }
    }

    private void setUpDataFromSummeryDaily(SummaryRS summeryDaily, SettlementsItemListRS item) {

        SummaryItemRS oneDKhr = summeryDaily.getOneD().getKhr();
        SummaryItemRS oneDUsd = summeryDaily.getOneD().getUsd();

        SummaryItemRS twoDKhr = summeryDaily.getTwoD().getKhr();
        SummaryItemRS twoDUsd = summeryDaily.getTwoD().getUsd();

        SummaryItemRS threeDKhr = summeryDaily.getThreeD().getKhr();
        SummaryItemRS threeDUsd = summeryDaily.getThreeD().getUsd();

        SummaryItemRS fourDKhr = summeryDaily.getFourD().getKhr();
        SummaryItemRS fourDUsd = summeryDaily.getFourD().getUsd();

        item.setBetAmount1DKhr(item.getBetAmount1DKhr().add(oneDKhr.getTotalSale()));
        item.setCom1DKhr(item.getCom1DKhr().add(oneDKhr.getCommission()));
        item.setRewardAmount1DKhr(item.getRewardAmount1DKhr().add(oneDKhr.getReward()));
        item.setWinAmount1DKhr(item.getWinAmount1DKhr().add(oneDKhr.getWinAmount()));
        item.setShare1DKhr(item.getShare1DKhr().add(oneDKhr.getShareAmount()));

        item.setBetAmount1DUsd(item.getBetAmount1DUsd().add(oneDUsd.getTotalSale()));
        item.setCom1DUsd(item.getCom1DUsd().add(oneDUsd.getCommission()));
        item.setRewardAmount1DUsd(item.getRewardAmount1DUsd().add(oneDUsd.getReward()));
        item.setWinAmount1DUsd(item.getWinAmount1DUsd().add(oneDUsd.getWinAmount()));
        item.setShare1DUsd(item.getShare1DUsd().add(oneDUsd.getShareAmount()));

        item.setBetAmount2DKhr(item.getBetAmount2DKhr().add(twoDKhr.getTotalSale()));
        item.setCom2DKhr(item.getCom2DKhr().add(twoDKhr.getCommission()));
        item.setRewardAmount2DKhr(item.getRewardAmount2DKhr().add(twoDKhr.getReward()));
        item.setWinAmount2DKhr(item.getWinAmount2DKhr().add(twoDKhr.getWinAmount()));
        item.setShare2DKhr(item.getShare2DKhr().add(twoDKhr.getShareAmount()));

        item.setBetAmount2DUsd(item.getBetAmount2DUsd().add(twoDUsd.getTotalSale()));
        item.setCom2DUsd(item.getCom2DUsd().add(twoDUsd.getCommission()));
        item.setRewardAmount2DUsd(item.getRewardAmount2DUsd().add(twoDUsd.getReward()));
        item.setWinAmount2DUsd(item.getWinAmount2DUsd().add(twoDUsd.getWinAmount()));
        item.setShare2DUsd(item.getShare2DUsd().add(twoDUsd.getShareAmount()));

        item.setBetAmount3DKhr(item.getBetAmount3DKhr().add(threeDKhr.getTotalSale()));
        item.setCom3DKhr(item.getCom3DKhr().add(threeDKhr.getCommission()));
        item.setRewardAmount3DKhr(item.getRewardAmount3DKhr().add(threeDKhr.getReward()));
        item.setWinAmount3DKhr(item.getWinAmount3DKhr().add(threeDKhr.getWinAmount()));
        item.setShare3DKhr(item.getShare3DKhr().add(threeDKhr.getShareAmount()));

        item.setBetAmount3DUsd(item.getBetAmount3DUsd().add(threeDUsd.getTotalSale()));
        item.setCom3DUsd(item.getCom3DUsd().add(threeDUsd.getCommission()));
        item.setRewardAmount3DUsd(item.getRewardAmount3DUsd().add(threeDUsd.getReward()));
        item.setWinAmount3DUsd(item.getWinAmount3DUsd().add(threeDUsd.getWinAmount()));
        item.setShare3DUsd(item.getShare3DUsd().add(threeDUsd.getShareAmount()));

        item.setBetAmount4DKhr(item.getBetAmount4DKhr().add(fourDKhr.getTotalSale()));
        item.setCom4DKhr(item.getCom4DKhr().add(fourDKhr.getCommission()));
        item.setRewardAmount4DKhr(item.getRewardAmount4DKhr().add(fourDKhr.getReward()));
        item.setWinAmount4DKhr(item.getWinAmount4DKhr().add(fourDKhr.getWinAmount()));
        item.setShare4DKhr(item.getShare4DKhr().add(fourDKhr.getShareAmount()));

        item.setBetAmount4DUsd(item.getBetAmount4DUsd().add(fourDUsd.getTotalSale()));
        item.setCom4DUsd(item.getCom4DUsd().add(fourDUsd.getCommission()));
        item.setRewardAmount4DUsd(item.getRewardAmount4DUsd().add(fourDUsd.getReward()));
        item.setWinAmount4DUsd(item.getWinAmount4DUsd().add(fourDUsd.getWinAmount()));
        item.setShare4DUsd(item.getShare4DUsd().add(fourDUsd.getShareAmount()));

    }

    private void updateOldBalance(SettlementsItemListRS item, CurrencyRS oldBalance, CurrencyRS summaryDaily, SettlementTotalSummeryRS totalSummeryRS) {
        if (oldBalance != null) {
            item.setOldAmountKhr(oldBalance.getAmountKhr());
            item.setOldAmountUsd(oldBalance.getAmountUsd());
            item.setWinLoseAmountKhr(item.getWinLoseAmountKhr().add(summaryDaily.getAmountKhr()));
            item.setWinLoseAmountUsd(item.getWinLoseAmountUsd().add(summaryDaily.getAmountUsd()));

            totalSummeryRS.setOldAmountKhr(totalSummeryRS.getOldAmountKhr().add(oldBalance.getAmountKhr()));
            totalSummeryRS.setOldAmountUsd(totalSummeryRS.getOldAmountUsd().add(oldBalance.getAmountUsd()));

            item.setTotalAmountKhr(item.getOldAmountKhr().add(item.getShareKhr()).add(item.getGive().getAmountKhr()).add(item.getBorrow().getAmountKhr()).add(item.getProtestAmount().getAmountKhr()));
            item.setTotalAmountUsd(item.getOldAmountUsd().add(item.getShareUsd()).add(item.getGive().getAmountUsd()).add(item.getBorrow().getAmountUsd()).add(item.getProtestAmount().getAmountUsd()));
        }
    }

    private StructureRS getSelectedUserReport(TotalReportRQ reportRQ, UserToken userToken) {
        UserEntity userEntity = userRP.findByCode(reportRQ.getFilterByUserCode());

        List<SummeryDailyEntity> summeryDailyEntities = summeryDailyRP.findAllByUserCodeIn(List.of(userEntity.getCode()), reportRQ.getFilterByStartDate(), reportRQ.getFilterByEndDate());
        List<SettlementItemsEntity> settlementItemsEntities = settlementItemRP.getAllSettlement(List.of(userEntity.getCode()), reportRQ.getFilterByStartDate(), reportRQ.getFilterByEndDate());

        SettlementListRS settlementListRS = new SettlementListRS();
        settlementListRS.setIsEditable(settlementUtility.getIsEditableSettlement(userToken, reportRQ));

        SettlementTotalSummeryRS summery = new SettlementTotalSummeryRS();

        Date startDate = generalUtility.parseDate(reportRQ.getFilterByStartDate());
        Date endDate = generalUtility.parseDate(reportRQ.getFilterByEndDate());

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(DateUtils.addDays(endDate, 1));

        for (Date date = start.getTime(); start.before(end); date = start.getTime()) {

            SettlementsItemListRS item = new SettlementsItemListRS();
            item.setUserCode(userEntity.getCode());
            CurrencyRS summaryDaily = new CurrencyRS();
            String currentDate = generalUtility.formatDateYYYYMMDD(date);


            for (SummeryDailyEntity summeryDailyEntity : summeryDailyEntities.stream().filter(it -> it.getUserCode().equals(userEntity.getCode()) && currentDate.equals(generalUtility.formatDateYYYYMMDD(it.getIssuedAt())) ).collect(Collectors.toList())) {

                if(summeryDailyEntity.getDetail() != null) {

                    SummaryRS summaryRS = summaryDailyUtility.getSummaryRSFromEntity(summeryDailyEntity);
                    List<BigDecimal> shared = summaryDailyUtility.getSharedBalanceFromSummeryDaily(summaryRS);

                    setUpDataFromSummeryDaily(summaryRS, item);

                    summaryDaily.setAmountKhr(summaryDaily.getAmountKhr().add(shared.get(0)));
                    summaryDaily.setAmountUsd(summaryDaily.getAmountUsd().add(shared.get(1)));

                } else {

                    BigDecimal amountKhr = summeryDailyEntity.getTotalTurnoverKhr().subtract(summeryDailyEntity.getTotalRewardKhr());
                    BigDecimal amountUsd = summeryDailyEntity.getTotalTurnoverUsd().subtract(summeryDailyEntity.getTotalRewardUsd());

                    summaryDaily.setAmountKhr(summaryDaily.getAmountKhr().add(amountKhr));
                    summaryDaily.setAmountUsd(summaryDaily.getAmountUsd().add(amountUsd));

                }

            }

            Map<String, CurrencyRS> oldBalanceMap = oldBalanceUtility.getUserOldBalanceMapMix(List.of(userEntity.getCode()), currentDate);

            BeanUtils.copyProperties(userEntity, item);
            item.setIssuedAt(currentDate);
            settlementUtility.setSettlementItem(settlementItemsEntities.stream().filter(it -> currentDate.equals(generalUtility.formatDateYYYYMMDD(it.getIssuedAt()))).collect(Collectors.toList()), summery, item);
            updateOldBalance(item, oldBalanceMap.get(userEntity.getCode()), summaryDaily, summery);

            settlementListRS.getSettlements().add(item);

            if (LotteryConstant.TOTAL.equalsIgnoreCase(reportRQ.getFilterByReportType())) {
                item.setTotalAmountKhr(item.getOldAmountKhr().add(item.getShareKhr()).add(item.getGive().getAmountKhr()).add(item.getBorrow().getAmountKhr()).add(item.getProtestAmount().getAmountKhr()));
                item.setTotalAmountUsd(item.getOldAmountUsd().add(item.getShareUsd()).add(item.getGive().getAmountUsd()).add(item.getBorrow().getAmountUsd()).add(item.getProtestAmount().getAmountUsd()));
            } else {
                item.setTotalAmountKhr(item.getTotalAmountKhr().subtract(item.getGive().getAmountKhr()).subtract(item.getBorrow().getAmountKhr()).subtract(item.getProtestAmount().getAmountKhr()));
                item.setTotalAmountUsd(item.getTotalAmountUsd().subtract(item.getGive().getAmountUsd()).subtract(item.getBorrow().getAmountUsd()).subtract(item.getProtestAmount().getAmountUsd()));
            }

            summery.addValueFromSettlementItemListRS(item);

            start.add(Calendar.DATE, 1);
        }

        // filter specific user in date range
        if (!LotteryConstant.ALL.equals(reportRQ.getFilterByUserCode()) && !reportRQ.getFilterByStartDate().equals(reportRQ.getFilterByEndDate())) {
            summery.setOldAmountKhr(BigDecimal.ZERO);
            summery.setOldAmountUsd(BigDecimal.ZERO);
            summery.setTotalKHR(BigDecimal.ZERO);
            summery.setTotalUSD(BigDecimal.ZERO);
        }

        settlementListRS.setSummery(summery);

        return responseBodyWithSuccessMessage(settlementListRS);
    }
}
