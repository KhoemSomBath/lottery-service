package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRQ {
    private String lotteryType;
    private String platformType;
    private String userCode;
    private String nickname;
    private String status;
}
