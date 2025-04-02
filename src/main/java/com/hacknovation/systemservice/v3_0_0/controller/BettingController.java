package com.hacknovation.systemservice.v3_0_0.controller;

import com.hacknovation.systemservice.v1_0_0.ui.controller.BaseController;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v3_0_0.servive.betting.BettingService;
import com.hacknovation.systemservice.v3_0_0.servive.betting.request.BaseBanhJiBettingRQ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Sombath
 * create at 23/6/23 1:17 AM
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0.0")
public class BettingController extends BaseController {

    private final BettingService bettingService;

    @PostMapping("{lotteryType}/betting")
    public ResponseEntity<StructureRS> betting(@PathVariable String lotteryType, @Valid @RequestBody BaseBanhJiBettingRQ request){
        request.setLotteryType(lotteryType);
        return response(bettingService.betting(request));
    }

}
