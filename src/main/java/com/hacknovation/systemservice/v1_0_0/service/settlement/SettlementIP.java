package com.hacknovation.systemservice.v1_0_0.service.settlement;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.enums.FinanceTypeEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.SettlementItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.SettlementItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.settlement.EditSettlementRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.MainSaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementTotalSummeryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementsItemListRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.DailyFormat2ReportUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.OldBalanceUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.SettlementUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.SummaryDailyUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementIP extends BaseServiceIP implements SettlementSV {

    private final UserRP userRP;
    private final UserNQ userNQ;
    private final JwtToken jwtToken;
    private final SettlementItemRP settlementItemRP;
    private final SettlementUtility settlementUtility;
    private final SummaryDailyUtility summaryDailyUtility;
    private final DailyFormat2ReportUtility dailyFormat2ReportUtility;
    private final HttpServletRequest request;
    private final OldBalanceUtility oldBalanceUtility;
    private final GeneralUtility generalUtility;
    private final ActivityLogUtility activityLogUtility;

    @Override
    public StructureRS listing() {
        ReportRQ reportRQ = new ReportRQ(request);
        UserToken userToken = jwtToken.getUserToken();

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

            /*
             * Get member codes
             */
            List<UserLevelReportTO> filterMemberTOS = userNQ.userLevelFilter(userCode, userType, LotteryConstant.ALL, UserConstant.MEMBER.toLowerCase(), reportRQ.getFilterByUserCode(), isCanSeeUserOnline);

            /*
             * Get user by level
             */
            List<UserLevelReportTO> userLevelReportTOS = userNQ.userReferralCodesList(userCode, userType, reportRQ.getFilterByLevel(), reportRQ.getFilterByUserCode(), reportRQ.getFilterByUsername());
            List<String> userLevelCodes = userLevelReportTOS.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());

            List<SettlementItemsEntity> settlementItemsEntities = settlementItemRP.listing(reportRQ.getFilterByLotteryType(), userLevelCodes, reportRQ.getFilterByStartDate(), reportRQ.getFilterByEndDate());

            Map<String, Map<String, CurrencyRS>> summaryDailyMapByUserAndDate = summaryDailyUtility.getSummaryDailyGroupByUserCodeAndDateMap(reportRQ.getFilterByLotteryType(), userLevelCodes, reportRQ.getFilterByStartDate(), reportRQ.getFilterByEndDate());

            /*
             * get old balance user
             */
            Map<String, CurrencyRS> oldBalanceMap = oldBalanceUtility.getUserOldBalanceMap(reportRQ.getFilterByLotteryType(), userLevelCodes, reportRQ.getFilterByEndDate());
            Date nextDate = DateUtils.addDays(generalUtility.parseDate(reportRQ.getFilterByEndDate()), 1);
            Map<String, CurrencyRS> totalBalanceMapAfterEndDate = oldBalanceUtility.getUserOldBalanceMap(reportRQ.getFilterByLotteryType(), userLevelCodes, generalUtility.formatDateYYYYMMDD(nextDate));

            MainSaleReportRS mainSaleReportRS = new MainSaleReportRS();
            if (LotteryConstant.TOTAL.equalsIgnoreCase(reportRQ.getFilterByReportType())) {
                mainSaleReportRS = dailyFormat2ReportUtility.memberReportResponse(reportRQ, userLevelReportTOS, filterMemberTOS);
            }

            Date startDate = generalUtility.parseDate(reportRQ.getFilterByStartDate());
            Date endDate = generalUtility.parseDate(reportRQ.getFilterByEndDate());

            Calendar start = Calendar.getInstance();
            start.setTime(startDate);
            Calendar end = Calendar.getInstance();
            end.setTime(DateUtils.addDays(endDate, 1));

            List<SettlementsItemListRS> itemListRS = new ArrayList<>();
            SettlementTotalSummeryRS totalSummeryRS = new SettlementTotalSummeryRS();

            if (UserConstant.ALL.equalsIgnoreCase(reportRQ.getFilterByUserCode())) {
                for (UserLevelReportTO user : userLevelReportTOS) {
                    CurrencyRS summaryDaily = new CurrencyRS();
                    Map<String, CurrencyRS> summaryMap = summaryDailyMapByUserAndDate.get(user.getUserCode());
                    /*
                     * get winLose by day
                     */
                    String lastDate = generalUtility.formatDateYYYYMMDD(generalUtility.parseDate(reportRQ.getFilterByEndDate()));
                    if (summaryMap != null) {
                        if (summaryMap.containsKey(lastDate)) {
                            BeanUtils.copyProperties(summaryMap.get(lastDate), summaryDaily);
                        }
                    }

                    SettlementsItemListRS itemListRS1 = new SettlementsItemListRS();
                    BeanUtils.copyProperties(user, itemListRS1);

                    List<SettlementItemsEntity> settlementItemsEntityList = settlementItemsEntities.stream().filter(it -> it.getMemberCode().equals(user.getUserCode())).collect(Collectors.toList());
                    settlementUtility.setSettlementItem(settlementItemsEntityList, totalSummeryRS, itemListRS1);
                    updateOldBalance(itemListRS1, oldBalanceMap.get(user.getUserCode()), summaryDaily, totalSummeryRS);

                    /*
                     * update bet amount and win amount
                     */
                    if (LotteryConstant.TOTAL.equalsIgnoreCase(reportRQ.getFilterByReportType())) {
                        List<SaleItemsRS> saleItemsRSList = mainSaleReportRS.getUnderLineSale().getItems().stream().filter(item -> item.getUserCode().equals(user.getUserCode())).collect(Collectors.toList());
                        settlementUtility.setSettlementSale(itemListRS1, saleItemsRSList);

                        /*
                         * update total amount with end date
                         */
                        CurrencyRS totalAmount = totalBalanceMapAfterEndDate.get(user.getUserCode());
                        itemListRS1.setTotalAmountKhr(totalAmount.getAmountKhr());
                        itemListRS1.setTotalAmountUsd(totalAmount.getAmountUsd());

                    } else {
                        itemListRS1.setTotalAmountKhr(itemListRS1.getTotalAmountKhr().subtract(itemListRS1.getGive().getAmountKhr()).subtract(itemListRS1.getBorrow().getAmountKhr()).subtract(itemListRS1.getProtestAmount().getAmountKhr()));
                        itemListRS1.setTotalAmountUsd(itemListRS1.getTotalAmountUsd().subtract(itemListRS1.getGive().getAmountUsd()).subtract(itemListRS1.getBorrow().getAmountUsd()).subtract(itemListRS1.getProtestAmount().getAmountUsd()));
                    }

                    totalSummeryRS.addValueFromSettlementItemListRS(itemListRS1);

                    itemListRS.add(itemListRS1);
                }
            } else {
                for (UserLevelReportTO user : userLevelReportTOS) {
                    Map<String, CurrencyRS> summaryDailyMapByUser = summaryDailyMapByUserAndDate.get(user.getUserCode());
                    for (Date date = start.getTime(); start.before(end); date = start.getTime()) {

                        CurrencyRS summaryDaily = new CurrencyRS();
                        String currentDate = generalUtility.formatDateYYYYMMDD(date);

                        /*
                         * get winLose by day
                         */
                        if (summaryDailyMapByUser != null) {
                            if (summaryDailyMapByUser.containsKey(currentDate)) {
                                BeanUtils.copyProperties(summaryDailyMapByUser.get(currentDate), summaryDaily);
                            }
                        }
                        /*
                         * get old balance
                         */
                        oldBalanceMap = oldBalanceUtility.getUserOldBalanceMap(reportRQ.getFilterByLotteryType(), List.of(user.getUserCode()), currentDate);

                        List<SettlementItemsEntity> settlementItemsEntityList = settlementItemsEntities.stream().filter(it -> currentDate.equals(generalUtility.formatDateYYYYMMDD(it.getIssuedAt())) && it.getMemberCode().equals(user.getUserCode())).collect(Collectors.toList());
                        SettlementsItemListRS itemListRS1 = new SettlementsItemListRS();
                        BeanUtils.copyProperties(user, itemListRS1);
                        itemListRS1.setIssuedAt(currentDate);
                        settlementUtility.setSettlementItem(settlementItemsEntityList, totalSummeryRS, itemListRS1);
                        updateOldBalance(itemListRS1, oldBalanceMap.get(user.getUserCode()), summaryDaily, totalSummeryRS);

                        /*
                         * update bet amount and win amount
                         */
                        if (LotteryConstant.TOTAL.equalsIgnoreCase(reportRQ.getFilterByReportType())) {
                            List<SaleItemsRS> saleItemsRSList = mainSaleReportRS.getUnderLineSale().getItems().stream().filter(item -> item.getUserCode().equals(user.getUserCode()) && generalUtility.formatDateYYYYMMDD(item.getDrawAt()).equals(currentDate)).collect(Collectors.toList());
                            settlementUtility.setSettlementSale(itemListRS1, saleItemsRSList);
                        } else {
                            itemListRS1.setTotalAmountKhr(itemListRS1.getTotalAmountKhr().subtract(itemListRS1.getGive().getAmountKhr()).subtract(itemListRS1.getBorrow().getAmountKhr()).subtract(itemListRS1.getProtestAmount().getAmountKhr()));
                            itemListRS1.setTotalAmountUsd(itemListRS1.getTotalAmountUsd().subtract(itemListRS1.getGive().getAmountUsd()).subtract(itemListRS1.getBorrow().getAmountUsd()).subtract(itemListRS1.getProtestAmount().getAmountUsd()));
                        }

                        totalSummeryRS.addValueFromSettlementItemListRS(itemListRS1);

                        itemListRS.add(itemListRS1);

                        start.add(Calendar.DATE, 1);
                    }
                }
            }

            SettlementListRS settlementListRS = new SettlementListRS();
            settlementListRS.setSettlements(itemListRS);
            settlementListRS.setIsEditable(settlementUtility.getIsEditableSettlement(userToken, reportRQ));

            // filter specific user in date range
            if (!LotteryConstant.ALL.equals(reportRQ.getFilterByUserCode()) && !reportRQ.getFilterByStartDate().equals(reportRQ.getFilterByEndDate())) {
                totalSummeryRS.setOldAmountKhr(BigDecimal.ZERO);
                totalSummeryRS.setOldAmountUsd(BigDecimal.ZERO);
                totalSummeryRS.setTotalKHR(BigDecimal.ZERO);
                totalSummeryRS.setTotalUSD(BigDecimal.ZERO);
            }
            settlementListRS.setSummery(totalSummeryRS);

            return responseBodyWithSuccessMessage(settlementListRS);

        } catch (Exception exception) {
            exception.printStackTrace();
            return responseBodyWithBadRequest(MessageConstant.BAD_REQUEST, MessageConstant.BAD_REQUEST_KEY);
        }
    }

    private void updateOldBalance(SettlementsItemListRS itemListRS, CurrencyRS oldBalance, CurrencyRS summaryDaily, SettlementTotalSummeryRS totalSummeryRS) {
        if (oldBalance != null) {
            itemListRS.setOldAmountKhr(oldBalance.getAmountKhr());
            itemListRS.setOldAmountUsd(oldBalance.getAmountUsd());
            itemListRS.setWinLoseAmountKhr(summaryDaily.getAmountKhr());
            itemListRS.setWinLoseAmountUsd(summaryDaily.getAmountUsd());

            totalSummeryRS.setOldAmountKhr(totalSummeryRS.getOldAmountKhr().add(oldBalance.getAmountKhr()));
            totalSummeryRS.setOldAmountUsd(totalSummeryRS.getOldAmountUsd().add(oldBalance.getAmountUsd()));

            itemListRS.setTotalAmountKhr(itemListRS.getOldAmountKhr().add(itemListRS.getWinLoseAmountKhr()).add(itemListRS.getGive().getAmountKhr()).add(itemListRS.getBorrow().getAmountKhr()).add(itemListRS.getProtestAmount().getAmountKhr()));
            itemListRS.setTotalAmountUsd(itemListRS.getOldAmountUsd().add(itemListRS.getWinLoseAmountUsd()).add(itemListRS.getGive().getAmountUsd()).add(itemListRS.getBorrow().getAmountUsd()).add(itemListRS.getProtestAmount().getAmountUsd()));
        }
    }

    @Override
    public StructureRS addOrEdit(EditSettlementRQ editSettlementRQ) {
        try {

            String actionType = "ADD";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date issuedAt = formatter.parse(editSettlementRQ.getDate() + " 07:00:00");
            Date currentDate = formatter.parse(formatter.format(new Date()));
            Date issuedAtNextTwoDay = DateUtils.addDays(issuedAt, 5);

            if (issuedAtNextTwoDay.getTime() < currentDate.getTime())
                return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.SELECTED_DATE_EXPIRED);

            var userToken = jwtToken.getUserToken();
            SettlementItemsEntity settlementItemsEntity;
            if (editSettlementRQ.getItemId() != null) {
                settlementItemsEntity = settlementItemRP.getOne(editSettlementRQ.getItemId());
                settlementItemsEntity.setAmountKhr(editSettlementRQ.getAmountKhr());
                settlementItemsEntity.setAmountUsd(editSettlementRQ.getAmountUsd());
                settlementItemsEntity.setUpdatedBy(userToken.getUserCode());
                actionType = "EDIT";
            } else {
                UserEntity userEntity = userRP.findByCode(editSettlementRQ.getUserCode());
                if (userEntity == null)
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.USER_COULD_NOT_BE_FOUND);

                FinanceTypeEnum type = getType(editSettlementRQ.getType());
                if (type == null)
                    return responseBody(HttpStatus.BAD_REQUEST, MessageConstant.UNKNOWN_TYPE);

                settlementItemsEntity = new SettlementItemsEntity();
                settlementItemsEntity.setAmountUsd(editSettlementRQ.getAmountUsd());
                settlementItemsEntity.setAmountKhr(editSettlementRQ.getAmountKhr());
                settlementItemsEntity.setType(type);
                settlementItemsEntity.setIssuedAt(issuedAt);

                settlementItemsEntity.setLotteryCode(editSettlementRQ.getLotteryType());
                settlementItemsEntity.setMemberCode(userEntity.getCode());
                settlementItemsEntity.setCreatedBy(userToken.getUserCode());
                settlementItemsEntity.setUpdatedBy(userToken.getUserCode());
            }
            List<String> minusSign = List.of("return", "returns", "give", "underProtest");
            List<String> plusSign = List.of("owed", "borrow", "upperProtest");
            if (minusSign.contains(editSettlementRQ.getType())) {
                settlementItemsEntity.setAmountUsd(BigDecimal.valueOf(Math.abs(editSettlementRQ.getAmountUsd().doubleValue())).multiply(BigDecimal.valueOf(-1)));
                settlementItemsEntity.setAmountKhr(BigDecimal.valueOf(Math.abs(editSettlementRQ.getAmountKhr().doubleValue())).multiply(BigDecimal.valueOf(-1)));
            } else if (plusSign.contains(editSettlementRQ.getType())) {
                settlementItemsEntity.setAmountUsd(BigDecimal.valueOf(Math.abs(editSettlementRQ.getAmountUsd().doubleValue())));
                settlementItemsEntity.setAmountKhr(BigDecimal.valueOf(Math.abs(editSettlementRQ.getAmountKhr().doubleValue())));
            } else {
                settlementItemsEntity.setAmountUsd(editSettlementRQ.getAmountUsd());
                settlementItemsEntity.setAmountKhr(editSettlementRQ.getAmountKhr());
            }

            activityLogUtility.addActivityLog(settlementItemsEntity.getLotteryCode(), ActivityLogConstant.MODULE_SETTLEMENT, userToken.getUserType(), actionType, userToken.getUserCode(), settlementItemRP.save(settlementItemsEntity));

            return responseBodyWithSuccessMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return responseBody(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private FinanceTypeEnum getType(String type) {
        switch (type) {
            case "returns":
            case "return":
                return FinanceTypeEnum.RETURN;
            case "owed":
                return FinanceTypeEnum.OWED;
            case "borrow":
                return FinanceTypeEnum.BORROW;
            case "give":
                return FinanceTypeEnum.GIVE;
            case "underProtest":
                return FinanceTypeEnum.UNDER_PROTEST;
            case "protestAmount":
                return FinanceTypeEnum.PROTEST_AMOUNT;
            case "upperProtest":
                return FinanceTypeEnum.UPPER_PROTEST;
            case "leap":
                return FinanceTypeEnum.LEAP;
            case "mt":
                return FinanceTypeEnum.MT;
            case "vn1":
                return FinanceTypeEnum.VN1;
            case "kh":
                return FinanceTypeEnum.KH;
            case "tn":
                return FinanceTypeEnum.TN;
            default:
                return null;
        }
    }

}
