package com.hacknovation.systemservice.v1_0_0.service.result;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.PublishResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.UpdateDrawItemsRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;

public interface ResultSV {
    StructureRS getAllResult(String lotteryType);
    StructureRS getResult(String lotteryType, String drawCode, String filterByDate);
    StructureRS setResult(UpdateDrawItemsRQ updateDrawItemsRQ);
    StructureRS publishResult(PublishResultRQ publishResultRQ);
}