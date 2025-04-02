package com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class AnalyseRS {
    private String postCode;
    private List<AnalyseItemsRS> items = new ArrayList<>();
    private BigDecimal totalSale = BigDecimal.ZERO;
}
