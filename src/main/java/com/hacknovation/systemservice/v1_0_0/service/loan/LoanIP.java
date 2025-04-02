package com.hacknovation.systemservice.v1_0_0.service.loan;

import com.hacknovation.systemservice.v1_0_0.io.entity.LoanEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.LoanRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.loan.LoanTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.loan.EditLoanRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.loan.LoanRS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanIP extends BaseServiceIP implements LoanSV {

    private final LoanRP loanRP;

    @Override
    public StructureRS getLoan(String lotteryType, String agentCode, String filterDate) {
        List<LoanTO> loan = loanRP.getLoan(lotteryType, agentCode, filterDate);
        List<LoanRS> respond = new ArrayList<>();
        for (LoanTO item: loan){
            respond.add(new LoanRS(item));
        }
        return responseBodyWithSuccessMessage(respond);
    }

    @Override
    public StructureRS addOrEdit(EditLoanRQ request) {
        LoanEntity loanEntity;
        if (request.getId() == null)
            loanEntity = new LoanEntity();
        else
            loanEntity = loanRP.getOne(request.getId());

        loanEntity.setUserCode(request.getUserCode());
        loanEntity.setLotteryType(request.getLotteryType());
        loanEntity.setIssuedAt(request.getDate());

        switch (request.getType()) {
            case "borrow":
                loanEntity.setBorrowKhr(request.getAmountKhr());
                loanEntity.setBorrowUsd(request.getAmountUsd());
                break;
            case "payback":
                loanEntity.setPaybackKhr(request.getAmountKhr());
                loanEntity.setPaybackUsd(request.getAmountUsd());
                break;
            default:
                loanEntity.setNextPayment(request.getNextPayment());
                break;
        }

        LoanEntity yesterdayLoan = loanRP.getYesterdayLoan(request.getUserCode(), request.getLotteryType(), request.getDate());
        if(yesterdayLoan != null) {
            loanEntity.setInstallmentPaymentKhr(loanEntity.getBorrowKhr().add(yesterdayLoan.getBorrowKhr().subtract(loanEntity.getPaybackKhr())));
            loanEntity.setInstallmentPaymentUsd(loanEntity.getBorrowUsd().add(yesterdayLoan.getBorrowUsd().subtract(loanEntity.getPaybackUsd())));
        }else{
            loanEntity.setInstallmentPaymentUsd(loanEntity.getBorrowUsd());
            loanEntity.setInstallmentPaymentKhr(loanEntity.getBorrowKhr());
        }

        return responseBodyWithSuccessMessage(loanRP.save(loanEntity));
    }
}
