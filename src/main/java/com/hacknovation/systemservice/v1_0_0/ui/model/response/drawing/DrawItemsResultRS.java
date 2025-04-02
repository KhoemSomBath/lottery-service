package com.hacknovation.systemservice.v1_0_0.ui.model.response.drawing;

import lombok.Data;

@Data
public class DrawItemsResultRS {
    private String rewardGroup;
    private String postCode;
    private String postGroup;
    private String twoDigits;
    private String threeDigits;
    private String number;
}
