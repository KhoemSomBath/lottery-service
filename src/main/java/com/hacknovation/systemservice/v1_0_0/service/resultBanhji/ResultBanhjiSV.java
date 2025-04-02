package com.hacknovation.systemservice.v1_0_0.service.resultBanhji;

import com.hacknovation.systemservice.v1_0_0.ui.model.response.resultBanhji.ResultBanhjiRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 21/11/2022
 * time: 11:03
 */
@Service
public interface ResultBanhjiSV {

    ResultBanhjiRS getResultBanhjiByLotteryAndDate(String lotteryType, String drawAt);

}
