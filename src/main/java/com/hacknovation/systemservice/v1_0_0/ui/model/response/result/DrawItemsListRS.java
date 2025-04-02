package com.hacknovation.systemservice.v1_0_0.ui.model.response.result;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * author : phokkinnky
 * date : 8/6/21
 */
@Data
public class DrawItemsListRS {
    private String lotteryType;
    private Date drawAt;
    private String hourKey;
    private Boolean isNight = Boolean.FALSE;
    private List<DrawItemRS> results;
}
