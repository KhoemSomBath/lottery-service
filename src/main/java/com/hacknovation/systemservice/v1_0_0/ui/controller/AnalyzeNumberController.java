package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.analyzePercentage.AnalyzePercentageFromTemp;
import com.hacknovation.systemservice.v1_0_0.service.analyzePercentage.AnalyzePercentageSV;
import com.hacknovation.systemservice.v1_0_0.service.analyzeV2.AnalyzeV2SV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.analyze.AnalyzeV2RQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*
 * author: kangto
 * createdAt: 16/11/2022
 * time: 21:50
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1.0.0")
public class AnalyzeNumberController extends BaseController {

    private final AnalyzePercentageSV analyzePercentageSV;
    private final AnalyzePercentageFromTemp analyzePercentageFromTemp;
    private final AnalyzeV2SV analyzeV2SV;

    @GetMapping("analyze-number")
    public ResponseEntity<StructureRS> getFilterAnalyze(@RequestParam String filterByLotteryType,
                                                        @RequestParam(required = false, defaultValue = "percentage") String filterByTransferType,
                                                        @RequestParam(required = false, defaultValue = "ALL") String filterByDrawCode) {
        return response(analyzePercentageSV.getFilter(filterByLotteryType, filterByTransferType, filterByDrawCode));
    }

    @PostMapping("analyze-number")
    public ResponseEntity<StructureRS> postFilterAnalyze(@Valid @RequestBody AnalyzeRQ analyzeRQ) {
        return response(analyzePercentageFromTemp.getPostAnalyze(analyzeRQ));
    }

    @PostMapping("analyze-number-db")
    public ResponseEntity<StructureRS> postFilterAnalyzeDB(@Valid @RequestBody AnalyzeRQ analyzeRQ) {
        return response(analyzePercentageFromTemp.getPostAnalyze(analyzeRQ));
    }

    @GetMapping("analyze-number-v2")
    public ResponseEntity<StructureRS> getFilterAnalyzeV2(@RequestParam String lotteryType,
                                                          @RequestParam(required = false, defaultValue = "ALL") String drawCode) {
        return response(analyzeV2SV.getFilter(lotteryType.toUpperCase(), drawCode));
    }

    @PostMapping("analyze-number-v2")
    public ResponseEntity<StructureRS> postFilterAnalyzeV2(@Valid @RequestBody AnalyzeV2RQ analyzeV2RQ) {
        return response(analyzeV2SV.postAnalyze(analyzeV2RQ));
    }

}
