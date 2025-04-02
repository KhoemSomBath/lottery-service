package com.hacknovation.systemservice.v1_0_0.service.analyzePercentage;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.exception.httpstatus.ForbiddenException;
import com.hacknovation.systemservice.v1_0_0.io.entity.kh.KHTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.leap.LeapTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.sc.SCTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.tn.TNTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vnone.VNOneTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.vntwo.VNTwoTempDrawingEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.kh.KHTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.leap.LeapTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.sc.SCTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.tn.TNTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vnone.VNOneTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.vntwo.VNTwoTempDrawingRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.RebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.MainAnalyseRS;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.AnalyzeMoneyUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.AnalyzePercentageUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:41
 */
@Service
@RequiredArgsConstructor
public class AnalyzePercentageIP extends BaseServiceIP implements AnalyzePercentageSV {

    private final TNTempDrawingRP tnTempDrawingRP;
    private final LeapTempDrawingRP leapTempDrawingRP;

    private final SCTempDrawingRP scTempDrawingRP;
    private final KHTempDrawingRP khTempDrawingRP;
    private final VNOneTempDrawingRP vnOneTempDrawingRP;
    private final VNTwoTempDrawingRP vnTwoTempDrawingRP;
    private final UserNQ userNQ;
    private final JwtToken jwtToken;
    private final AnalyzePercentageUtility analyzePercentageUtility;
    private final AnalyzeMoneyUtility analyzeMoneyUtility;
    private final UpperUserCodeUtility upperUserCodeUtility;

    @Override
    public StructureRS getFilter(String lotteryType, String filterByTransferType, String filterByDrawCode) {
        MainAnalyseRS mainAnalyseRS = new MainAnalyseRS();
        RebateRQ rebateTransfer = new RebateRQ();
        rebateTransfer.setThreeD(BigDecimal.valueOf(800));
        rebateTransfer.setTwoD(BigDecimal.valueOf(90));
        rebateTransfer.setFourD(BigDecimal.valueOf(5000));
        AnalyzeRQ filter = mainAnalyseRS.getFilter();
        filter.setRebateTransfer(rebateTransfer);
        filter.setTransferType(filterByTransferType);
        boolean isNight = false;
        switch (lotteryType) {
            case LotteryConstant.VN1:
                VNOneTempDrawingEntity drawingEntity = LotteryConstant.ALL.equals(filterByDrawCode) ? vnOneTempDrawingRP.recentDrawing() : vnOneTempDrawingRP.findByCode(filterByDrawCode);
                if (drawingEntity == null)
                    drawingEntity = vnOneTempDrawingRP.lastDraw();

                isNight = drawingEntity.getIsNight();
                filter.setDrawCode(drawingEntity.getCode());
                filter.setDrawAt(drawingEntity.getResultedPostAt());
                analyzePercentageUtility.updateDefaultVNFilter(mainAnalyseRS, lotteryType, drawingEntity.getIsNight());

                break;
            case LotteryConstant.VN2:
                VNTwoTempDrawingEntity vnTwoTempDrawingEntity = LotteryConstant.ALL.equals(filterByDrawCode) ? vnTwoTempDrawingRP.recentDrawing() : vnTwoTempDrawingRP.findByCode(filterByDrawCode);
                if (vnTwoTempDrawingEntity == null)
                    vnTwoTempDrawingEntity = vnTwoTempDrawingRP.lastDraw();

                isNight = vnTwoTempDrawingEntity.getIsNight();
                filter.setDrawCode(vnTwoTempDrawingEntity.getCode());
                filter.setDrawAt(vnTwoTempDrawingEntity.getResultedPostAt());
                analyzePercentageUtility.updateDefaultVNFilter(mainAnalyseRS, lotteryType, vnTwoTempDrawingEntity.getIsNight());

                break;
            case LotteryConstant.TN:
                TNTempDrawingEntity tnTempDrawingEntity = LotteryConstant.ALL.equals(filterByDrawCode) ? tnTempDrawingRP.recentDrawing() : tnTempDrawingRP.findByCode(filterByDrawCode);
                if (tnTempDrawingEntity == null)
                    tnTempDrawingEntity = tnTempDrawingRP.lastDraw();

                isNight = tnTempDrawingEntity.getIsNight();
                filter.setDrawCode(tnTempDrawingEntity.getCode());
                filter.setDrawAt(tnTempDrawingEntity.getResultedPostAt());
                analyzePercentageUtility.updateDefaultTNFilter(mainAnalyseRS, tnTempDrawingEntity.getShiftCode());

                break;
            case LotteryConstant.LEAP:
                LeapTempDrawingEntity leapTempDrawingEntity = LotteryConstant.ALL.equals(filterByDrawCode) ? leapTempDrawingRP.recentDrawing() : leapTempDrawingRP.findByCode(filterByDrawCode);
                if (leapTempDrawingEntity == null)
                    leapTempDrawingEntity = leapTempDrawingRP.lastDraw();

                filter.setDrawCode(leapTempDrawingEntity.getCode());
                filter.setDrawAt(leapTempDrawingEntity.getResultedPostAt());
                analyzePercentageUtility.updateDefaultLeapFilter(mainAnalyseRS);
                break;

            case LotteryConstant.SC:
                SCTempDrawingEntity scTempDrawingEntity = LotteryConstant.ALL.equals(filterByDrawCode) ? scTempDrawingRP.recentDrawing() : scTempDrawingRP.findByCode(filterByDrawCode);
                if (scTempDrawingEntity == null)
                    scTempDrawingEntity = scTempDrawingRP.lastDraw();

                filter.setDrawCode(scTempDrawingEntity.getCode());
                filter.setDrawAt(scTempDrawingEntity.getResultedPostAt());
                analyzePercentageUtility.updateDefaultSCFilter(mainAnalyseRS);
                break;

            case LotteryConstant.KH:
                KHTempDrawingEntity khTempDrawingEntity = LotteryConstant.ALL.equals(filterByDrawCode) ? khTempDrawingRP.recentDrawing() : khTempDrawingRP.findByCode(filterByDrawCode);
                if (khTempDrawingEntity == null)
                    khTempDrawingEntity = khTempDrawingRP.lastDraw();

                filter.setDrawCode(khTempDrawingEntity.getCode());
                filter.setDrawAt(khTempDrawingEntity.getResultedPostAt());
                analyzePercentageUtility.updateDefaultKHFilter(mainAnalyseRS);
                break;
        }

        filter.setLotteryType(lotteryType);

        if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(filterByTransferType)) {
            analyzePercentageUtility.updateDefaultTransferMoney(mainAnalyseRS);
            if (isNight) {
                mainAnalyseRS
                        .getFilter()
                        .getFilterPosts()
                        .removeIf(it-> !List.of("ABCD", "LO").contains(it.getPostCode()));
            }
        }

