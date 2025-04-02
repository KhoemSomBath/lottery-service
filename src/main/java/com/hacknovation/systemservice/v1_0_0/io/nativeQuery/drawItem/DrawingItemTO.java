package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.drawItem;

import lombok.Data;

/**
 * author : phokkinnky
 * date : 8/24/21
 */
@Data
public class DrawingItemTO {

    private String drawCode;
    private String postCode;
    private String twoDigits;
    private String threeDigits;
    private String fourDigits;
    private Boolean isNight = Boolean.FALSE;

    public String getTwoDigits() {
        if (this.twoDigits == null)
            return "";
        return twoDigits;
    }

    public String getThreeDigits() {
        if (this.threeDigits == null)
            return "";
        return threeDigits;
    }

    public String getFourDigits() {
        if (this.fourDigits == null)
            return "";
        return fourDigits;
    }
}
