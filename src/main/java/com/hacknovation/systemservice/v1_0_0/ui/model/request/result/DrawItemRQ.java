package com.hacknovation.systemservice.v1_0_0.ui.model.request.result;

import lombok.Data;

/**
 * author : phokkinnky
 * date : 8/6/21
 */
@Data
public class DrawItemRQ {
    private Integer id;
    private String postCode;
    private String postGroup;
    private String twoDigits;
    private String threeDigits;
    private String fourDigits;
    private String number;

    public String getThreeDigits() {
        if (this.threeDigits == null)
            return "***";
        if (this.threeDigits.isEmpty())
            return "***";
        return threeDigits;
    }

    public String getTwoDigits() {
        if (this.twoDigits == null)
            return "**";
        if (this.twoDigits.isEmpty())
            return "**";
        return twoDigits;
    }
}
