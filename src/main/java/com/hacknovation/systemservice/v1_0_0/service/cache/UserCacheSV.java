package com.hacknovation.systemservice.v1_0_0.service.cache;

import org.springframework.stereotype.Service;

import java.util.List;

/*
 * author: kangto
 * createdAt: 01/10/2022
 * time: 21:23
 */
@Service
public interface UserCacheSV {
    void removeUserCacheByUsername(String username);
    void removeUserCacheAll();

    void removeMaxBetItemByUserCode(String userCode);
    void removeMaxBetItemByUserCodeIn(List<String> userCodes);
    void removeMaxBetItemAll();

    void removeHasLotteryTemplate(String key);
}
