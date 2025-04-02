package com.hacknovation.systemservice.v3_0_0.controller;

import com.hacknovation.systemservice.v1_0_0.ui.controller.BaseController;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v3_0_0.servive.betting.BettingHelper;
import com.hacknovation.systemservice.v3_0_0.servive.betting.request.PagingRQ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0.0/paging")
@RequiredArgsConstructor
public class PagingController extends BaseController {

    @GetMapping()
    public ResponseEntity<StructureRS> listing(PagingRQ pagingRQ) {
        return response(BettingHelper.PAGING.listing(pagingRQ));
    }

    @PostMapping()
    public ResponseEntity<StructureRS> add(@RequestBody PagingRQ pagingRQ) {
        BettingHelper.PAGING.add(pagingRQ);
        return response();
    }

}
