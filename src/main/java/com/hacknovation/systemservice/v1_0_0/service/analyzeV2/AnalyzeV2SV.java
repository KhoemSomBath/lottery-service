package com.hacknovation.systemservice.v1_0_0.service.analyzeV2;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeV2RQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 06/04/2023
 * time: 15:52
 */
@Service
public interface AnalyzeV2SV {
    StructureRS getFilter(String lotteryType, String drawCode);
    StructureRS postAnalyze(AnalyzeV2RQ analyzeV2RQ);
}
