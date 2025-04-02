package com.hacknovation.systemservice.v1_0_0.service.manageresult;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.manageresult.ManageResultRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;

/**
 * @author Sombath
 * create at 5/9/22 4:55 PM
 */
public interface ManageResultSV {
    StructureRS getManageResultForm(String lottery);
    StructureRS setManageResultForm(ManageResultRQ manageResultRQ);

    StructureRS lockPost(String postCode);
}
