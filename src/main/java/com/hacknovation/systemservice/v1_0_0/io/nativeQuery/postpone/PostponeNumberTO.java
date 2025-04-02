package com.hacknovation.systemservice.v1_0_0.io.nativeQuery.postpone;

import lombok.Data;

/*
 * author: kangto
 * createdAt: 26/01/2022
 * time: 15:06
 */
@Data
public class PostponeNumberTO {
    private String lotteryType;
    private String numberDetail;
    private Boolean status;
}
