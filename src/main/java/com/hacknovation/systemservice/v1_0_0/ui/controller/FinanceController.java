package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.finance.FinanceSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.finance.FinanceRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1.0.0/finance")
@RequiredArgsConstructor
public class FinanceController extends BaseController {

    private final FinanceSV financeSV;

    @GetMapping("balance")
    public ResponseEntity<StructureRS> balance() {
        return response(financeSV.balance());
    }

    @PostMapping("transaction")
    public ResponseEntity<StructureRS> transaction(@Valid @RequestBody FinanceRQ financeRQ) {
        return response(financeSV.transaction(financeRQ));
    }

    @GetMapping("listing/{userCode}")
    public ResponseEntity<StructureRS> listing(@PathVariable String userCode) {
        return response(financeSV.listing(userCode));
    }

    @GetMapping("listing")
    public ResponseEntity<StructureRS> listMobile() {
        return response(financeSV.listMobile());
    }

}
