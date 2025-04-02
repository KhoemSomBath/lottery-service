package com.hacknovation.systemservice.v1_0_0.ui.model.response.postponenumber;

import lombok.Data;

import java.util.Date;

/*
 * author: kangto
 * createdAt: 14/02/2022
 * time: 09:51
 */
@Data
public class UserHasPostponeRS {
    private String lotteryType;
    private String drawCode;
    private Date drawAt;
    private String userCode;
    private String upperPostpone;
    private String numberDetail;
    private String fullPostpone;

    public String getUpperPostpone() {
        if (upperPostpone == null)
            return "";
        return upperPostpone;
    }

    public String getNumberDetail() {
        if (numberDetail == null)
            return "";
        return numberDetail;
    }

    public String getFullPostpone() {
        if (fullPostpone == null)
            return "";
        return fullPostpone;
    }

}
