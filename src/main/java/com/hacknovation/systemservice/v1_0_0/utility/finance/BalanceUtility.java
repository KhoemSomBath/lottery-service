package com.hacknovation.systemservice.v1_0_0.utility.finance;

import com.hacknovation.systemservice.constant.LotteryConstant;
import com.hacknovation.systemservice.enums.FinanceTypeEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionBalanceEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.TransactionBalanceRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.finance.FinanceRQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceUtility {

    @Autowired
    private TransactionBalanceRP balanceRP;

    // deposit and withdraw
    public void depositWithdraw(FinanceRQ financeRQ, TransactionEntity transactionEntity) {

        TransactionBalanceEntity balanceEntity = getBalanceEntity(financeRQ.getProceedForUserCode());
        if (financeRQ.getType().equalsIgnoreCase(FinanceTypeEnum.WITHDRAW.toString())) {
            if (LotteryConstant.CURRENCY_KHR.equals(financeRQ.getCurrencyCode())) {
                balanceEntity.setBalanceKhr(balanceEntity.getBalanceKhr().subtract(financeRQ.getAmount()));
            }
            if (LotteryConstant.CURRENCY_USD.equals(financeRQ.getCurrencyCode())) {
                balanceEntity.setBalanceUsd(balanceEntity.getBalanceUsd().subtract(financeRQ.getAmount()));
            }
            balanceRP.save(balanceEntity);
        } else {
            if (balanceEntity == null) {
                TransactionBalanceEntity transactionBalanceEntity = new TransactionBalanceEntity();
                transactionBalanceEntity.setUserCode(transactionEntity.getUserCode());
                if (LotteryConstant.CURRENCY_KHR.equals(transactionEntity.getCurrencyCode())) {
                    transactionBalanceEntity.setBalanceKhr(transactionEntity.getAmount());
                }
                if (LotteryConstant.CURRENCY_USD.equals(transactionEntity.getCurrencyCode())) {
                    transactionBalanceEntity.setBalanceUsd(transactionEntity.getAmount());
                }
                transactionBalanceEntity.setStatus(true);
                balanceRP.save(transactionBalanceEntity);
            } else {
                if (LotteryConstant.CURRENCY_KHR.equals(financeRQ.getCurrencyCode())) {
                    balanceEntity.setBalanceKhr(balanceEntity.getBalanceKhr().add(financeRQ.getAmount()));
                }
                if (LotteryConstant.CURRENCY_USD.equals(financeRQ.getCurrencyCode())) {
                    balanceEntity.setBalanceUsd(balanceEntity.getBalanceUsd().add(financeRQ.getAmount()));
                }
                balanceRP.save(balanceEntity);
            }
        }

    }

    public TransactionBalanceEntity getBalanceEntity(String userCode) {
        return balanceRP.userBalanceByCode(userCode);
    }


    public boolean isNotEnoughBalance(TransactionBalanceEntity balanceEntity, BigDecimal amount, String currencyCode) {
        boolean isNotEnoughBalance = balanceEntity == null;
        if (balanceEntity != null) {
            BigDecimal balance = balanceEntity.getBalanceKhr();
            if (LotteryConstant.CURRENCY_USD.equals(currencyCode))
                balance = balanceEntity.getBalanceUsd();
            isNotEnoughBalance = balance.compareTo(amount) < 0;
        }
        return isNotEnoughBalance;
    }
}
