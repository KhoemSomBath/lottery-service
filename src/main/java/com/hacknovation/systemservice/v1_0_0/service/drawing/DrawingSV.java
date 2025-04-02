package com.hacknovation.systemservice.v1_0_0.service.drawing;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.draw.UpdateDrawingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

@Service
public interface DrawingSV {
    StructureRS drawShifts(String lotteryType, String filterByDate);
    StructureRS updateDraw(UpdateDrawingRQ updateDrawingRQ);
    void removeCache(String lotteryType, String drawAt);
}
