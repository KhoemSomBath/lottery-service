package com.hacknovation.systemservice.v1_0_0.service.resultInput;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput.RenderNumberRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput.SetResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 29/10/2022
 * time: 15:22
 */
@Service
public interface ResultInputSV {

    StructureRS listResultItem(String lotteryType);

    StructureRS showForm(String lotteryType);

    StructureRS startRenderNumber(RenderNumberRQ renderNumberRQ);

    StructureRS setResultRender(SetResultRQ setResultRQ);

    StructureRS closeDraw(String lotteryType);
}
