package com.hacknovation.systemservice.v1_0_0.service.cache;

import com.hacknovation.systemservice.constant.HazelcastConstant;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * author: kangto
 * createdAt: 01/10/2022
 * time: 21:24
 */
@Service
@RequiredArgsConstructor
public class UserCacheIP implements UserCacheSV {

    private final HazelcastInstance hazelcastInstance;

    @Override
    public void removeUserCacheByUsername(String username) {
        hazelcastInstance.getMap(HazelcastConstant.MEMBER_USERS_COLLECTION).delete(username.toUpperCase());
    }

    @Override
    public void removeUserCacheAll() {
        hazelcastInstance.getMap(HazelcastConstant.MEMBER_USERS_COLLECTION).clear();
    }

    @Async
    public void removeMaxBetItemByUserCode(String userCode) {
        hazelcastInstance.getMap(HazelcastConstant.MAX_BET_ITEMS_COLLECTION).delete(userCode);
    }

    @Async
    public void removeMaxBetItemByUserCodeIn(List<String> userCodes) {
        userCodes.forEach(this::removeMaxBetItemByUserCode);
    }

    @Async
    public void removeMaxBetItemAll() {
        hazelcastInstance.getMap(HazelcastConstant.MAX_BET_ITEMS_COLLECTION).clear();
    }

    @Override
    public void removeHasLotteryTemplate(String key) {
        hazelcastInstance.getMap(HazelcastConstant.HAS_LOTTERY_TEMPLATE_COLLECTION).delete(key);
    }

}
