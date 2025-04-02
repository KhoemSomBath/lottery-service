package com.hacknovation.systemservice.v1_0_0.ui.model.response.result.fullinput;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FullResultInputRS {
    private String lotteryType;
    private Date drawAt;
    private Boolean isNight = Boolean.FALSE;
    private List<FullResultInputRowRS> results = new ArrayList<>();
}
