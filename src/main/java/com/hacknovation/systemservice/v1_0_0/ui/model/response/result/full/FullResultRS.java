package com.hacknovation.systemservice.v1_0_0.ui.model.response.result.full;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FullResultRS {
    private String lotteryType;
    private Date drawAt;
    private Boolean isNight = Boolean.FALSE;
    private List<FullResultRowRS> results = new ArrayList<>();
}
