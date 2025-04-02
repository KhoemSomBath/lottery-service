package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.config.ConfigSV;
import com.hacknovation.systemservice.v1_0_0.service.hasLotteryTemplate.HasLotteryTemplateSV;
import com.hacknovation.systemservice.v1_0_0.service.lockbetTemplate.LockbetTemplateSV;
import com.hacknovation.systemservice.v1_0_0.service.postRelease.PostReleaseSV;
import com.hacknovation.systemservice.v1_0_0.service.postponenumber.PostponeSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.RebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.CheckHasLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.UpdateRebateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.config.UpdateUserHasLotteryRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.hasLotteryTemplate.HasLotteryTemplateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.lockbetTemplate.UpdateLockbetRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postRelease.UpdatePostReleaseRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.ListUserHasPostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.UpdateUserHasPostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/v1.0.0/config")
@RequiredArgsConstructor
public class ConfigController extends BaseController {

    private final ConfigSV configSV;
    private final PostponeSV postponeSV;
    private final LockbetTemplateSV lockbetTemplateSV;
    private final PostReleaseSV postReleaseSV;
    private final HasLotteryTemplateSV hasLotteryTemplateSV;


    /**
     * Has lottery
     */
    @GetMapping()
    public ResponseEntity<StructureRS> userHasLottery() {
        return response(configSV.userHasLottery());
    }

    @GetMapping("create/{userCode}")
    public ResponseEntity<StructureRS> fetchUserHasLotteryWhenCreate(@PathVariable String userCode) {
        return response(configSV.getUserHasLotteryCreate(userCode));
    }

    @GetMapping("{userCode}")
    public ResponseEntity<StructureRS> getUserHasLottery(@PathVariable String userCode) {
        return response(configSV.fetchUserHasLotteryList(userCode));
    }

    @PostMapping()
    public ResponseEntity<StructureRS> updateUserHasLottery(@Valid @RequestBody UpdateUserHasLotteryRQ updateUserHasLotteryRQ) {
        return response(configSV.updateUserHasLottery(updateUserHasLotteryRQ));
    }

    @PostMapping("check-has-lottery")
    public ResponseEntity<StructureRS> checkShareCommission(@Valid @RequestBody CheckHasLotteryRQ checkHasLotteryRQ) {
        return response(configSV.checkShareCommission(checkHasLotteryRQ));
    }

    @PostMapping("rebate-sixd")
    public ResponseEntity<StructureRS> updateRebateSixD(@Valid @RequestBody UpdateRebateRQ updateRebateRQ) {
        return response(configSV.updateRebateSixD(updateRebateRQ));
    }

    /**
     * Manage lottery
     */
    @GetMapping("type")
    public ResponseEntity<StructureRS> lottery(@RequestParam(required = false) String userCode, @RequestParam(required = false, defaultValue = "parent") String type) {
        return response(configSV.lottery(userCode, type));
    }

    @GetMapping("rebate")
    public ResponseEntity<StructureRS> rebates() {
        return response(configSV.listRebate());
    }

    @GetMapping("filter-able-rebate")
    public ResponseEntity<StructureRS> filterAbleRebate(@RequestParam String filterByLevel, @RequestParam String filterByLotteryType) {
        return  response(configSV.filterAbleRebate(filterByLotteryType, filterByLevel));
    }

    @PostMapping("rebate/{id}")
    public ResponseEntity<StructureRS> rebateUpdate(@PathVariable("id") Integer id, @RequestBody RebateRQ rebateRQ) {
        return response(configSV.updateRebate(id, rebateRQ));
    }

    /**
     * User has postponeNumber
     */
    @GetMapping("postpone")
    public ResponseEntity<StructureRS> listUserHasPostpone(@RequestParam String filterByDrawCode) {
        return  response(postponeSV.userHasPostponeList());
    }

    @PostMapping("postpone")
    public ResponseEntity<StructureRS> updateUserHasPostpone(@Valid @RequestBody UpdateUserHasPostponeRQ postponeRQ) {
        return response(postponeSV.updateUserHasPostpone(postponeRQ));
    }

    @DeleteMapping("postpone/{itemId}")
    public ResponseEntity<StructureRS> removeUserHasPostpone(@PathVariable Integer itemId) {
        return response(postponeSV.removeUserHasPostpone(itemId));
    }

    @GetMapping("shifts")
    public ResponseEntity<StructureRS> shift(@RequestParam(required = false, defaultValue = "ALL") String lotteryType) {
        return response(configSV.shift(lotteryType));
    }

    @GetMapping("lockbet-template")
    public ResponseEntity<StructureRS> listLockbetTemplate() {
        return response(lockbetTemplateSV.listLockbet());
    }

    @PostMapping("lockbet-template")
    public ResponseEntity<StructureRS> updateLockbetTemplate(@Valid @RequestBody UpdateLockbetRQ updateLockbetRQ) {
        return response(lockbetTemplateSV.updateLockbet(updateLockbetRQ));
    }

    @GetMapping("lottery-type")
    public ResponseEntity<StructureRS> getLotteryType(){
        return response(configSV.getLotteryType());
    }


    @GetMapping("post-release")
    public ResponseEntity<StructureRS> getPostReleaseConfig(@RequestParam(required = false, value = "ALL") String filterByLotteryType) {
        return response(postReleaseSV.listPostRelease(filterByLotteryType));
    }

    @PutMapping("post-release")
    public ResponseEntity<StructureRS> updatePostReleaseConfig(@Valid @RequestBody UpdatePostReleaseRQ updatePostReleaseRQ) {
        return response(postReleaseSV.updatePostRelease(updatePostReleaseRQ));
    }


    @GetMapping("has-lottery-template")
    public ResponseEntity<StructureRS> listHasLotteryTemplate() {
        return response(hasLotteryTemplateSV.listHasLotteryTemplate());
    }

    @PostMapping("has-lottery-template")
    public ResponseEntity<StructureRS> createHasLotteryTemplate(@Valid @RequestBody HasLotteryTemplateRQ lotteryTemplateRQ) {
        return response(hasLotteryTemplateSV.createOrUpdateTemplate(lotteryTemplateRQ));
    }

}
