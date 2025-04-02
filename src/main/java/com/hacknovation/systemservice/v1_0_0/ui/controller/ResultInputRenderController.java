package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.resultInput.ResultInputSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput.RenderNumberRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.resultInput.SetResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*
 * author: kangto
 * createdAt: 29/10/2022
 * time: 15:19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1.0.0/render")
public class ResultInputRenderController extends BaseController {

    private final ResultInputSV resultInputSV;

    @GetMapping("/{lotteryType}")
    @PreAuthorize("@customSecurityExpressionRoot.canContains('list-result-render')")
    public ResponseEntity<StructureRS> list(@PathVariable String lotteryType) {
        return response(resultInputSV.listResultItem(lotteryType));
    }

    @PostMapping("/begin/{lotteryType}")
    @PreAuthorize("@customSecurityExpressionRoot.canContains('create-result-render')")
    public ResponseEntity<StructureRS> startFormRender(@PathVariable String lotteryType) {
        return response(resultInputSV.showForm(lotteryType.toUpperCase()));
    }

    @PostMapping("/start")
    @PreAuthorize("@customSecurityExpressionRoot.canContains('create-result-render')")
    public ResponseEntity<StructureRS> startRenderNumber(@Valid @RequestBody RenderNumberRQ renderNumberRQ) {
        return response(resultInputSV.startRenderNumber(renderNumberRQ));
    }

    @PostMapping("/set-result")
    @PreAuthorize("@customSecurityExpressionRoot.canContains('create-result-render')")
    public ResponseEntity<StructureRS> setResultRender(@Valid @RequestBody SetResultRQ setResultRQ) {
        return response(resultInputSV.setResultRender(setResultRQ));
    }

    @PostMapping("/end/{lotteryType}")
    @PreAuthorize("@customSecurityExpressionRoot.canContains('create-result-render')")
    public ResponseEntity<StructureRS> closeDraw(@PathVariable String lotteryType) {
        return response(resultInputSV.closeDraw(lotteryType));
    }

}
