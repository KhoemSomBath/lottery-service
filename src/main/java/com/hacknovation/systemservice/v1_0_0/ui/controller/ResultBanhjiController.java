package com.hacknovation.systemservice.v1_0_0.ui.controller;


import com.hacknovation.systemservice.v1_0_0.service.resultBanhji.ResultBanhjiSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.resultBanhji.ResultBanhjiRS;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/*
 * author: kangto
 * createdAt: 21/11/2022
 * time: 10:55
 */
@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultBanhjiController extends BaseController {

    private final ResultBanhjiSV resultBanhjiSV;

    @GetMapping("{lotteryType}")
    public ResultBanhjiRS getResultByDate(@PathVariable String lotteryType, @RequestParam String resultedPostAt) {
        return resultBanhjiSV.getResultBanhjiByLotteryAndDate(lotteryType.toUpperCase(), resultedPostAt);
    }

}
