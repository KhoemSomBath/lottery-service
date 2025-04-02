package com.hacknovation.systemservice.v1_0_0.service.analyzePercentage;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.PostConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.exception.httpstatus.ForbiddenException;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.service.analyzePercentage.analyzefromdb.AnalyzeFromDB;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.DrawingDTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.RebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseItemsRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.AnalyseRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse.MainAnalyseRS;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.AnalyzeMoneyUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sombath
 * create at 3/3/23 7:47 PM
 */

@Service
@RequiredArgsConstructor
public class AnalyzePercentageFromTemp extends BaseServiceIP {

    private final UserNQ userNQ;
    private final JwtToken jwtToken;

    private final AnalyzeFromDB mcAnalyze;

    private final AnalyzeMoneyUtility analyzeMoneyUtility;

    public StructureRS getPostAnalyze(AnalyzeRQ analyzeRQ) {
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
                case LotteryConstant.SC:
                    if (userToken.getPermissions() == null || !userToken.getPermissions().contains("list-transfer-number-sc"))
                        throw new ForbiddenException("Access denied");
                    break;
                case LotteryConstant.KH:
                    if (userToken.getPermissions() == null || !userToken.getPermissions().contains("list-transfer-number-kh"))
                        throw new ForbiddenException("Access denied");
                    break;
            }

        String userCode = userToken.getUserCode();
        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }

        boolean isCanSeeUserOnline = true;
        if (userToken.getUserType().equalsIgnoreCase(UserConstant.SYSTEM)) {
            isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            if (userToken.getUserRole().equalsIgnoreCase(UserConstant.SUPER_ADMIN))
                isCanSeeUserOnline = true;
        }

        List<UserLevelReportTO> filterMemberTOS = userNQ.userLevelFilter(userCode, userToken.getUserType(), LotteryConstant.ALL, UserConstant.MEMBER, UserConstant.ALL, isCanSeeUserOnline);
        List<String> memberCodes = new ArrayList<>();

        if (!filterMemberTOS.isEmpty()) {
            memberCodes = filterMemberTOS.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());
        }

        AnalyzeRQ backup = new AnalyzeRQ();
        BeanUtils.copyProperties(analyzeRQ, backup);

        if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType())) {
            List<RebateRQ> rebates = new ArrayList<>();
            for (RebateRQ rebate : analyzeRQ.getFilterPosts()) {

                if(!LotteryConstant.LO_GROUP.equals(rebate.getPostCode())){
                    String[] posts = rebate.getPostCode().split("");

                    for (String post: posts){
                        RebateRQ rebateRQ = new RebateRQ();
                        rebateRQ.setPostCode(post);
                        rebates.add(rebateRQ);
                    }

                } else {
                    RebateRQ rebateRQ = new RebateRQ();
                    rebateRQ.setPostCode(PostConstant.LO_GROUP);
                    rebates.add(rebateRQ);
                }

            }

            analyzeRQ.setFilterPosts(rebates);
            mainAnalyseRS.setFilter(analyzeRQ);
        } else {

            // filter percentage

            analyzeRQ.getFilterPosts().removeIf(rebateRQ -> {

                if(LotteryConstant.POST.equals(analyzeRQ.getPostGroup()))
                    return rebateRQ.getPostCode().startsWith("Lo");
                else
                    return !rebateRQ.getPostCode().startsWith("Lo");

            });

        }


        DrawingDTO drawingDTO = new DrawingDTO();

        mcAnalyze.setAnalyze(mainAnalyseRS, analyzeRQ, memberCodes, userToken, drawingDTO);

        if (LotteryConstant.TRANSFER_TYPE_MONEY.equalsIgnoreCase(analyzeRQ.getTransferType())) {
            mainAnalyseRS.setFilter(backup);
            analyzeMoneyUtility.analyzeMoney(mainAnalyseRS);
            this.sortAnalyzePercentageByRebateCodeAndBetAmountDesc(mainAnalyseRS);
        }

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
