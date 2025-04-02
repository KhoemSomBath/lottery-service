package com.hacknovation.systemservice.v1_0_0.service.analyzePercentage;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:40
 */
@Service
public interface AnalyzePercentageSV {
    StructureRS getFilter(String lotteryType, String filterByTransferType, String filterByDrawCode);
    StructureRS postAnalyzeFilter(AnalyzeRQ analyzeRQ);
}
