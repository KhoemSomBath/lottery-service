package com.hacknovation.systemservice.v1_0_0.ui.controller;

import com.hacknovation.systemservice.v1_0_0.service.probability.ProbabilitySV;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.probability.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 22:36
 */
@RestController
@RequestMapping(value = "/api/v1.0.0")
@RequiredArgsConstructor
public class ProbabilityController extends BaseController {

    private final ProbabilitySV probabilitySV;


    @PreAuthorize("@customSecurityExpressionRoot.can('list-probability-type')")
    @GetMapping("manage-probability-rule")
    public ResponseEntity<StructureRS> getProbabilityRule() {
        return response(probabilitySV.probabilityRule());
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('update-probability-type')")
    @PutMapping("manage-probability-rule")
    public ResponseEntity<StructureRS> updateProbabilityRule(@Valid @RequestBody UpdateProbabilityRuleRQ updateProbabilityRuleRQ) {
        return response(probabilitySV.updateProbabilityRule(updateProbabilityRuleRQ));
    }

    @GetMapping("manage-probability/types")
    public ResponseEntity<StructureRS> getProbabilityKey() {
        return response(probabilitySV.probabilityKeyList());
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('list-probability')")
    @GetMapping("manage-probability")
    public ResponseEntity<StructureRS> getProbabilityList() {
        return response(probabilitySV.probabilityList());
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('update-probability')")
    @PutMapping("manage-probability")
    public ResponseEntity<StructureRS> updateProbability(@RequestBody List<@Valid UpdateProbabilityRQ> updateProbabilityRQList) {
        return response(probabilitySV.updateProbability(updateProbabilityRQList));
    }


    @PreAuthorize("@customSecurityExpressionRoot.can('list-probability-draw')")
    @GetMapping("manage-probability-draw")
    public ResponseEntity<StructureRS> getProbabilityDrawList() {
        return response(probabilitySV.probabilityDrawList());
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('update-probability-draw')")
    @PutMapping("manage-probability-draw")
    public ResponseEntity<StructureRS> updateProbabilityDraw(@RequestBody List<@Valid UpdateProbabilityDrawRQ> updateProbabilityDrawRQList) {
        return response(probabilitySV.updateProbabilityDraw(updateProbabilityDrawRQList));
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('update-probability')")
    @PutMapping("manage-probability-all")
    public ResponseEntity<StructureRS> updateAllProbability(@RequestBody @Valid UpdateAllProbabilityRQ updateAllProbabilityRQ) {
        return response(probabilitySV.updateAllProbability(updateAllProbabilityRQ));
    }

    @PreAuthorize("@customSecurityExpressionRoot.can('update-probability-draw')")
    @PutMapping("manage-probability-draw-all")
    public ResponseEntity<StructureRS> updateAllProbabilityDraw(@RequestBody @Valid UpdateAllProbabilityDrawRQ updateAllProbabilityRQ) {
        return response(probabilitySV.updateAllProbabilityDraw(updateAllProbabilityRQ));
    }

}
