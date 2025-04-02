package com.hacknovation.systemservice.v2_0_0.component.dailyreport;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.TrackUserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.ReportRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.HasLotteryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SaleReportRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.report.sale.SummaryReportRS;
import com.hacknovation.systemservice.v1_0_0.utility.DateUtil;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.SqlUtility;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.OldBalanceUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.SettlementUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.UserHasLotteryJsonUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Sombath
 * create at 25/4/23 12:22 PM
 */
@Service
@RequiredArgsConstructor
public class AllDailyReportService extends BaseServiceIP {

    private final GeneralUtility generalUtility;
    private final OldBalanceUtility oldBalanceUtility;
    private final SettlementUtility settlementUtility;
    private final UpperUserCodeUtility upperUserCodeUtility;
    private final UserHasLotteryJsonUtility userHasLotteryJsonUtility;
    private final HttpServletRequest request;
    private final JwtToken jwtToken;
    private final SqlUtility sqlUtility;

    @Value("classpath:query/report/temp-daily-report.sql")
    private Resource tempReportResource;

    @Value("classpath:query/report/daily-report.sql")
    private Resource reportResource;

    @Value("classpath:query/draw/get-all-draw-by-date.sql")
    private Resource drawingResource;

    @Value("classpath:query/draw/temp-get-all-draw-by-date.sql")
    private Resource tempDrawingResource;

    @Value("classpath:query/user/user-level.sql")
    private Resource userFilterResource;

    public StructureRS getReport() {

        ReportRQ reportRQ = new ReportRQ(request);
        UserToken userToken = jwtToken.getUserToken();
        String userType = userToken.getUserType().toLowerCase();

        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userToken.setUserCode(userToken.getParentCode());
        }

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        boolean isTemp = generalUtility.isTempTable(reportRQ.getFilterByStartDate());
        Resource resource = isTemp ? tempDrawingResource : drawingResource;

        List<DrawingDTO> draw = sqlUtility.executeQueryForList(resource, Map.of("filterDate", reportRQ.getFilterByStartDate()), DrawingDTO.class);
        Map<String, List<DrawingDTO>> drawByLottery = draw.stream().collect(Collectors.groupingBy(DrawingDTO::getLottery));


        Map<String, Object> params = new HashMap<>();
        params.put("userCode", userToken.getUserCode());
        params.put("userType", userType);
        params.put("filterByUsername", reportRQ.getFilterByUsername());
        params.put("filterUserLevel", UserConstant.MEMBER);
        params.put("filterUserCode", reportRQ.getFilterByUserCode());
        params.put("userOnline", isCanSeeUserOnline);

