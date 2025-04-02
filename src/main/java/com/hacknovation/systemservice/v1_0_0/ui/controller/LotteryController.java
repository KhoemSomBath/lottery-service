package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.analyze.AnalyzeSV;
import com.hacknovation.systemservice.v1_0_0.service.betting.BettingSV;
import com.hacknovation.systemservice.v1_0_0.service.cancelticket.CancelTicketService;
import com.hacknovation.systemservice.v1_0_0.service.dashboard.DashboardService;
import com.hacknovation.systemservice.v1_0_0.service.drawing.DrawingSV;
import com.hacknovation.systemservice.v1_0_0.service.initialbalance.InitialBalanceSV;
import com.hacknovation.systemservice.v1_0_0.service.loan.LoanSV;
import com.hacknovation.systemservice.v1_0_0.service.manageresult.ManageResultSV;
import com.hacknovation.systemservice.v1_0_0.service.page.PagingSV;
import com.hacknovation.systemservice.v1_0_0.service.postponenumber.PostponeSV;
import com.hacknovation.systemservice.v1_0_0.service.report.ReportSV;
import com.hacknovation.systemservice.v1_0_0.service.report.SettlementReportService;
import com.hacknovation.systemservice.v1_0_0.service.report.TotalDailyReportService;
import com.hacknovation.systemservice.v1_0_0.service.result.ResultFull;
import com.hacknovation.systemservice.v1_0_0.service.result.ResultSV;
import com.hacknovation.systemservice.v1_0_0.service.settlement.SettlementSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.betting.UpdateIsSeenRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.cancelticket.CancelTicketRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.draw.UpdateDrawingRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.initialbalance.EditInitialBalanceRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.loan.EditLoanRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.manageresult.ManageResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.PostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.PublishResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.UpdateDrawItemsRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.result.full.FullResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.settlement.EditSettlementRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1.0.0")
public class LotteryController extends BaseController {

    private final AnalyzeSV analyzeSV;
    private final ResultSV resultSV;
    private final ReportSV reportSV;
    private final LoanSV loanSV;
    private final SettlementSV settlementSV;
    private final InitialBalanceSV initialBalanceSV;
    private final PostponeSV postponeSV;
    private final DrawingSV drawingSV;
    private final BettingSV bettingSV;
    private final PagingSV pagingSV;
    private final ActivityLogUtility activityLogUtility;
    private final DashboardService dashboardService;
    private final ResultFull resultFull;
    private final TotalDailyReportService totalDailyReportService;
    private final SettlementReportService settlementReportService;
    private final CancelTicketService cancelTicketService;

    // result
    private final ManageResultSV manageResultSV;

    @GetMapping("betting")
    public ResponseEntity<StructureRS> listBetting() {
        return response(bettingSV.listing());
    }

    @GetMapping("monitor-ticket")
    public ResponseEntity<StructureRS> monitorTicket(String lotteryType, @RequestParam(required = false, defaultValue = "ALL") String drawCode, @RequestParam(required = false, defaultValue = "0") Integer isMark, Integer size, Integer page) {
        return response(bettingSV.monitorTicket(lotteryType, drawCode, size, isMark, page));
    }


    @PostMapping("monitor-ticket/type")
    public ResponseEntity<StructureRS> ticketIsMark(@RequestBody @Valid UpdateIsSeenRQ request) {
        if(request.getType().equalsIgnoreCase("SEEN"))
            return response(bettingSV.updateIsSeen(request.getLottery(), request.getOrderId()));
        else
            return response(bettingSV.updateIsMark(request.getLottery(), request.getOrderId()));
    }

    @GetMapping("paging")
    public ResponseEntity<StructureRS> paging() {
        return response(pagingSV.listing());
    }

    /**
     * Manage results
     */

    @GetMapping("result/all")
    public ResponseEntity<StructureRS> getAllResult(@RequestParam String lotteryType) {
        return response(resultSV.getAllResult(lotteryType));
    }

