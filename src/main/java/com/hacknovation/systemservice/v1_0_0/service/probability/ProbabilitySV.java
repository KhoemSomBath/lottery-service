package com.hacknovation.systemservice.v1_0_0.service.probability;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.probability.*;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * author: kangto
 * createdAt: 25/08/2022
 * time: 22:37
 */
@Service
public interface ProbabilitySV {

    StructureRS probabilityRule();
    StructureRS updateProbabilityRule(UpdateProbabilityRuleRQ updateProbabilityRuleRQ);

    StructureRS probabilityKeyList();

    StructureRS probabilityList();
    StructureRS updateProbability(List<UpdateProbabilityRQ> updateProbabilityRQList);

    StructureRS probabilityDrawList();
    StructureRS updateProbabilityDraw(List<UpdateProbabilityDrawRQ> updateProbabilityDrawRQList);

    StructureRS updateAllProbability(UpdateAllProbabilityRQ updateAllProbabilityRQ);
    StructureRS updateAllProbabilityDraw(UpdateAllProbabilityDrawRQ updateAllProbabilityDrawRQ);
}
