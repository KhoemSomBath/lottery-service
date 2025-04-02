package com.hacknovation.systemservice.v1_0_0.service.initialbalance;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.InitialBalanceEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserNQ;
import com.hacknovation.systemservice.v1_0_0.io.repo.InitialBalanceRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.initialbalance.EditInitialBalanceRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.initialbalance.InitialBalanceListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.initialbalance.InitialBalanceRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementSummaryRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.InitialBalanceUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialBalanceIP extends BaseServiceIP implements InitialBalanceSV {

    private final InitialBalanceRP initialBalanceRP;
    private final InitialBalanceUtility initialBalanceUtility;
    private final JwtToken jwtToken;
    private final UserRP userRP;
    private final UserNQ userNQ;
    private final UpperUserCodeUtility upperUserCodeUtility;
    private final HttpServletRequest request;
    private final ActivityLogUtility activityLogUtility;

    @Override
    public StructureRS initialBalance(String filterByUserCode, String filterByLevel) {
        UserEntity userEntity;
        String lotteryTypeSuper;
        String filterByUsername = LotteryConstant.ALL;
        if (request.getParameter("filterByUsername") != null) {
            filterByUsername = request.getParameter("filterByUsername");
        }

        UserToken userToken = jwtToken.getUserToken();
        String userCode = userToken.getUserCode();
        String userType = userToken.getUserType().toLowerCase();

        if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
            userCode = userToken.getParentCode();
        }

        Page<UserLevelReportTO> userLevelReportTOS = userNQ.userReferralCodes(userCode, userType, filterByLevel, filterByUserCode, filterByUsername, PageRequest.of(0, 200));

        if (UserConstant.ALL.equalsIgnoreCase(userCode)) {
            userEntity = new UserEntity();
            userEntity.setRoleCode(UserConstant.COMPANY);
            userEntity.setLotteryType(LotteryConstant.VN1);
            lotteryTypeSuper = LotteryConstant.VN1;
        } else {
            userEntity = userRP.findByCode(userCode);
            lotteryTypeSuper = upperUserCodeUtility.getLotteryTypeFromSuperSenior(userEntity);
        }

        List<String> lotteries = List.of(lotteryTypeSuper.split(","));

        String lt = request.getParameter("filterByLotteryType");
        if (lt != null) {
            if (lt.equalsIgnoreCase("MT"))
                lt = "VN2";
            lotteries = List.of(lt);
        }

        List<String> memberCodes = userLevelReportTOS.stream().map(UserLevelReportTO::getUserCode).collect(Collectors.toList());
        List<InitialBalanceEntity> initialBalanceEntities = initialBalanceRP.listing(memberCodes, lotteries);

        List<InitialBalanceListRS> initialBalanceListRSList = new ArrayList<>();
        for (UserLevelReportTO user : userLevelReportTOS) {
            InitialBalanceListRS initialBalanceListRS = new InitialBalanceListRS();
            BeanUtils.copyProperties(user, initialBalanceListRS);
            initialBalanceListRS.setUserCode(user.getUserCode());
            initialBalanceUtility.setBalanceItem(user.getUserCode(), initialBalanceEntities, initialBalanceListRS, lotteries);

            SettlementSummaryRS totalAmount = new SettlementSummaryRS();

            for (String lotteryType : lotteries) {
                switch (lotteryType.toUpperCase()) {
                    case LotteryConstant.LEAP:
                        totalAmount.setAmountKhr(totalAmount.getAmountKhr().add(initialBalanceListRS.getLeap().getAmountKhr()));
                        totalAmount.setAmountUsd(totalAmount.getAmountUsd().add(initialBalanceListRS.getLeap().getAmountUsd()));
                        break;
                    case LotteryConstant.MT:
                    case LotteryConstant.VN2:
                        totalAmount.setAmountKhr(totalAmount.getAmountKhr().add(initialBalanceListRS.getMt().getAmountKhr()));
                        totalAmount.setAmountUsd(totalAmount.getAmountUsd().add(initialBalanceListRS.getMt().getAmountUsd()));
                        break;
                    case LotteryConstant.VN1:
                        totalAmount.setAmountKhr(totalAmount.getAmountKhr().add(initialBalanceListRS.getVn1().getAmountKhr()));
                        totalAmount.setAmountUsd(totalAmount.getAmountUsd().add(initialBalanceListRS.getVn1().getAmountUsd()));
                        break;
                    case LotteryConstant.KH:
                        totalAmount.setAmountKhr(totalAmount.getAmountKhr().add(initialBalanceListRS.getKh().getAmountKhr()));
                        totalAmount.setAmountUsd(totalAmount.getAmountUsd().add(initialBalanceListRS.getKh().getAmountUsd()));
                        break;
                    case LotteryConstant.TN:
                        totalAmount.setAmountKhr(totalAmount.getAmountKhr().add(initialBalanceListRS.getTn().getAmountKhr()));
                        totalAmount.setAmountUsd(totalAmount.getAmountUsd().add(initialBalanceListRS.getTn().getAmountUsd()));
                        break;
                    case LotteryConstant.SC:
                        totalAmount.setAmountKhr(totalAmount.getAmountKhr().add(initialBalanceListRS.getSc().getAmountKhr()));
                        totalAmount.setAmountUsd(totalAmount.getAmountUsd().add(initialBalanceListRS.getSc().getAmountUsd()));
                        break;
                }
            }
            initialBalanceListRS.setTotal(totalAmount);
            initialBalanceListRSList.add(initialBalanceListRS);
        }

        InitialBalanceRS initialBalanceRS = new InitialBalanceRS();
        initialBalanceRS.setItems(initialBalanceListRSList);
        initialBalanceRS.setIsEditable(initialBalanceUtility.isEditableInitialBalance(userToken, filterByLevel));

        return responseBodyWithSuccessMessage(initialBalanceRS);
    }

    @Override
    public StructureRS addOrEditInitialBalance(EditInitialBalanceRQ editInitialBalanceRQ) {
        InitialBalanceEntity initialBalanceEntity;
        var token = jwtToken.getUserToken();
        String action = "ADD";

        if (editInitialBalanceRQ.getItemId() != null) {
            initialBalanceEntity = initialBalanceRP.getOne(editInitialBalanceRQ.getItemId());
            initialBalanceEntity.setUpdatedBy(token.getUserCode());
            action = "EDIT";
        } else {
            initialBalanceEntity = new InitialBalanceEntity();
            initialBalanceEntity.setLotteryType(editInitialBalanceRQ.getType().toUpperCase());
            initialBalanceEntity.setUserCode(editInitialBalanceRQ.getUserCode());
            initialBalanceEntity.setCreatedBy(token.getUserCode());
            initialBalanceEntity.setIssuedAt(new Date());
        }
        initialBalanceEntity.setBalanceUsd(editInitialBalanceRQ.getAmountUsd());
        initialBalanceEntity.setBalanceKhr(editInitialBalanceRQ.getAmountKhr());
        activityLogUtility.addActivityLog(initialBalanceEntity.getLotteryType(), ActivityLogConstant.MODULE_INITIAL_BALANCE, token.getUserType(), action, token.getUserCode(), initialBalanceRP.save(initialBalanceEntity));
        return responseBodyWithSuccessMessage();
    }
}
