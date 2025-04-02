package com.hacknovation.systemservice.v1_0_0.ui.model.response.initialbalance;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 18/02/2022
 * time: 14:16
 */
@Data
public class InitialBalanceRS {
    private Boolean isEditable = Boolean.TRUE;
    private List<InitialBalanceListRS> items = new ArrayList<>();
}
