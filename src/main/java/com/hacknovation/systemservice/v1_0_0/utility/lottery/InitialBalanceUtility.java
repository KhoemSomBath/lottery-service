package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.constant.UserConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.InitialBalanceEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserReferralTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.InitialBalanceRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.initialbalance.InitialBalanceListRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.settlement.SettlementSummaryRS;
import com.hacknovation.systemservice.v1_0_0.utility.UpperUserCodeUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitialBalanceUtility {

    private final InitialBalanceRP initialBalanceRP;
    private final UpperUserCodeUtility upperUserCodeUtility;

    public void setBalanceItem(String userCode, List<InitialBalanceEntity> initialBalanceEntities, InitialBalanceListRS itemListRS1, List<String> lotteries) {

        List<InitialBalanceEntity> itemsByUserCode = initialBalanceEntities.stream().filter(item -> item.getUserCode().equalsIgnoreCase(userCode)).collect(Collectors.toList());
        if (itemsByUserCode.size() > 0) {

            Optional<InitialBalanceEntity> leap = itemsByUserCode.stream().filter(item -> item.getLotteryType().equals(LotteryConstant.LEAP)).findFirst();
            if (leap.isPresent()) {
                itemListRS1.setLeap(setAmount(leap.get()));
            } else if(lotteries.contains(LotteryConstant.LEAP)){
                itemListRS1.setLeap(setZero());
            }

            Optional<InitialBalanceEntity> mt = itemsByUserCode.stream().filter(item -> item.getLotteryType().equals(LotteryConstant.MT) || item.getLotteryType().equals(LotteryConstant.VN2)).findFirst();
            if (mt.isPresent()) {
                itemListRS1.setMt(setAmount(mt.get()));
            } else if(lotteries.contains(LotteryConstant.MT) || lotteries.contains(LotteryConstant.VN2)){
                itemListRS1.setMt(setZero());
            }

            Optional<InitialBalanceEntity> vn1 = itemsByUserCode.stream().filter(item -> item.getLotteryType().equals(LotteryConstant.VN1)).findFirst();
            if (vn1.isPresent()) {
                itemListRS1.setVn1(setAmount(vn1.get()));
            } else if(lotteries.contains(LotteryConstant.VN1)){
                itemListRS1.setVn1(setZero());
            }

            Optional<InitialBalanceEntity> tn = itemsByUserCode.stream().filter(item -> item.getLotteryType().equals(LotteryConstant.TN)).findFirst();
            if (tn.isPresent()) {
                itemListRS1.setTn(setAmount(tn.get()));
            } else if(lotteries.contains(LotteryConstant.TN)){
                itemListRS1.setTn(setZero());
            }

            Optional<InitialBalanceEntity> kh = itemsByUserCode.stream().filter(item -> item.getLotteryType().equals(LotteryConstant.KH)).findFirst();
            if (kh.isPresent()) {
                itemListRS1.setKh(setAmount(kh.get()));
            } else if(lotteries.contains(LotteryConstant.KH)) {
                itemListRS1.setKh(setZero());
            }

            Optional<InitialBalanceEntity> sc = itemsByUserCode.stream().filter(item -> item.getLotteryType().equals(LotteryConstant.SC)).findFirst();
            if (sc.isPresent()) {
                itemListRS1.setSc(setAmount(sc.get()));
            } else if(lotteries.contains(LotteryConstant.SC)) {
                itemListRS1.setSc(setZero());
            }

        } else {
            for (String lottery : lotteries) {
                if (LotteryConstant.VN1.equals(lottery)) {
                    itemListRS1.setVn1(setZero());
                }

                if (LotteryConstant.VN2.equals(lottery) || LotteryConstant.MT.equals(lottery)) {
                    itemListRS1.setMt(setZero());
                }

                if (LotteryConstant.LEAP.equals(lottery)) {
                    itemListRS1.setLeap(setZero());
                }

                if (LotteryConstant.TN.equals(lottery)) {
                    itemListRS1.setTn(setZero());
                }

                if (LotteryConstant.KH.equals(lottery)) {
                    itemListRS1.setKh(setZero());
                }
                if (LotteryConstant.SC.equals(lottery)) {
                    itemListRS1.setSc(setZero());
                }
            }
        }
    }


    private SettlementSummaryRS setAmount(InitialBalanceEntity item) {
        SettlementSummaryRS amount = new SettlementSummaryRS();
        amount.setItemId(item.getId());
        amount.setAmountKhr(item.getBalanceKhr());
        amount.setAmountUsd(item.getBalanceUsd());
        return amount;
    }

    private SettlementSummaryRS setZero() {
        SettlementSummaryRS amount = new SettlementSummaryRS();
        amount.setAmountKhr(BigDecimal.ZERO);
        amount.setAmountUsd(BigDecimal.ZERO);
        return amount;
    }


    /***
     * get map initial balance by lottery
     * @param lotteryType String
     * @param userCodes List<String>
     * @return Map<String, CurrencyRS>
     */
    public Map<String, CurrencyRS> getInitialBalanceMap(String lotteryType, List<String> userCodes) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();
        List<InitialBalanceEntity> initialBalanceEntities = initialBalanceRP.listingByLotteryType(userCodes, lotteryType);
        initialBalanceEntities.forEach(initial -> {
            CurrencyRS currencyRS = new CurrencyRS();
            currencyRS.setAmountKhr(initial.getBalanceKhr());
            currencyRS.setAmountUsd(initial.getBalanceUsd());
            currencyRSMap.put(initial.getUserCode(), currencyRS);
        });

        return currencyRSMap;
    }

    public Map<String, CurrencyRS> getInitialBalanceMapMix(List<String> userCodes) {
        Map<String, CurrencyRS> currencyRSMap = new HashMap<>();
        List<InitialBalanceEntity> initialBalanceEntities = initialBalanceRP.listingByUserCode(userCodes);
        initialBalanceEntities.forEach(initial -> {
            CurrencyRS currencyRS = new CurrencyRS();

            if(currencyRSMap.containsKey(initial.getUserCode()))
                currencyRS = currencyRSMap.get(initial.getUserCode());

            currencyRS.setAmountKhr(currencyRS.getAmountKhr().add(initial.getBalanceKhr()));
            currencyRS.setAmountUsd(currencyRS.getAmountUsd().add(initial.getBalanceUsd()));
            currencyRSMap.put(initial.getUserCode(), currencyRS);
        });

        return currencyRSMap;
    }

    public boolean isEditableInitialBalance(UserToken userToken, String filterRole) {
        boolean isEdit = true;
        if (!UserConstant.SYSTEM.equalsIgnoreCase(userToken.getUserType())) {
            String currentRole = userToken.getUserRole();
            if (UserConstant.SUB_ACCOUNT.equalsIgnoreCase(userToken.getUserRole()))
                currentRole = userToken.getParentRole();
            if (!upperUserCodeUtility.underLevelRole(currentRole).equalsIgnoreCase(filterRole))
                isEdit = false;
        }

        return isEdit;
    }


}
