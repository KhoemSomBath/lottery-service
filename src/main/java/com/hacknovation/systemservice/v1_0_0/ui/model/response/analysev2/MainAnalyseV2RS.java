package com.hacknovation.systemservice.v1_0_0.ui.model.response.analysev2;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeV2RQ;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 06/04/2023
 * time: 16:06
 */
@Data
public class MainAnalyseV2RS {
    private AnalyzeV2RQ filter = new AnalyzeV2RQ();
    private List<AnalyseV2RS> items = new ArrayList<>();
    private BigDecimal betAmount = BigDecimal.ZERO;
    private BigDecimal commissionAmount = BigDecimal.ZERO;
}
