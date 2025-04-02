package com.hacknovation.systemservice.v2_0_0.controller;

import com.hacknovation.systemservice.v1_0_0.ui.controller.BaseController;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v2_0_0.component.dailyreport.AllDailyReportService;
import com.hacknovation.systemservice.v2_0_0.component.mixsettlement.MixSettlementService;
import com.hacknovation.systemservice.v2_0_0.component.totalreport.AllTotalReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sombath
 * create at 25/4/23 1:21 PM
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1.0.0/report")
public class ReportController extends BaseController {

    private final AllDailyReportService dailyReportService;
    private final AllTotalReportService allTotalReportService;
    private final MixSettlementService mixSettlementService;

    @GetMapping("daily-report/all")
    public ResponseEntity<StructureRS> getDailyReport() {
        return response(dailyReportService.getReport());
    }

    @GetMapping("total-report/all")
    public ResponseEntity<StructureRS> getTotalReport() {
        return response(allTotalReportService.getReport());
    }

    @GetMapping("settlement-report/all")
    public ResponseEntity<StructureRS> getMixSettlement() {
        return response(mixSettlementService.getSettlementList());
    }

}
