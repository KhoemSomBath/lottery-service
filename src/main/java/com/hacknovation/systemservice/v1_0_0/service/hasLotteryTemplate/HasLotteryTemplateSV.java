package com.hacknovation.systemservice.v1_0_0.service.hasLotteryTemplate;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.hasLotteryTemplate.HasLotteryTemplateRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 30/11/2022
 * time: 12:05
 */
@Service
public interface HasLotteryTemplateSV {

    StructureRS listHasLotteryTemplate();
    StructureRS createOrUpdateTemplate(HasLotteryTemplateRQ hasLotteryTemplateRQ);

}
