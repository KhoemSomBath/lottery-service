package com.hacknovation.systemservice.v1_0_0.service.initialbalance;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.initialbalance.EditInitialBalanceRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

@Service
public interface InitialBalanceSV {
    StructureRS initialBalance(String agentCode, String userRole);
    StructureRS addOrEditInitialBalance(EditInitialBalanceRQ request);
}
