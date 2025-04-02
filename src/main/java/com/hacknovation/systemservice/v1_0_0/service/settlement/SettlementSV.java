package com.hacknovation.systemservice.v1_0_0.service.settlement;


import com.hacknovation.systemservice.v1_0_0.ui.model.request.settlement.EditSettlementRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;

public interface SettlementSV {
    StructureRS listing();
    StructureRS addOrEdit(EditSettlementRQ editSettlementRQ);
}