package com.hacknovation.systemservice.v2_0_0.component.mixsettlement;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.SettlementItemsEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.SettlementItemRP;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.report.SettlementRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementTotalSummeryRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementsItemListRS;
import com.hacknovation.systemservice.v1_0_0.utility.GeneralUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.OldBalanceUtility;
import com.hacknovation.systemservice.v1_0_0.utility.lottery.SettlementUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sombath
 * create at 29/4/23 1:07 PM
 */

@Service
@RequiredArgsConstructor
public class MixSettlementService extends BaseServiceIP {

    private final UserRP userRP;
    private final SettlementItemRP settlementItemRP;
    private final SettlementUtility settlementUtility;
    private final OldBalanceUtility oldBalanceUtility;
    private final GeneralUtility generalUtility;
    private final JwtToken jwtToken;
    private final HttpServletRequest request;


    public StructureRS getSettlementList() {

        SettlementRQ reportRQ = new SettlementRQ(request);
        UserToken userToken = jwtToken.getUserToken();
        if (!reportRQ.getFilterByUserCode().equals(LotteryConstant.ALL))
            return this.getSelectedUserSettlementReport(reportRQ, userToken);

        try {
            String userCode = userToken.getUserCode();
            String userType = userToken.getUserType();
            if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole())) {
                userCode = userToken.getParentCode();
            }
            boolean isCanSeeUserOnline = true;
            if (UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType())) {
                isCanSeeUserOnline = userToken.getPermissions().contains(UserConstant.USER_ONLINE_PERMISSION);
            }
            List<UserEntity> userEntities = userRP.getUserByFilter(userType, userCode, reportRQ.getFilterByLevel(), reportRQ.getFilterByUsername(), isCanSeeUserOnline);
            List<String> userCodes = userEntities.stream().map(UserEntity::getCode).collect(Collectors.toList());
            List<SettlementItemsEntity> settlementItemsEntities = settlementItemRP.getAllSettlement(userCodes, reportRQ.getFilterByStartDate(), reportRQ.getFilterByEndDate());

            Date endDate = DateUtils.addDays(generalUtility.parseDate(reportRQ.getFilterByEndDate()), 1);
            Map<String, CurrencyRS> oldBalanceMap = oldBalanceUtility.getUserOldBalanceMapMix(userCodes, generalUtility.formatDateYYYYMMDD(endDate));

            SettlementListRS settlementListRS = new SettlementListRS();
            SettlementTotalSummeryRS summeryRS = new SettlementTotalSummeryRS();

            settlementListRS.setIsEditable(settlementUtility.getIsEditableSettlement(userToken, reportRQ));

            for (UserEntity userEntity : userEntities) {
                SettlementsItemListRS item = new SettlementsItemListRS();
                item.setNickname(userEntity.getNickname());
                item.setUsername(userEntity.getUsername());
                item.setUserCode(userEntity.getCode());
                item.setRoleCode(userEntity.getRoleCode());

                settlementUtility.setSettlementItem(settlementItemsEntities.stream().filter(it->it.getMemberCode().equals(userEntity.getCode())).collect(Collectors.toList()), summeryRS, item);

                this.setOldBalance(oldBalanceMap.get(userEntity.getCode()), item, summeryRS);

                settlementListRS.getSettlements().add(item);
            }

            settlementListRS.setSummery(summeryRS);

            return responseBodyWithSuccessMessage(settlementListRS);
        } catch (Exception exception) {
            exception.printStackTrace();
            return responseBodyWithBadRequest(MessageConstant.BAD_REQUEST, MessageConstant.BAD_REQUEST_KEY);
        }
    }

    private StructureRS getSelectedUserSettlementReport(SettlementRQ reportRQ, UserToken userToken) {
        UserEntity userEntity = userRP.findByCode(reportRQ.getFilterByUserCode());

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
            String currentDate = generalUtility.formatDateYYYYMMDD(date);
            String tomorrow = generalUtility.formatDateYYYYMMDD(DateUtils.addDays(date, 1));

            Map<String, CurrencyRS> oldBalanceMap = oldBalanceUtility.getUserOldBalanceMapMix(List.of(userEntity.getCode()), tomorrow);

            BeanUtils.copyProperties(userEntity, item);
            item.setIssuedAt(currentDate);
            settlementUtility.setSettlementItem(settlementItemsEntities.stream().filter(it -> currentDate.equals(generalUtility.formatDateYYYYMMDD(it.getIssuedAt()))).collect(Collectors.toList()), summery, item);

            this.setOldBalance(oldBalanceMap.get(userEntity.getCode()), item, summery);

            settlementListRS.getSettlements().add(item);

            start.add(Calendar.DATE, 1);
        }
        summery.setTotalKHR(BigDecimal.ZERO);
        summery.setTotalUSD(BigDecimal.ZERO);

        settlementListRS.setSummery(summery);

        return responseBodyWithSuccessMessage(settlementListRS);
    }

    private void setOldBalance(CurrencyRS oldBalance, SettlementsItemListRS item, SettlementTotalSummeryRS summeryRS) {
        if (oldBalance != null) {
            item.setTotalAmountKhr(oldBalance.getAmountKhr());
            item.setTotalAmountUsd(oldBalance.getAmountUsd());
            summeryRS.setTotalKHR(summeryRS.getTotalKHR().add(oldBalance.getAmountKhr()));
            summeryRS.setTotalUSD(summeryRS.getTotalUSD().add(oldBalance.getAmountUsd()));
        }
    }

}
