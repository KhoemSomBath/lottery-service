package com.hacknovation.systemservice.v1_0_0.io.repo;

import com.hacknovation.systemservice.v1_0_0.io.entity.HasLotteryTemplateEntity;
import com.hacknovation.systemservice.v1_0_0.ui.model.dto.hasLotteryTemplate.HasLotteryTemplateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * author: kangto
 * createdAt: 30/11/2022
 * time: 11:52
 */
@Repository
public interface HasLotteryTemplateRP extends JpaRepository<HasLotteryTemplateEntity, Long> {

    @Query(value = "SELECT * FROM has_lottery_template WHERE dow = :dayOfWeek AND lottery_code = :lotteryType AND uc = :userCode AND pc = :postCode AND rc = :rebateCode ", nativeQuery = true)
    HasLotteryTemplateEntity getOneTemplate(String dayOfWeek, String lotteryType, String userCode, String postCode, String rebateCode);


    @Query(value = "SELECT " +
            "hlt.dow dayOfWeek, " +
            "hlt.lottery_code lotteryType, " +
            "hlt.uc userCode, " +
            "u.username, " +
            "hlt.status, " +
            "hlt.pc postCode, " +
            "hlt.rc rebateCode, " +
            "hlt.limit_digit limitDigit, " +
            "hlt.updated_by updatedBy " +
            "FROM has_lottery_template hlt INNER JOIN users u ON hlt.uc=u.`code` " +
            "WHERE CASE WHEN :dayOfWeek <> 'ALL' THEN hlt.dow = :dayOfWeek ELSE TRUE END AND " +
            "IF(:lotteryType <> 'ALL', hlt.lottery_code = :lotteryType, TRUE) AND " +
            "IF(:userCode <> 'ALL', hlt.uc = :userCode, TRUE) " +
            "ORDER BY u.username, hlt.lottery_code, hlt.pc, hlt.rc ",
            nativeQuery = true)
    List<HasLotteryTemplateDTO> getItemList(String dayOfWeek, String lotteryType, String userCode);

}
