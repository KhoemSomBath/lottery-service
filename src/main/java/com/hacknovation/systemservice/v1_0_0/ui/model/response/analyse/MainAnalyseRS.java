package com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 19/02/2022
 * time: 11:38
 */
@Data
public class MainAnalyseRS {
    private AnalyzeRQ filter = new AnalyzeRQ();
    private List<AnalyseRS> items = new ArrayList<>();
    private BigDecimal totalSale = BigDecimal.ZERO;
}
