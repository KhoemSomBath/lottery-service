package com.hacknovation.systemservice.v1_0_0.service.finance;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.finance.FinanceRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

@Service
public interface FinanceSV {
    StructureRS balance();
    StructureRS transaction(FinanceRQ financeRQ);
    StructureRS listing(String userCode);
    StructureRS listMobile();
}