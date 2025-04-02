package com.hacknovation.systemservice.v1_0_0.service.lockbetTemplate;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.lockbetTemplate.UpdateLockbetRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 13:57
 */
@Service
public interface LockbetTemplateSV {
    StructureRS listLockbet();
    StructureRS updateLockbet(UpdateLockbetRQ updateLockbetRQ);
}
