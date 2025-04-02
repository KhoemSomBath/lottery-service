package com.hacknovation.systemservice.v1_0_0.ui.model.response.resultBanhji;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
 * author: kangto
 * createdAt: 21/11/2022
 * time: 10:57
 */
@Data
public class ResultBanhjiRS {
    private Integer status = 0;
    private String lotteryType;
    private Boolean isNight = false;
    private List<ResultBanhjiItemRS> results = new ArrayList<>();
    private String message = "Successfully";
}
