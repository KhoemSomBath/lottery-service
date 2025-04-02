package com.hacknovation.systemservice.v1_0_0.service.postRelease;

import com.hacknovation.systemservice.constant.ActivityLogConstant;
import com.hacknovation.systemservice.constant.MessageConstant;
import com.hacknovation.systemservice.v1_0_0.io.entity.PostReleaseConfigEntity;
import com.hacknovation.systemservice.v1_0_0.io.repo.PostReleaseConfigRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.ui.model.request.postRelease.UpdatePostReleaseRQ;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.utility.ActivityLogUtility;
import com.hacknovation.systemservice.v1_0_0.utility.auth.JwtToken;
import com.hacknovation.systemservice.v1_0_0.utility.auth.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 30/08/2022
 * time: 11:54
 */
@Service
@RequiredArgsConstructor
public class PostReleaseIP extends BaseServiceIP implements PostReleaseSV {

    private final PostReleaseConfigRP postReleaseConfigRP;
    private final ActivityLogUtility activityLogUtility;
    private final JwtToken jwtToken;

    @Override
    public StructureRS listPostRelease(String lotteryType) {
        return responseBodyWithSuccessMessage(postReleaseConfigRP.findAllByLotteryType(lotteryType));
    }

    @Override
    public StructureRS updatePostRelease(UpdatePostReleaseRQ updatePostReleaseRQ) {

        PostReleaseConfigEntity postReleaseConfigEntity = postReleaseConfigRP.findByLotteryTypeAndPostCode(updatePostReleaseRQ.getLotteryType(), updatePostReleaseRQ.getPostCode());
        if (postReleaseConfigEntity == null)
            return responseBodyWithBadRequest(MessageConstant.DATA_NOT_FOUND, MessageConstant.DATA_NOT_FOUND_KEY);

        UserToken token = jwtToken.getUserToken();

        postReleaseConfigEntity.setIsCanRelease(updatePostReleaseRQ.getIsCanRelease());
        postReleaseConfigEntity.setUpdatedBy(token.getUsername());
        postReleaseConfigRP.save(postReleaseConfigEntity);

        activityLogUtility.addActivityLog(
                updatePostReleaseRQ.getLotteryType(),
                ActivityLogConstant.MODULE_POST_RELEASE_CONFIG,
                token.getUserType(),
                ActivityLogConstant.ACTION_UPDATE,
                token.getUserCode(),
                postReleaseConfigEntity);

        return responseBodyWithSuccessMessage();
    }
}