        return responseBodyWithSuccessMessage(mainAnalyseRS);
    }

    @Override
    public StructureRS postAnalyzeFilter(AnalyzeRQ analyzeRQ) {
        MainAnalyseRS mainAnalyseRS = new MainAnalyseRS();

        updateMainAnalyze(mainAnalyseRS, analyzeRQ);

        return responseBodyWithSuccessMessage(mainAnalyseRS);
    }


    private void updateMainAnalyze(MainAnalyseRS mainAnalyseRS, AnalyzeRQ analyzeRQ) {
        BeanUtils.copyProperties(analyzeRQ, mainAnalyseRS.getFilter());

        UserToken userToken = jwtToken.getUserToken();

        if (!userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
            switch (analyzeRQ.getLotteryType().toUpperCase()) {
                case LotteryConstant.VN1:
                    if (userToken.getPermissions() == null || !userToken.getPermissions().contains("list-transfer-number-vnone"))
                        throw new ForbiddenException("Access denied");
                    break;
                case LotteryConstant.VN2:
                    if (userToken.getPermissions() == null || !userToken.getPermissions().contains("list-transfer-number-mt"))
                        throw new ForbiddenException("Access denied");
                    break;
                case LotteryConstant.TN:
                    if (userToken.getPermissions() == null || !userToken.getPermissions().contains("list-transfer-number-tn"))
                        throw new ForbiddenException("Access denied");
                    break;
                case LotteryConstant.LEAP:
                    if (userToken.getPermissions() == null || !userToken.getPermissions().contains("list-transfer-number-leap"))
                        throw new ForbiddenException("Access denied");
                    break;
                case LotteryConstant.KH:
                    if (userToken.getPermissions() == null || !userToken.getPermissions().contains("list-transfer-number-kh"))
                        throw new ForbiddenException("Access denied");
                    break;
            }

        String userCode = userToken.getUserCode();
        String filterUserLevel = userToken.getUserRole();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
            filterUserLevel = userToken.getParentRole();
        }

        if (analyzeRQ.getPostGroup().equalsIgnoreCase("LO"))
            filterUserLevel = UserConstant.MEMBER;

        if (UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType()))
            filterUserLevel = UserConstant.SUPER_SENIOR;

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        List<UserLevelReportTO> filterMemberTOS = userNQ.userLevelFilter(userCode, userToken.getUserType(), LotteryConstant.ALL, filterUserLevel, UserConstant.ALL, isCanSeeUserOnline);
        List<String> usernameList = new ArrayList<>();
        List<String> memberCodes = new ArrayList<>();

        if (!filterMemberTOS.isEmpty()) {
            memberCodes = filterMemberTOS.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());
            usernameList = filterMemberTOS.stream().map(UserLevelReportTO::getUsername).collect(Collectors.toList());
        }

        List<RebateRQ> backupFilterPost = new ArrayList<>();
        if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType())) {
            List<RebateRQ> newFilterPost = new ArrayList<>();
            for (RebateRQ filterPost : mainAnalyseRS.getFilter().getFilterPosts()) {
                backupFilterPost.add(filterPost);
                RebateRQ newRebateRQ = new RebateRQ();
                BeanUtils.copyProperties(filterPost, newRebateRQ);
                if (PostConstant.LO_GROUP.equals(filterPost.getPostCode())) {
                    newRebateRQ.setPostCode(PostConstant.LO1);
                    newFilterPost.add(newRebateRQ);
                } else {
                    for (String post : filterPost.getPostCode().split("")) {
                        newRebateRQ = new RebateRQ();
                        BeanUtils.copyProperties(filterPost, newRebateRQ);
                        newRebateRQ.setPostCode(post);
                        newFilterPost.add(newRebateRQ);
                    }
                }
            }
            mainAnalyseRS.getFilter().setFilterPosts(newFilterPost);
        }

        DrawingDTO drawingDTO = new DrawingDTO();

        analyzePercentageUtility.analyzePercentage(mainAnalyseRS, memberCodes, usernameList, analyzeRQ.getAnalyzeType(), drawingDTO);
        if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType())) {
            if (drawingDTO.getIsNight()) {
                backupFilterPost.removeIf(it-> !List.of("ABCD", "LO").contains(it.getPostCode()));
            }
            mainAnalyseRS.getFilter().setFilterPosts(backupFilterPost);
            analyzeMoneyUtility.analyzeMoney(mainAnalyseRS);
        } else {
            if (mainAnalyseRS.getFilter().getPostGroup().equals(LotteryConstant.POST)) {
                mainAnalyseRS.getItems().removeIf(it-> it.getPostCode().equals(PostConstant.LO1));
            }
        }

        sortAnalyzePercentageByRebateCodeAndBetAmountDesc(mainAnalyseRS);
    }

    private void sortAnalyzePercentageByRebateCodeAndBetAmountDesc(MainAnalyseRS mainAnalyseRS) {
        AnalyzeRQ filter = mainAnalyseRS.getFilter();
        for (AnalyseRS item : mainAnalyseRS.getItems()) {
            List<AnalyseItemsRS> newList = new ArrayList<>();
            Map<String, List<AnalyseItemsRS>> groupByRebateCode = item.getItems().stream().collect(Collectors.groupingBy(AnalyseItemsRS::getRebateCode));
            List<String> rebateCodes = new ArrayList<>(groupByRebateCode.keySet());
            Collections.sort(rebateCodes);
            for (String rebateCode : rebateCodes) {
                List<AnalyseItemsRS> listRebateCode = new ArrayList<>(groupByRebateCode.get(rebateCode));
                if (LotteryConstant.TRANSFER_TYPE_MONEY.equals(filter.getTransferType())) {
                    listRebateCode.sort(Comparator.comparing(AnalyseItemsRS::getTotalSale).reversed());
                } else {
                    listRebateCode.sort(Comparator.comparing(AnalyseItemsRS::getBetAmount).reversed());
                }
                newList.addAll(listRebateCode);
            }
            item.setItems(newList);
        }
    }
}
