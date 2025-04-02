package com.hacknovation.systemservice.v1_0_0.service.postponenumber;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.PostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.ListUserHasPostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postponenumber.UpdateUserHasPostponeRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 21/01/2022
 * time: 18:35
 */
@Service
public interface PostponeSV {
    StructureRS listing();
    StructureRS updatePostpone(PostponeRQ postponeRQ);

    StructureRS userHasPostponeList();
    StructureRS updateUserHasPostpone(UpdateUserHasPostponeRQ userHasPostponeRQ);
    StructureRS removeUserHasPostpone(Integer itemId);
}
