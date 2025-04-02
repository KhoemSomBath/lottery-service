package com.hacknovation.systemservice.v1_0_0.ui.model.request.manageresult;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @author Sombath
 * create at 6/9/22 11:23 AM
 */

@Data
public class ManageResultRQ {

    @NotNull
    private String lotteryType;
    @NotNull
    private String drawCode;
    private String rebateCode;
    @NotNull
    private String type;
    private String number;
    @NotNull
    private String postCode;
    private String userCode;

}