    @GetMapping("result")
    public ResponseEntity<StructureRS> result(@RequestParam String lotteryType, @RequestParam String drawCode, @RequestParam(required = false) String filterByDate) {
        return response(resultSV.getResult(lotteryType, drawCode, filterByDate));
    }

    @PreAuthorize("@customSecurityExpressionRoot.canContains('list-result-full')")
    @GetMapping("result-full")
    public ResponseEntity<StructureRS> resultFull(@RequestParam String lotteryType, @RequestParam String drawCode, @RequestParam String filterByDate) {
        return response(resultFull.getResult(drawCode, filterByDate, lotteryType));
    }

    @PreAuthorize("@customSecurityExpressionRoot.canContains('create-result-full')")
    @PostMapping("result-full")
    public ResponseEntity<StructureRS> resultFull(@RequestBody @Valid FullResultRQ fullResultRQ) {
        return response(resultFull.setResult(fullResultRQ, true));
    }

    @PreAuthorize("@customSecurityExpressionRoot.canContains('list-result-full')")
    @GetMapping("result-full-only")
    public ResponseEntity<StructureRS> resultFullOnly(@RequestParam String lotteryType, @RequestParam String drawCode, @RequestParam String filterByDate) {
        return response(resultFull.getResult(drawCode, filterByDate, lotteryType));
    }

    @PreAuthorize("@customSecurityExpressionRoot.canContains('create-result-full')")
    @PostMapping("result-full-only")
    public ResponseEntity<StructureRS> resultFullOnly(@RequestBody @Valid FullResultRQ fullResultRQ) {
        return response(resultFull.setResult(fullResultRQ, false));
    }

    @PreAuthorize("@customSecurityExpressionRoot.canContains('create-result-full')")
    @PostMapping("publish-result-full")
    public ResponseEntity<StructureRS> publishResultFull(@Valid @RequestBody PublishResultRQ publishResultRQ) {
        return response(resultFull.publishResult(publishResultRQ));
    }

