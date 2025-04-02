package com.hacknovation.systemservice.v1_0_0.service.loan;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.loan.EditLoanRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

@Service
public interface LoanSV {
    StructureRS getLoan(String lotteryType, String agentCode, String filterDate);
    StructureRS addOrEdit(EditLoanRQ request);
}
