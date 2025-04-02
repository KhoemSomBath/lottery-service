package com.hacknovation.systemservice.v1_0_0.ui.model.response.result;

import lombok.Data;

/**
 * author : phokkinnky
 * date : 8/6/21
 */
@Data
public class DrawItemRS {
    private Integer id;
    private String postCode;
    private String postGroup;
    private String twoDigits;
    private String threeDigits;
    private String fourDigits;
    private String number;
    private String thNumber;

}
