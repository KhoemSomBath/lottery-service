package com.hacknovation.systemservice.v1_0_0.ui.model.request.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 28/01/2022
 * time: 13:44
 */
@Data
public class CommissionLotteryRQ {
    private String lotteryType;
    private List<CommissionRQ> items = new ArrayList<>();
}
