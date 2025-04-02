package com.hacknovation.systemservice.v1_0_0.utility.number;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/*
 * author: kangto
 * createdAt: 19/10/2022
 * time: 14:03
 */
@Data
public class NumberB {

    private String fiveDigit;

    public NumberB(String fiveDigit) {
        this.fiveDigit = fiveDigit;
    }

    public String getNumberB2D() {
        return StringUtils.left(fiveDigit, 2);
    }

    public String getNumberB3D() {
        return StringUtils.right(fiveDigit, 3);
    }

    public String getNumberC2D() {
        return StringUtils.right(fiveDigit, 2);
    }

    public String getNumberC3D() {
        return StringUtils.left(fiveDigit, 3);
    }

    public String getNumberD2D() {
        return StringUtils.left(fiveDigit, 1)
                .concat(StringUtils.right(fiveDigit, 1));
    }

    public String getNumberD3D() {
        String fourDigit = StringUtils.right(fiveDigit, 4);
        return StringUtils.left(fourDigit, 3);
    }

}
