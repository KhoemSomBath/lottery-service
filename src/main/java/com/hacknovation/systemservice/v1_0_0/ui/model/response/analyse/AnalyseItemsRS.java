package com.hacknovation.systemservice.v1_0_0.ui.model.response.analyse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hacknovation.systemservice.exception.serilize.RemoveFloatingPointSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class AnalyseItemsRS {
    private List<PostSelectRS> highlightPosts = new ArrayList<>();
    private String rebateCode;
    private String number;
    private BigDecimal totalSale = BigDecimal.ZERO;
    private BigDecimal totalReward = BigDecimal.ZERO;
    private BigDecimal betAmount = BigDecimal.ZERO;
    private Boolean isHighlightSale = false;

    @JsonSerialize(using = RemoveFloatingPointSerializer.class)
    private BigDecimal percentage = BigDecimal.ZERO;
    @JsonIgnore
    private BigDecimal filterPercentage = BigDecimal.ZERO;
}
