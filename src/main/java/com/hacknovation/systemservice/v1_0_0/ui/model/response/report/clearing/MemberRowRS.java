package com.hacknovation.systemservice.v1_0_0.ui.model.response.report.clearing;

import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.report.clearing.ClearingTO;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.CurrencyRS;
import lombok.Data;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 05/02/2022
 * time: 15:08
 */
@Data
public class MemberRowRS {

    private Date drawAt;
    private Integer pageNumber;

    private CurrencyRS twoDAmount = new CurrencyRS();
    private CurrencyRS threeDAmount = new CurrencyRS();
    private CurrencyRS fourDAmount = new CurrencyRS();

    private CurrencyRS winTwoDAmount = new CurrencyRS();
    private CurrencyRS winThreeDAmount = new CurrencyRS();
    private CurrencyRS winFourDAmount = new CurrencyRS();

}