        // all member by login user
        List<UserLevelReportTO> members = sqlUtility.executeQueryForList(userFilterResource, params, UserLevelReportTO.class);
        List<String> memberCodes = members.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());

        // get list filtered user
        params.put("filterUserLevel", reportRQ.getFilterByLevel());
        List<UserLevelReportTO> users = sqlUtility.executeQueryForList(userFilterResource, params, UserLevelReportTO.class);

        Map<String, List<String>> memberCodeByTheirParent = new HashMap<>();
        for (UserLevelReportTO user : users) {
            Predicate<UserLevelReportTO> predicate = (m) -> m.getSuperSeniorCode().equals(user.getUserCode()) ||
                    m.getSeniorCode().equals(user.getUserCode()) ||
                    m.getMasterCode().equals(user.getUserCode()) ||
                    m.getAgentCode().equals(user.getUserCode()) ||
                    m.getUserCode().equals(user.getUserCode());
            List<String> _memberCodes = members.stream().filter(predicate).map(UserLevelReportTO::getUserCode).collect(Collectors.toList());
            memberCodeByTheirParent.put(user.getUserCode(), _memberCodes);
        }

        // betting data
        params = new HashMap<>();
        params.put("memberCodes", memberCodes);
        params.put("startDate", DateUtil.getDateUTC(reportRQ.getFilterByStartDate(), DateUtil.START_OF_DAY));
        params.put("endDate", DateUtil.getDateUTC(reportRQ.getFilterByStartDate(), DateUtil.END_OF_DAY));

        List<DailyReportTO> data = sqlUtility.executeQueryForList(isTemp ? tempReportResource : reportResource, params, DailyReportTO.class);
        Map<String, List<DailyReportTO>> dataByLottery = data.stream().collect(Collectors.groupingBy(DailyReportTO::getLottery));

        List<String> upperCodes = users.stream().map(UpperUserCodeUtility::staticUpperUserCode).collect(Collectors.toList());
        List<String> underCodes = users.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());

        DailyReportRS dailyReportRS = new DailyReportRS();
        for (String lottery : LotteryConstant.ALL_LOTTERY) {
            reportRQ.setFilterByLotteryType(lottery);
                List<DailyReportTO> _data = dataByLottery.getOrDefault(lottery, Collections.emptyList());
                DailyReportItemsRS dailyReportItemsRS = getItemPerLottery(users, drawByLottery.getOrDefault(lottery, Collections.emptyList()), _data, memberCodeByTheirParent, reportRQ, underCodes, upperCodes);

                dailyReportItemsRS.setLotteryType(lottery);
                dailyReportRS.getUpperSummary().add(dailyReportItemsRS.getUpperLineSale().getSummary());
                dailyReportRS.getUnderSummary().add(dailyReportItemsRS.getUnderLineSale().getSummary());

                if (!dailyReportItemsRS.getUnderLineSale().getItems().isEmpty())
                    dailyReportRS.getItems().add(dailyReportItemsRS);
        }

        return response(dailyReportRS);
    }

    private DailyReportItemsRS getItemPerLottery(List<UserLevelReportTO> user, List<DrawingDTO> drawingDTOS, List<DailyReportTO> data, Map<String, List<String>> memberCodeByTheirParent, ReportRQ reportRQ, List<String> underCodes, List<String> upperCodes) {
        /*
         * get user has lottery from track table
         */
        List<Long> hasLotteryIds = data.stream().map(DailyReportTO::getHasLotteryId).collect(Collectors.toList());
        Map<Long, List<DailyReportTO>> orderDTOMapById = data.stream().collect(Collectors.groupingBy(DailyReportTO::getOrderId));
        List<TrackUserHasLotteryEntity> trackUserHasLotteryEntities = userHasLotteryJsonUtility.getTrackUserHasLotteryEntitiesByIdIn(hasLotteryIds);
        Map<Long, TrackUserHasLotteryEntity> trackUserHasLotteryEntityMapByOrderId = userHasLotteryJsonUtility._getTrackUserHasLotteryByOrderId(orderDTOMapById, trackUserHasLotteryEntities);

        /*
         * Underline Sale Report
         */
        SummaryReportRS summaryUnderLine = new SummaryReportRS();
        SummaryReportRS summaryUpperLine = new SummaryReportRS();
        List<SaleItemsRS> underLineItemsRS = new ArrayList<>();
        List<SaleItemsRS> upperLineItemsRS = new ArrayList<>();

        for (UserLevelReportTO userUnder : user) {
            for (DrawingDTO draw : drawingDTOS) {
                List<DailyReportTO> memberReportTOS = data.stream().filter(item -> item.getDrawCode().equals(draw.getDrawCode()) && memberCodeByTheirParent.get(userUnder.getUserCode()).contains(item.getUserCode())).collect(Collectors.toList());
                addSaleItemRSFromListMemberTO(memberReportTOS, userUnder, underLineItemsRS, upperLineItemsRS, draw, summaryUnderLine, summaryUpperLine, trackUserHasLotteryEntityMapByOrderId);
            }
        }

        SaleReportRS underLineReport = new SaleReportRS();
        underLineReport.setLotteryType(LotteryConstant.ALL);
        underLineReport.setRoleCode(reportRQ.getFilterByLevel().toUpperCase());
        underLineReport.setNickname(LotteryConstant.ALL);
        underLineReport.setUsername(LotteryConstant.ALL);
        if (!LotteryConstant.ALL.equals(reportRQ.getFilterByUserCode())) {
            if (user.size() == 1) {
                UserLevelReportTO userTO = user.get(0);
                underLineReport.setNickname(userTO.getNickname());
                underLineReport.setUsername(userTO.getUsername());
            }
        }

        HasLotteryRS hasLotteryRSUnder = new HasLotteryRS();
        if (underCodes.stream().distinct().count() == 1) {
            if(!trackUserHasLotteryEntities.isEmpty()) {
                userHasLotteryJsonUtility.setUserHasLotteryRS(reportRQ.getFilterByLevel(), hasLotteryRSUnder, trackUserHasLotteryEntities.get(0));
                underLineReport.setIsShowHasLottery(true);
            }
        }

        underLineReport.setDate(reportRQ.getFilterByStartDate());
        underLineReport.setHasLottery(hasLotteryRSUnder);
        underLineReport.setSummary(summaryUnderLine);
        underLineReport.setItems(underLineItemsRS);

        underLineItemsRS.removeIf(item -> BigDecimal.ZERO.compareTo(item.getWinLoseAmountKhr().add(item.getWinLoseAmountUsd())) == 0);
        upperLineItemsRS.removeIf(item -> BigDecimal.ZERO.compareTo(item.getWinLoseAmountKhr().add(item.getWinLoseAmountUsd())) == 0);

        /*
         * Transform data from underline to upper line
         */
        SaleReportRS upperLineReport = new SaleReportRS();
        BeanUtils.copyProperties(underLineReport, upperLineReport);
        upperLineReport.setLotteryType(reportRQ.getFilterByLotteryType());
        HasLotteryRS hasLotteryRSUpper = new HasLotteryRS();
        if (upperCodes.stream().distinct().count() == 1) {
            if(!trackUserHasLotteryEntities.isEmpty()) {
                userHasLotteryJsonUtility.setUserHasLotteryRS(upperUserCodeUtility.upperLevelRole(reportRQ.getFilterByLevel()), hasLotteryRSUpper, trackUserHasLotteryEntities.get(0));
                upperLineReport.setIsShowHasLottery(true);
            }
        }
        upperLineReport.setHasLottery(hasLotteryRSUpper);
        upperLineReport.setItems(upperLineItemsRS);
        upperLineReport.setSummary(summaryUpperLine);

        /*
         * update old balance for underLine & upperLine
         */
        oldBalanceUtility.updateOldBalance(underLineReport, underCodes, reportRQ);
        // calculate upper when show all user
        if (LotteryConstant.ALL.equalsIgnoreCase(reportRQ.getFilterByUserCode())) {
            oldBalanceUtility.updateOldBalance(upperLineReport, upperCodes, reportRQ);
        }

        /*
         * update settlement items give and borrow
         */
        settlementUtility.updateSettlementItemSummaryRS(underLineReport, underCodes, reportRQ);
        // calculate upper when show all user
        if (LotteryConstant.ALL.equalsIgnoreCase(reportRQ.getFilterByUserCode())) {
            settlementUtility.updateSettlementItemSummaryRS(upperLineReport, upperCodes, reportRQ);
        }

        /*
         * Main sale report
         */
        DailyReportItemsRS dailyReportItemsRS = new DailyReportItemsRS();
        dailyReportItemsRS.setUnderLineSale(underLineReport);
        dailyReportItemsRS.setUpperLineSale(upperLineReport);
        return dailyReportItemsRS;
    }

    private void setItemAmount(UserLevelReportTO user, List<DailyReportTO> data, List<SaleItemsRS> underItemsRS, List<SaleItemsRS> upperItemRS, DrawingDTO drawingDTO, SummaryReportRS underSummaryReportRS, SummaryReportRS upperSummaryReportRS, Map<Long, TrackUserHasLotteryEntity> trackUserHasLotteryEntityMapByOrderId) {
        SaleItemsRS underSaleItemsRS = new SaleItemsRS();
        BeanUtils.copyProperties(user, underSaleItemsRS);
        underSaleItemsRS.setDrawCode(drawingDTO.getDrawCode());
        underSaleItemsRS.setDrawAt(drawingDTO.getResultedPostAt());

        SaleItemsRS upperSaleItemsRS = new SaleItemsRS();
        BeanUtils.copyProperties(underSaleItemsRS, upperSaleItemsRS);

        HasLotteryRS underHasLotteryRS = new HasLotteryRS();
        HasLotteryRS upperHasLotteryRS = new HasLotteryRS();
        String upperLevelRole = upperUserCodeUtility.upperLevelRole(user.getRoleCode());
        for (DailyReportTO sale : data) {

            /*
             * update hasLotteryRSByUser base on order id
             */
            userHasLotteryJsonUtility.setUserHasLotteryRS(user.getRoleCode(), underHasLotteryRS, trackUserHasLotteryEntityMapByOrderId.get(sale.getOrderId()));
            userHasLotteryJsonUtility.setUserHasLotteryRS(upperLevelRole, upperHasLotteryRS, trackUserHasLotteryEntityMapByOrderId.get(sale.getOrderId()));

            updateSaleItemRSByMemberReportTO(underSaleItemsRS, sale, underHasLotteryRS, user.getRoleCode());
            updateSaleItemRSByMemberReportTO(upperSaleItemsRS, sale, upperHasLotteryRS, upperLevelRole);

        }

        /*
         * under level
         */
        underSaleItemsRS.setWinLoseAmountKhr(underSaleItemsRS.getCommissionKhr().subtract(underSaleItemsRS.getRewardAmountKhr()));
        underSaleItemsRS.setWinLoseAmountUsd(underSaleItemsRS.getCommissionUsd().subtract(underSaleItemsRS.getRewardAmountUsd()));
        underItemsRS.add(underSaleItemsRS);
        updateSummaryRSBySaleItemRS(underSummaryReportRS, underSaleItemsRS);

        /*
         * upper level
         */
        upperSaleItemsRS.setWinLoseAmountKhr(upperSaleItemsRS.getCommissionKhr().subtract(upperSaleItemsRS.getRewardAmountKhr()));
        upperSaleItemsRS.setWinLoseAmountUsd(upperSaleItemsRS.getCommissionUsd().subtract(upperSaleItemsRS.getRewardAmountUsd()));
        upperItemRS.add(upperSaleItemsRS);
        updateSummaryRSBySaleItemRS(upperSummaryReportRS, upperSaleItemsRS);
    }

    private void updateSaleItemRSByMemberReportTO(SaleItemsRS saleItemsRS, DailyReportTO sale, HasLotteryRS hasLotteryRS, String roleCode) {
        BigDecimal com1DKhr = sale.getCom1DKhr();
        BigDecimal com2DKhr = sale.getCom2DKhr();
        BigDecimal com3DKhr = sale.getCom3DKhr();
        BigDecimal com4DKhr = sale.getCom4DKhr();
        BigDecimal com1DUsd = sale.getCom1DUsd();
        BigDecimal com2DUsd = sale.getCom2DUsd();
        BigDecimal com3DUsd = sale.getCom3DUsd();
        BigDecimal com4DUsd = sale.getCom4DUsd();

        BigDecimal rewardAmount1DKhr = sale.getRewardAmount1DKhr();
        BigDecimal rewardAmount2DKhr = sale.getRewardAmount2DKhr();
        BigDecimal rewardAmount3DKhr = sale.getRewardAmount3DKhr();
        BigDecimal rewardAmount4DKhr = sale.getRewardAmount4DKhr();
        BigDecimal rewardAmount1DUsd = sale.getRewardAmount1DUsd();
        BigDecimal rewardAmount2DUsd = sale.getRewardAmount2DUsd();
        BigDecimal rewardAmount3DUsd = sale.getRewardAmount3DUsd();
        BigDecimal rewardAmount4DUsd = sale.getRewardAmount4DUsd();

        if (!UserConstant.MEMBER.equalsIgnoreCase(roleCode)) {
            com1DKhr = generalUtility.commissionAmount(sale.getBetAmount1DKhr(), hasLotteryRS.getCom1D());
            com2DKhr = generalUtility.commissionAmount(sale.getBetAmount2DKhr(), hasLotteryRS.getCom2D());
            com3DKhr = generalUtility.commissionAmount(sale.getBetAmount3DKhr(), hasLotteryRS.getCom3D());
            com4DKhr = generalUtility.commissionAmount(sale.getBetAmount4DKhr(), hasLotteryRS.getCom4D());
            com1DUsd = generalUtility.commissionAmount(sale.getBetAmount1DUsd(), hasLotteryRS.getCom1D());
            com2DUsd = generalUtility.commissionAmount(sale.getBetAmount2DUsd(), hasLotteryRS.getCom2D());
            com3DUsd = generalUtility.commissionAmount(sale.getBetAmount3DUsd(), hasLotteryRS.getCom3D());
            com4DUsd = generalUtility.commissionAmount(sale.getBetAmount4DUsd(), hasLotteryRS.getCom4D());

            rewardAmount1DKhr = sale.getWinAmount1DKhr().multiply(hasLotteryRS.getRebateRate1D());
            rewardAmount2DKhr = sale.getWinAmount2DKhr().multiply(hasLotteryRS.getRebateRate2D());
            rewardAmount3DKhr = sale.getWinAmount3DKhr().multiply(hasLotteryRS.getRebateRate3D());
            rewardAmount4DKhr = sale.getWinAmount4DKhr().multiply(hasLotteryRS.getRebateRate4D());
            rewardAmount1DUsd = sale.getWinAmount1DUsd().multiply(hasLotteryRS.getRebateRate1D());
            rewardAmount2DUsd = sale.getWinAmount2DUsd().multiply(hasLotteryRS.getRebateRate2D());
            rewardAmount3DUsd = sale.getWinAmount3DUsd().multiply(hasLotteryRS.getRebateRate3D());
            rewardAmount4DUsd = sale.getWinAmount4DUsd().multiply(hasLotteryRS.getRebateRate4D());

        }

        BigDecimal share1DKhr = com1DKhr.subtract(rewardAmount1DKhr).multiply(hasLotteryRS.getShare1D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);
        BigDecimal share2DKhr = com2DKhr.subtract(rewardAmount2DKhr).multiply(hasLotteryRS.getShare2D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);
        BigDecimal share3DKhr = com3DKhr.subtract(rewardAmount3DKhr).multiply(hasLotteryRS.getShare3D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);
        BigDecimal share4DKhr = com4DKhr.subtract(rewardAmount4DKhr).multiply(hasLotteryRS.getShare4D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);

        BigDecimal share1DUsd = com1DUsd.subtract(rewardAmount1DUsd).multiply(hasLotteryRS.getShare1D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);
        BigDecimal share2DUsd = com2DUsd.subtract(rewardAmount2DUsd).multiply(hasLotteryRS.getShare2D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);
        BigDecimal share3DUsd = com3DUsd.subtract(rewardAmount3DUsd).multiply(hasLotteryRS.getShare3D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);
        BigDecimal share4DUsd = com4DUsd.subtract(rewardAmount4DUsd).multiply(hasLotteryRS.getShare4D()).divide(BigDecimal.valueOf(100), 3, RoundingMode.CEILING);

//        if(hasLotteryRS.getShare1D().compareTo(BigDecimal.ZERO) == 0) {
//            share1DKhr = com1DKhr.subtract(rewardAmount1DKhr);
//            share1DUsd = com1DUsd.subtract(rewardAmount1DUsd);
//        }
//
//        if(hasLotteryRS.getShare2D().compareTo(BigDecimal.ZERO) == 0) {
//            share2DKhr = com2DKhr.subtract(rewardAmount2DKhr);
//            share2DUsd = com2DUsd.subtract(rewardAmount2DUsd);
//        }
//
//        if(hasLotteryRS.getShare3D().compareTo(BigDecimal.ZERO) == 0) {
//            share3DKhr = com3DKhr.subtract(rewardAmount3DKhr);
//            share3DUsd = com3DUsd.subtract(rewardAmount3DUsd);
//        }
//
//        if(hasLotteryRS.getShare4D().compareTo(BigDecimal.ZERO) == 0) {
//            share4DKhr = com4DKhr.subtract(rewardAmount4DKhr);
//            share4DUsd = com4DUsd.subtract(rewardAmount4DUsd);
//        }

        saleItemsRS.setShare1DKhr(saleItemsRS.getShare1DKhr().add(share1DKhr));
        saleItemsRS.setShare2DKhr(saleItemsRS.getShare2DKhr().add(share2DKhr));
        saleItemsRS.setShare3DKhr(saleItemsRS.getShare3DKhr().add(share3DKhr));
        saleItemsRS.setShare4DKhr(saleItemsRS.getShare4DKhr().add(share4DKhr));

        saleItemsRS.setShare1DUsd(saleItemsRS.getShare1DUsd().add(share1DUsd));
        saleItemsRS.setShare2DUsd(saleItemsRS.getShare2DUsd().add(share2DUsd));
        saleItemsRS.setShare3DUsd(saleItemsRS.getShare3DUsd().add(share3DUsd));
        saleItemsRS.setShare4DUsd(saleItemsRS.getShare4DUsd().add(share4DUsd));


        saleItemsRS.setCom1DKhr(saleItemsRS.getCom1DKhr().add(com1DKhr));
        saleItemsRS.setCom2DKhr(saleItemsRS.getCom2DKhr().add(com2DKhr));
        saleItemsRS.setCom3DKhr(saleItemsRS.getCom3DKhr().add(com3DKhr));
        saleItemsRS.setCom4DKhr(saleItemsRS.getCom4DKhr().add(com4DKhr));

        saleItemsRS.setCom1DUsd(saleItemsRS.getCom1DUsd().add(com1DUsd));
        saleItemsRS.setCom2DUsd(saleItemsRS.getCom2DUsd().add(com2DUsd));
        saleItemsRS.setCom3DUsd(saleItemsRS.getCom3DUsd().add(com3DUsd));
        saleItemsRS.setCom4DUsd(saleItemsRS.getCom4DUsd().add(com4DUsd));

        saleItemsRS.setBetAmount1DKhr(saleItemsRS.getBetAmount1DKhr().add(sale.getBetAmount1DKhr()));
        saleItemsRS.setBetAmount2DKhr(saleItemsRS.getBetAmount2DKhr().add(sale.getBetAmount2DKhr()));
        saleItemsRS.setBetAmount3DKhr(saleItemsRS.getBetAmount3DKhr().add(sale.getBetAmount3DKhr()));
        saleItemsRS.setBetAmount4DKhr(saleItemsRS.getBetAmount4DKhr().add(sale.getBetAmount4DKhr()));

        saleItemsRS.setBetAmount1DUsd(saleItemsRS.getBetAmount1DUsd().add(sale.getBetAmount1DUsd()));
        saleItemsRS.setBetAmount2DUsd(saleItemsRS.getBetAmount2DUsd().add(sale.getBetAmount2DUsd()));
        saleItemsRS.setBetAmount3DUsd(saleItemsRS.getBetAmount3DUsd().add(sale.getBetAmount3DUsd()));
        saleItemsRS.setBetAmount4DUsd(saleItemsRS.getBetAmount4DUsd().add(sale.getBetAmount4DUsd()));

        saleItemsRS.setWinAmount1DKhr(saleItemsRS.getWinAmount1DKhr().add(sale.getWinAmount1DKhr()));
        saleItemsRS.setWinAmount2DKhr(saleItemsRS.getWinAmount2DKhr().add(sale.getWinAmount2DKhr()));
        saleItemsRS.setWinAmount3DKhr(saleItemsRS.getWinAmount3DKhr().add(sale.getWinAmount3DKhr()));
        saleItemsRS.setWinAmount4DKhr(saleItemsRS.getWinAmount4DKhr().add(sale.getWinAmount4DKhr()));

        saleItemsRS.setWinAmount1DUsd(saleItemsRS.getWinAmount1DUsd().add(sale.getWinAmount1DUsd()));
        saleItemsRS.setWinAmount2DUsd(saleItemsRS.getWinAmount2DUsd().add(sale.getWinAmount2DUsd()));
        saleItemsRS.setWinAmount3DUsd(saleItemsRS.getWinAmount3DUsd().add(sale.getWinAmount3DUsd()));
        saleItemsRS.setWinAmount4DUsd(saleItemsRS.getWinAmount4DUsd().add(sale.getWinAmount4DUsd()));

        saleItemsRS.setRewardAmount1DKhr(saleItemsRS.getRewardAmount1DKhr().add(rewardAmount1DKhr));
        saleItemsRS.setRewardAmount2DKhr(saleItemsRS.getRewardAmount2DKhr().add(rewardAmount2DKhr));
        saleItemsRS.setRewardAmount3DKhr(saleItemsRS.getRewardAmount3DKhr().add(rewardAmount3DKhr));
        saleItemsRS.setRewardAmount4DKhr(saleItemsRS.getRewardAmount4DKhr().add(rewardAmount4DKhr));

        saleItemsRS.setRewardAmount1DUsd(saleItemsRS.getRewardAmount1DUsd().add(rewardAmount1DUsd));
        saleItemsRS.setRewardAmount2DUsd(saleItemsRS.getRewardAmount2DUsd().add(rewardAmount2DUsd));
        saleItemsRS.setRewardAmount3DUsd(saleItemsRS.getRewardAmount3DUsd().add(rewardAmount3DUsd));
        saleItemsRS.setRewardAmount4DUsd(saleItemsRS.getRewardAmount4DUsd().add(rewardAmount4DUsd));
    }

    private void updateSummaryRSBySaleItemRS(SummaryReportRS summaryReportRS, SaleItemsRS saleItemsRS) {
        summaryReportRS.setCom1DKhr(summaryReportRS.getCom1DKhr().add(saleItemsRS.getCom1DKhr()));
        summaryReportRS.setCom2DKhr(summaryReportRS.getCom2DKhr().add(saleItemsRS.getCom2DKhr()));
        summaryReportRS.setCom3DKhr(summaryReportRS.getCom3DKhr().add(saleItemsRS.getCom3DKhr()));
        summaryReportRS.setCom4DKhr(summaryReportRS.getCom4DKhr().add(saleItemsRS.getCom4DKhr()));

        summaryReportRS.setCom1DUsd(summaryReportRS.getCom1DUsd().add(saleItemsRS.getCom1DUsd()));
        summaryReportRS.setCom2DUsd(summaryReportRS.getCom2DUsd().add(saleItemsRS.getCom2DUsd()));
        summaryReportRS.setCom3DUsd(summaryReportRS.getCom3DUsd().add(saleItemsRS.getCom3DUsd()));
        summaryReportRS.setCom4DUsd(summaryReportRS.getCom4DUsd().add(saleItemsRS.getCom4DUsd()));

        summaryReportRS.setShare1DKhr(summaryReportRS.getShare1DKhr().add(saleItemsRS.getShare1DKhr()));
        summaryReportRS.setShare2DKhr(summaryReportRS.getShare2DKhr().add(saleItemsRS.getShare2DKhr()));
        summaryReportRS.setShare3DKhr(summaryReportRS.getShare3DKhr().add(saleItemsRS.getShare3DKhr()));
        summaryReportRS.setShare4DKhr(summaryReportRS.getShare4DKhr().add(saleItemsRS.getShare4DKhr()));

        summaryReportRS.setShare1DUsd(summaryReportRS.getShare1DUsd().add(saleItemsRS.getShare1DUsd()));
        summaryReportRS.setShare2DUsd(summaryReportRS.getShare2DUsd().add(saleItemsRS.getShare2DUsd()));
        summaryReportRS.setShare3DUsd(summaryReportRS.getShare3DUsd().add(saleItemsRS.getShare3DUsd()));
        summaryReportRS.setShare4DUsd(summaryReportRS.getShare4DUsd().add(saleItemsRS.getShare4DUsd()));

        summaryReportRS.setBetAmount1DKhr(summaryReportRS.getBetAmount1DKhr().add(saleItemsRS.getBetAmount1DKhr()));
        summaryReportRS.setBetAmount2DKhr(summaryReportRS.getBetAmount2DKhr().add(saleItemsRS.getBetAmount2DKhr()));
        summaryReportRS.setBetAmount3DKhr(summaryReportRS.getBetAmount3DKhr().add(saleItemsRS.getBetAmount3DKhr()));
        summaryReportRS.setBetAmount4DKhr(summaryReportRS.getBetAmount4DKhr().add(saleItemsRS.getBetAmount4DKhr()));

        summaryReportRS.setBetAmount1DUsd(summaryReportRS.getBetAmount1DUsd().add(saleItemsRS.getBetAmount1DUsd()));
        summaryReportRS.setBetAmount2DUsd(summaryReportRS.getBetAmount2DUsd().add(saleItemsRS.getBetAmount2DUsd()));
        summaryReportRS.setBetAmount3DUsd(summaryReportRS.getBetAmount3DUsd().add(saleItemsRS.getBetAmount3DUsd()));
        summaryReportRS.setBetAmount4DUsd(summaryReportRS.getBetAmount4DUsd().add(saleItemsRS.getBetAmount4DUsd()));

        summaryReportRS.setWinAmount1DKhr(summaryReportRS.getWinAmount1DKhr().add(saleItemsRS.getWinAmount1DKhr()));
        summaryReportRS.setWinAmount2DKhr(summaryReportRS.getWinAmount2DKhr().add(saleItemsRS.getWinAmount2DKhr()));
        summaryReportRS.setWinAmount3DKhr(summaryReportRS.getWinAmount3DKhr().add(saleItemsRS.getWinAmount3DKhr()));
        summaryReportRS.setWinAmount4DKhr(summaryReportRS.getWinAmount4DKhr().add(saleItemsRS.getWinAmount4DKhr()));

        summaryReportRS.setWinAmount1DUsd(summaryReportRS.getWinAmount1DUsd().add(saleItemsRS.getWinAmount1DUsd()));
        summaryReportRS.setWinAmount2DUsd(summaryReportRS.getWinAmount2DUsd().add(saleItemsRS.getWinAmount2DUsd()));
        summaryReportRS.setWinAmount3DUsd(summaryReportRS.getWinAmount3DUsd().add(saleItemsRS.getWinAmount3DUsd()));
        summaryReportRS.setWinAmount4DUsd(summaryReportRS.getWinAmount4DUsd().add(saleItemsRS.getWinAmount4DUsd()));

        summaryReportRS.setRewardAmount1DKhr(summaryReportRS.getRewardAmount1DKhr().add(saleItemsRS.getRewardAmount1DKhr()));
        summaryReportRS.setRewardAmount2DKhr(summaryReportRS.getRewardAmount2DKhr().add(saleItemsRS.getRewardAmount2DKhr()));
        summaryReportRS.setRewardAmount3DKhr(summaryReportRS.getRewardAmount3DKhr().add(saleItemsRS.getRewardAmount3DKhr()));
        summaryReportRS.setRewardAmount4DKhr(summaryReportRS.getRewardAmount4DKhr().add(saleItemsRS.getRewardAmount4DKhr()));

        summaryReportRS.setRewardAmount1DUsd(summaryReportRS.getRewardAmount1DUsd().add(saleItemsRS.getRewardAmount1DUsd()));
        summaryReportRS.setRewardAmount2DUsd(summaryReportRS.getRewardAmount2DUsd().add(saleItemsRS.getRewardAmount2DUsd()));
        summaryReportRS.setRewardAmount3DUsd(summaryReportRS.getRewardAmount3DUsd().add(saleItemsRS.getRewardAmount3DUsd()));
        summaryReportRS.setRewardAmount4DUsd(summaryReportRS.getRewardAmount4DUsd().add(saleItemsRS.getRewardAmount4DUsd()));

        summaryReportRS.setWinLoseAmountKhr(summaryReportRS.getCommissionKhr().subtract(summaryReportRS.getRewardAmountKhr()));
        summaryReportRS.setWinLoseAmountUsd(summaryReportRS.getCommissionUsd().subtract(summaryReportRS.getRewardAmountUsd()));
    }

    /**
     * add sale item rs from member report to
     *
     * @param memberReportTOS                       List<MemberReportTO>
     * @param userUnder                             UserLevelReportTO
     * @param underLineItemsRS                      List<SaleItemsRS>
     * @param draw                                  DrawingDTO
     * @param summaryUnderLine                      SummaryReportRS
     * @param trackUserHasLotteryEntityMapByOrderId Map<Integer, TrackUserHasLotteryEntity>
     */
    private void addSaleItemRSFromListMemberTO(List<DailyReportTO> memberReportTOS, UserLevelReportTO userUnder, List<SaleItemsRS> underLineItemsRS, List<SaleItemsRS> upperLineItemsRS, DrawingDTO draw, SummaryReportRS summaryUnderLine, SummaryReportRS summaryUpperLine, Map<Long, TrackUserHasLotteryEntity> trackUserHasLotteryEntityMapByOrderId) {
        if (memberReportTOS.size() > 0) {
            setItemAmount(userUnder, memberReportTOS, underLineItemsRS, upperLineItemsRS, draw, summaryUnderLine, summaryUpperLine, trackUserHasLotteryEntityMapByOrderId);
        } else {
            SaleItemsRS underLineItemsRS1 = new SaleItemsRS();
            BeanUtils.copyProperties(userUnder, underLineItemsRS1);
            underLineItemsRS1.setDrawCode(draw.getDrawCode());
            underLineItemsRS1.setDrawAt(draw.getResultedPostAt());
            underLineItemsRS.add(underLineItemsRS1);
            upperLineItemsRS.add(underLineItemsRS1);
        }
    }

}
