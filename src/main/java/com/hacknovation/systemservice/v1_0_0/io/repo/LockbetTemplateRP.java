package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.LockbetTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * author: kangto
 * createdAt: 17/03/2022
 * time: 12:15
 */
@Repository
public interface LockbetTemplateRP extends JpaRepository<LockbetTemplateEntity, Long> {

    List<LockbetTemplateEntity> findAllByLotteryTypeAndUserCodeAndShiftCode(String lotteryType, String userCode, String shiftCode);

    LockbetTemplateEntity findByLotteryTypeAndDayOfWeekAndShiftCodeAndUserCode(String lotteryType, String dayOfWeek, String ShiftCode, String userCode);

}
