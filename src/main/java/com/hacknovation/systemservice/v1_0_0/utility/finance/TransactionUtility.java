package com.hacknovation.systemservice.v1_0_0.utility.finance;

import com.hacknovation.systemservice.constant.FinanceConstant;
import com.hacknovation.systemservice.enums.FinanceStatusEnum;
import com.hacknovation.systemservice.enums.FinanceTypeEnum;
import com.hacknovation.systemservice.v1_0_0.io.entity.TransactionEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.TransactionRP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.finance.FinanceRQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionUtility {

    @Autowired
    private TransactionRP transactionRP;

    public TransactionEntity transaction(FinanceRQ financeRQ)
    {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setUserCode(financeRQ.getProceedForUserCode());
        transactionEntity.setAmount(financeRQ.getAmount());
        transactionEntity.setRemark(financeRQ.getRemark());
        if (financeRQ.getType().equalsIgnoreCase(FinanceConstant.DEPOSIT)) {
            transactionEntity.setType(FinanceTypeEnum.DEPOSIT);
        }
        if (financeRQ.getType().equalsIgnoreCase(FinanceConstant.WITHDRAW)) {
            transactionEntity.setType(FinanceTypeEnum.WITHDRAW);
        }
        if (financeRQ.getType().equalsIgnoreCase(FinanceConstant.RECEIVABLE)) {
            transactionEntity.setType(FinanceTypeEnum.RECEIVABLE);
        }
        if (financeRQ.getType().equalsIgnoreCase(FinanceConstant.PAYABLE)) {
            transactionEntity.setType(FinanceTypeEnum.PAYABLE);
        }
        if (financeRQ.getType().equalsIgnoreCase(FinanceConstant.TRANSFER)) {
            transactionEntity.setType(FinanceTypeEnum.TRANSFER);
        }
        transactionEntity.setProceedBy(financeRQ.getProceederCode());
        transactionEntity.setCurrencyCode(financeRQ.getCurrencyCode());
        transactionEntity.setStatus(FinanceStatusEnum.APPROVED);
        return transactionRP.save(transactionEntity);
    }

}