package com.hacknovation.systemservice.v1_0_0.ui.model.transform;

import lombok.Data;

/**
 * author : phokkinnky
 * date : 8/24/21
 */
@Data
public class DrawItemTF {

    private String postCode;
    private String twoDigits;
    private String threeDigits;
    private String fourDigits;
    private String fiveDigits;
    private String sixDigits;

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

    public String getFiveDigits() {
        if (this.fiveDigits == null)
            return "";
        return fiveDigits;
    }

    public String getSixDigits() {
        if (this.sixDigits == null)
            return "";
        return sixDigits;
    }
}
