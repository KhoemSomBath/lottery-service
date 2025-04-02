package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasPostponeNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * author: kangto
 * createdAt: 14/02/2022
 * time: 09:45
 */
@Repository
public interface UserHasPostponeNumberRP extends JpaRepository<UserHasPostponeNumberEntity, Long> {

    UserHasPostponeNumberEntity findByLotteryTypeAndDrawCodeAndUserCode(String lotteryType, String drawCode, String userCode);

    @Transactional
    void deleteAllByLotteryTypeAndDrawCodeAndUserCodeAndNumberInAndIsAllMemberAndIsDefault(String lotteryType, String drawCode, String userCode, List<String> numbers, Boolean isAllMember, Boolean isDefault);

    List<UserHasPostponeNumberEntity> findAllByLotteryTypeAndDrawCodeInAndUserCodeOrderByNumberAsc(String lotteryType, List<String> drawCodes, String userCode);

    List<UserHasPostponeNumberEntity> findAllByLotteryTypeAndDrawCodeInAndUserCodeIn(String lotteryType, List<String> drawCode, List<String> userCodes);

}
