package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.UserHasLotteryEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.CommissionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserHasLotteryRP extends JpaRepository<UserHasLotteryEntity, Long> {

    @Query(value = "SELECT * FROM user_has_lotteries WHERE lottery_code = :lotteryCode AND uc = :userCode AND rc = :rebateCode ", nativeQuery = true)
    UserHasLotteryEntity checkUserHasLottery(String lotteryCode, String userCode, String rebateCode);

    @Query(value = "SELECT * FROM user_has_lotteries WHERE lottery_code = :lotteryCode AND uc = :userCode and rc = :rebate", nativeQuery = true)
    UserHasLotteryEntity userLotteryByCodeAndRebateCode(String lotteryCode, String userCode, String rebate);

    @Query(value = "SELECT * FROM user_has_lotteries WHERE lottery_code = :lotteryCode AND uc IN :userCodes and rc = :rebate", nativeQuery = true)
    List<UserHasLotteryEntity> userHasLotteriesByLotteryUserCodeInRebate(String lotteryCode, List<String> userCodes, String rebate);

    @Query(value = "SELECT * FROM user_has_lotteries WHERE uc = :userCode ORDER BY id  ", nativeQuery = true)
    List<UserHasLotteryEntity> userHasLotteries(String userCode);

    List<UserHasLotteryEntity> findByLotteryCodeAndUserCodeIn(String lotteryCode, List<String> userCodes);

    @Query(value = "SELECT id, uc userCode, lottery_code lotteryType, rc rebateCode, rebate_rate rebateRate ,commission FROM user_has_lotteries WHERE uc IN :userCodes  ORDER BY id  ", nativeQuery = true)
    List<CommissionDTO> commission(List<String> userCodes);

    @Modifying
    @Transactional
    @Query(value = "update users u " +
            "       inner join user_has_lotteries uhl on u.code = uhl.uc " +
            "       inner join user_has_lotteries upperLevel on upperLevel.uc = :upperLevelCode AND upperLevel.lottery_code=uhl.lottery_code" +
            "        set uhl.max_bet_first      = upperLevel.max_bet_first, " +
            "        uhl.max_bet_second      = upperLevel.max_bet_second, " +
            "        uhl.max_bet_second_min = upperLevel.max_bet_second_min, " +
            "        uhl.max_bet_range = upperLevel.max_bet_range," +
            "        uhl.max_bet_item_range = upperLevel.max_bet_item_range, " +
            "        uhl.limit_digit        = upperLevel.limit_digit " +
            "    where  " +
            "    case when :upperRole = 'super-senior' then u.super_senior_code = :upperLevelCode " +
            "    when :upperRole = 'senior' then u.senior_code = :upperLevelCode " +
            "    when :upperRole = 'master' then u.master_code = :upperLevelCode " +
            "    when :upperRole = 'agent' then u.agent_code = :upperLevelCode " +
            "    else true  end " +
            "    AND upperLevel.lottery_code = :lottery  " +
            "    AND upperLevel.rc = :rebateCode " +
            "    AND uhl.lottery_code = :lottery " +
            "    AND uhl.rc = :rebateCode ", nativeQuery = true)
    void copyUserHasLotteriesFromUpperLevel(String upperLevelCode, String upperRole, String lottery, String rebateCode);

    Boolean existsByUserCodeAndLotteryCode(String userCode, String lotteryCode);
}
