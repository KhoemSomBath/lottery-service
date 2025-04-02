package com.hacknovation.systemservice.v1_0_0.service.hazelcast;

import com.hacknovation.systemservice.constant.HazelcastConstant;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserRP;
import com.hacknovation.systemservice.v1_0_0.service.baseservice.BaseServiceIP;
import com.hacknovation.systemservice.v1_0_0.service.cache.UserCacheSV;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.StructureRS;
import com.hacknovation.systemservice.v1_0_0.ui.model.response.hazelcast.HazelcastCountRS;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * author: kangto
 * createdAt: 29/12/2022
 * time: 14:49
 */
@Service
@RequiredArgsConstructor
public class HazelcastIP extends BaseServiceIP implements HazelcastSV {

    private final HazelcastInstance hazelcastInstance;
    private final UserRP userRP;
    private final UserCacheSV userCacheSV;

    @Override
    public StructureRS countOnline() {
        HazelcastCountRS countRS = new HazelcastCountRS();

        countRS.setOnlineMembers(hazelcastInstance.getMap(HazelcastConstant.MEMBER_USERS_COLLECTION).size());
        countRS.setSubscribers(hazelcastInstance.getMap(HazelcastConstant.SUBSCRIBE_COLLECTION).size());

        return responseBodyWithSuccessMessage(countRS);
    }

    @Override
    public void logoutAllUser() {
        userRP.updateLastLoginAll();
        userCacheSV.removeUserCacheAll();
    }
}