    @PostMapping("result")
    public ResponseEntity<StructureRS> setResult(@Valid @RequestBody UpdateDrawItemsRQ updateDrawItemsRQ) {
        return response(resultSV.setResult(updateDrawItemsRQ));
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('create-result')")
    @PostMapping("publish-result")
    public ResponseEntity<StructureRS> publishResult(@Valid @RequestBody PublishResultRQ publishResultRQ) {
        return response(resultSV.publishResult(publishResultRQ));
    }

    /**
     * Manage draw
     */
    @GetMapping("shift-drawing")
    public ResponseEntity<StructureRS> getDrawing(@RequestParam String lotteryType, @RequestParam String filterByDate) {
        return response(drawingSV.drawShifts(lotteryType, filterByDate));
    }

    @PutMapping("drawing")
    public ResponseEntity<StructureRS> updateDraw(@Valid @RequestBody UpdateDrawingRQ updateDrawingRQ) {
        return response(drawingSV.updateDraw(updateDrawingRQ));
    }

    /**
     * Postpone Number
     */
    @GetMapping("postpone-number")
    public ResponseEntity<StructureRS> postponeNumber() {
        return response(postponeSV.listing());
    }

    @PostMapping("postpone-number")
    public ResponseEntity<StructureRS> editPostponeNumber(@Valid @RequestBody PostponeRQ postponeRQ) {
        return response(postponeSV.updatePostpone(postponeRQ));
    }


    @GetMapping("total-report")
    public ResponseEntity<StructureRS> totalReport() {
        return response(totalDailyReportService.getReport());
    }

    /**
     * Settlement
     */
    @GetMapping("settlement")
    public ResponseEntity<StructureRS> settlementList() {
        return response(settlementReportService.getSettlementList());
    }

    @PostMapping("settlement")
    public ResponseEntity<StructureRS> editSettlement(@Valid @RequestBody EditSettlementRQ request) {
        return response(settlementSV.addOrEdit(request));
    }

    /**
     * Loan
     */
    @GetMapping("loan")
    public ResponseEntity<StructureRS> loan(@RequestParam String lotteryType, @RequestParam String filterDate, @RequestParam String agentCode) {
        return response(loanSV.getLoan(lotteryType, agentCode, filterDate));
    }

    @PutMapping("loan")
    public ResponseEntity<StructureRS> editLoan(@Valid @RequestBody EditLoanRQ request) {
        return response(loanSV.addOrEdit(request));
    }

    /**
     * Initial Balance
     */
    @GetMapping("initial-balance")
    public ResponseEntity<StructureRS> initialBalance(@RequestParam(required = false, defaultValue = "ALL") String userCode, @RequestParam(required = false) String filterByLevel) {
        return response(initialBalanceSV.initialBalance(userCode, filterByLevel));
    }

    @PutMapping("initial-balance")
    public ResponseEntity<StructureRS> addOrEditInitialBalance(@RequestBody EditInitialBalanceRQ request) {
        return response(initialBalanceSV.addOrEditInitialBalance(request));
    }

    /**
     * Result
     */
    @PreAuthorize("@customSecurityExpressionRoot.canContains('list-manage-result')")
    @PostMapping("manage-result")
    public ResponseEntity<StructureRS> manageResult(@RequestParam String lotteryType) {
        return response(manageResultSV.getManageResultForm(lotteryType));
    }

    @PostMapping("lock-post")
    public ResponseEntity<StructureRS> lockPost(@RequestParam String postCode) {
        return response(manageResultSV.lockPost(postCode));
    }

    @PreAuthorize("@customSecurityExpressionRoot.canContains('update-manage-result')")
    @PutMapping("manage-result")
    public ResponseEntity<StructureRS> setManageResult(@RequestBody @Valid ManageResultRQ manageResultRQ) {
        return response(manageResultSV.setManageResultForm(manageResultRQ));
    }

    /**
     * Analyze
     */
    @GetMapping("selling-lottery")
    public ResponseEntity<StructureRS> analyseSelling() {
        return response(analyzeSV.analyseSelling());
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('list-analyze-report')")
    @GetMapping("analyze-log")
    public ResponseEntity<StructureRS> analyseLog(@RequestParam String lotteryType, String drawCode) {
        return response(analyzeSV.analyzeLog(lotteryType, drawCode));
    }

    /**
     * Reports
     */
    @GetMapping("daily-report")
    public ResponseEntity<StructureRS> dailyReport() {
        return response(reportSV.dailyReport());
    }

    @GetMapping("member-report")
    public ResponseEntity<StructureRS> memberReport() {
        return response(reportSV.memberReport());
    }

    @PostMapping("cancel-ticket")
    public ResponseEntity<StructureRS> cancelTicket(@Validated @RequestBody CancelTicketRQ cancelTicketRQ) {
        return response(cancelTicketService.cancelTicket(cancelTicketRQ.getId(), cancelTicketRQ.getLotteryType(), cancelTicketRQ.getDrawCode(), cancelTicketRQ.getStatus()));
    }

    @GetMapping("ticket-report")
    public ResponseEntity<StructureRS> ticketReport() {
        return response(reportSV.ticketReport());
    }

    @GetMapping("clearing-report")
    public ResponseEntity<StructureRS> clearingReport() {
        return response(reportSV.clearingReport());
    }

    // Log
    @GetMapping("activity-log")
    public ResponseEntity<StructureRS> activityLog(
            @RequestParam(required = false) String lotteryType,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String username
    ) {
        return response(activityLogUtility.getActivityLog(lotteryType, module, username));
    }


    @GetMapping("activity-log-module")
    public ResponseEntity<StructureRS> activityLogModule() {
        return response(activityLogUtility.getModuleOption());
    }

    @GetMapping("dashboard/summary-user-levels")
    public ResponseEntity<StructureRS> userSummaries() {
        return response(dashboardService.getUserSummaries());
    }


}