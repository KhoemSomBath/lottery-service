package com.hacknovation.systemservice.v1_0_0.utility.lottery;

import com.hacknovation.systemservice.v1_0_0.io.entity.UserEntity;
import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.io.nativeQuery.user.UserLevelReportTO;
import com.hacknovation.systemservice.v1_0_0.io.repo.UserHasLotteryRP;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * author: kangto
 * createdAt: 08/02/2022
 * time: 00:14
 */
@Component
@RequiredArgsConstructor
public class UserHasLotteryUtility {
    private final UserHasLotteryRP userHasLotteryRP;


    /**
     * get map user has lottery
     * @param lotteryType String
     * @param userCodes List<String>
     * @return Map<String, List<UserHasLotteryEntity>>
     */
    public Map<String, List<UserHasLotteryEntity>> getUserHasLotteryGroupByUser(String lotteryType, List<String> userCodes) {
        List<UserHasLotteryEntity> userHasLotteryEntities = userHasLotteryRP.findByLotteryCodeAndUserCodeIn(lotteryType, userCodes);
        return userHasLotteryEntities.stream().collect(Collectors.groupingBy(UserHasLotteryEntity::getUserCode, Collectors.toList()));
    }

    /**
     * get user has lottery entity
     * @param userHasLotteryEntityMap Map<String, UserHasLotteryEntity>
     * @param rebateCode String
     * @return UserHasLotteryEntity
     */
    public UserHasLotteryEntity getRebateAndCommission(Map<String, UserHasLotteryEntity> userHasLotteryEntityMap, String rebateCode) {
        UserHasLotteryEntity userHasLotteryEntity = new UserHasLotteryEntity();
        if (userHasLotteryEntityMap.containsKey(rebateCode)) {
            BeanUtils.copyProperties(userHasLotteryEntityMap.get(rebateCode), userHasLotteryEntity);
        }

        return userHasLotteryEntity;
    }

    public List<String> getUpperCodes(UserLevelReportTO user) {
        List<String> upperCodes = new ArrayList<>();
        if (user.getSuperSeniorCode() != null)
            upperCodes.add(user.getSuperSeniorCode());
        if (user.getSeniorCode() != null)
            upperCodes.add(user.getSeniorCode());
        if (user.getMasterCode() != null)
            upperCodes.add(user.getMasterCode());
        if (user.getAgentCode() != null)
            upperCodes.add(user.getAgentCode());
        upperCodes.add(user.getUserCode());
        return upperCodes;
    }

}
