package com.hacknovation.systemservice.v1_0_0.service.postRelease;

import com.hacknovation.systemservice.v1_0_0.ui.model.request.postRelease.UpdatePostReleaseRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 30/08/2022
 * time: 11:52
 */
@Service
public interface PostReleaseSV {
    StructureRS listPostRelease(String lotteryType);
    StructureRS updatePostRelease(UpdatePostReleaseRQ updatePostReleaseRQ);
}
