package com.hacknovation.systemservice.v1_0_0.ui.model.response.user;

import com.hacknovation.systemservice.constant.LotteryConstant;
import lombok.Data;

/**
 * @author KHOEM Sombath
 * Date: 6/14/2021
 * Time: 5:39 PM
 */
@Data
public class LotteryTyRS {
    private String code;
    private String value;

    public LotteryTyRS(String code) {
        this.code = code.equalsIgnoreCase(LotteryConstant.SIX6D) ? LotteryConstant.SIXD : code;
        this.value = LotteryConstant.VN2.equalsIgnoreCase(code) ? LotteryConstant.MT : code;
    }
}
